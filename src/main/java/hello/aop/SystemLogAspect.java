package hello.aop;

import hello.bean.Log;
import hello.bean.User;
import hello.service.LogService;
import hello.util.DateUtils;
import hello.util.JacksonUtils;
import hello.util.StringUtils;
import hello.util.UuidUtils;
import io.swagger.annotations.ApiOperation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 系统日志切点类
 * @author linrx
 *
 */
@Aspect
@Component
public class SystemLogAspect {
	private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);
	/**用户session key 正式环境从配置文件中取,这里仅作为演示*/
	private static final String SESSION_USER_KEY = "user";
	private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");
	private static final ThreadLocal<Log> logThreadLocal = new NamedThreadLocal<Log>("ThreadLocal log");
	private static final ThreadLocal<User> currentUser=new NamedThreadLocal<>("ThreadLocal user");
	
	@Autowired(required=false)
	private HttpServletRequest request;
	
	//@Autowired
	//private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private LogService logService;

	/**Controller层切点 注解拦截*/
	@Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
	public void controllerAspect(){}

	/**
	 * 前置通知 用于拦截Controller层记录用户的操作的开始时间
	 * @param joinPoint 切点
	 * @throws InterruptedException 
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) throws InterruptedException{
		Date beginTime=new Date();
		beginTimeThreadLocal.set(beginTime);
		//debug模式下 显式打印开始时间用于调试
		if (logger.isDebugEnabled()){
	        logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
	        	.format(beginTime), request.getRequestURI());
		}
		//读取session中的用户 
		currentUser.set(this.getCurrentUser(request));
	}
	
	/**
	 * 后置通知 用于拦截Controller层记录用户的操作
	 * @param joinPoint 切点
	 */
	@After("controllerAspect()")
	public void doAfter(JoinPoint joinPoint) {
        User user = currentUser.get();
        if(user == null){
        	//登入login操作 前置通知时用户未校验 所以session中不存在用户信息
    	    user = this.getCurrentUser(request);
    	    if(user==null){
    	    	return;
    	    }
        }
		Log log=new Log();
		log.setLogId(UuidUtils.creatUUID());
		log.setTitle(getControllerMethodDescription2(joinPoint));
		log.setType(Log.TYPE_INFO);
		log.setRemoteAddr(request.getRemoteAddr());
		log.setRequestUri(request.getRequestURI());
		log.setMethod(request.getMethod());
		log.setParams(this.buildRequestParams(request.getParameterMap(), joinPoint.getArgs()));
		log.setUserId(user.getId());
		//得到线程绑定的局部变量--获取前置通知设置的开始时间
		log.setOperateDate(beginTimeThreadLocal.get());
		long beginTime = beginTimeThreadLocal.get().getTime();
		long endTime = System.currentTimeMillis();
		log.setTimeout(DateUtils.formatDateTime(System.currentTimeMillis() - beginTimeThreadLocal.get().getTime()));

    	// debu模式下打印JVM信息。
		if (logger.isDebugEnabled()){
	        logger.debug("计时结束：{}  URI: {}  耗时： {}   最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
	        		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(endTime), 
	        		request.getRequestURI(), 
	        		DateUtils.formatDateTime(endTime - beginTime),
					Runtime.getRuntime().maxMemory()/1024/1024, 
					Runtime.getRuntime().totalMemory()/1024/1024, 
					Runtime.getRuntime().freeMemory()/1024/1024, 
					(Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024); 
		}

    	 //1.直接执行保存操作
        //this.logService.createLog(log);
        //2.优化:异步保存日志
        new Thread(new SaveLogThread(log, logService)).start();
        //3.再优化:通过线程池来执行日志保存
        //threadPoolTaskExecutor.execute(new SaveLogThread(log, logService));
    	// 演示直接打印
    	//System.out.println(log);
        logThreadLocal.set(log);
	}
	
	/**
	 *  异常通知 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")  
	public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		Log log = logThreadLocal.get();
		if(log != null){
			log.setType(Log.TYPE_ERROR);
			log.setException(e.toString());
			new UpdateLogThread(log, logService).start();			
		}
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * 
	 * @param joinPoint 切点
	 * @return 方法描述
	 */
	public static String getControllerMethodDescription2(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		ApiOperation apiOperation = method
				.getAnnotation(ApiOperation.class);
		String discription = apiOperation.notes();
		return discription;
	}
	
	/**
	 * 获取session中的用户信息
	 * @param request
	 * @return
	 */
	private User getCurrentUser(HttpServletRequest request){
		HttpSession session = request.getSession();       
	    User user = (User) session.getAttribute(SESSION_USER_KEY);    
	    return user;
	}

	/**
	 * 构建请求参数
	 * <ul>
	 * <li>优先获取request中的请求参数,使用于get等方法</li>
	 * <li>request中无参数时,获取连接点参数,间接获取post类型请求体参数</li>
	 * </ul>
	 * @param paramMap request.getParameterMap(); //请求提交的参数
	 * @param args joinPoint.getArgs(); //连接点获取请求参数
	 * @return
	 */
	private String buildRequestParams(Map<String, String[]> paramMap, Object[] args) {
		StringBuilder params = new StringBuilder();
		// post 请求体json参数
		if (CollectionUtils.isEmpty(paramMap)) {
			for (Object obj : args) {
				if(! (obj instanceof Serializable)) {
					continue;
				}
				params.append(JacksonUtils.obj2jsonIgnoreNull(obj));
			}
		// get 请求参数
		} else {
			for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
				params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
				String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
				params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
			}
		}
		return params.toString();
	}

	/**
	 * 保存日志线程
	 * 
	 * @author lin.r.x
	 *
	 */
	private static class SaveLogThread implements Runnable {
		private Log log;
		private LogService logService;

		public SaveLogThread(Log log, LogService logService) {
			this.log = log;
			this.logService = logService;
		}

		@Override
		public void run() {
			logService.createLog(log);
		}
	}

	/**
	 * 日志更新线程
	 * 
	 * @author lin.r.x
	 *
	 */
	private static class UpdateLogThread extends Thread {
		private Log log;
		private LogService logService;

		public UpdateLogThread(Log log, LogService logService) {
			super(UpdateLogThread.class.getSimpleName());
			this.log = log;
			this.logService = logService;
		}

		@Override
		public void run() {
			this.logService.updateLog(log);
		}
	}


}
