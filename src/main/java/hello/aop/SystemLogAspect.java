package hello.aop;

import hello.bean.Log;
import hello.bean.User;
import hello.service.LogService;
import hello.util.DateUtils;
import hello.util.UuidUtils;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
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
	private static final ThreadLocal<Date> beginTimeThreadLocal =
			new NamedThreadLocal<Date>("ThreadLocal beginTime");
	private static final ThreadLocal<Log> logThreadLocal = 
			new NamedThreadLocal<Log>("ThreadLocal log");
	
	private static final ThreadLocal<User> currentUser=new NamedThreadLocal<>("ThreadLocal user");
	
	@Autowired(required=false)
	private HttpServletRequest request;
	
	//@Autowired
	//private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private LogService logService;

	/**
	 * Service层切点 
	 */
/*	@Pointcut("@annotation(com.myron.ims.annotation.SystemServiceLog)")
	public void serviceAspect(){}*/
	
	/**
	 * Controller层切点 注解拦截
	 */
	@Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
	public void controllerAspect(){}
	
	/**
	 * 方法规则拦截
	 */
	@Pointcut("execution(* com.myron.ims.controller.*.*(..))")
	public void controllerPointerCut(){}
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
        Object [] args = joinPoint.getArgs();
        System.out.println(args);
        
    	String title="";
    	String type="info";						  //日志类型(info:入库,error:错误)
    	String remoteAddr=request.getRemoteAddr();//请求的IP
    	String requestUri=request.getRequestURI();//请求的Uri
    	String method=request.getMethod();		  //请求的方法类型(post/get)
    	Map<String,String[]> params=request.getParameterMap(); //请求提交的参数
    	try {
    		title=getControllerMethodDescription2(joinPoint);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}    
    	
    	//得到线程绑定的局部变量--获取前置通知设置的开始时间
    	long beginTime = beginTimeThreadLocal.get().getTime();  
    	long endTime = System.currentTimeMillis(); 
    	
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
    	
    	Log log=new Log();
    	log.setLogId(UuidUtils.creatUUID());
    	log.setTitle(title);
    	log.setType(type);
    	log.setRemoteAddr(remoteAddr);
    	log.setRequestUri(requestUri);
    	log.setMethod(method);
    	log.setMapToParams(params);
    	log.setUserId(user.getId());
    	Date operateDate=beginTimeThreadLocal.get();
    	log.setOperateDate(operateDate);
    	log.setTimeout(DateUtils.formatDateTime(endTime - beginTime));

    	 //1.直接执行保存操作
        //this.logService.createSystemLog(log);

        //2.优化:异步保存日志
        //new SaveLogThread(log, logService).start();

        //3.再优化:通过线程池来执行日志保存
        //threadPoolTaskExecutor.execute(new SaveLogThread(log, logService));
    	
    	// 演示直接打印
    	System.out.println(log);
    	
    	
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
			log.setType("error");
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
