package hello.service.impl;

import hello.bean.Log;
import hello.service.LogService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("logService")
public class LogServiceImpl  implements LogService{
	private static final Map<String, Log> Logs = new HashMap<>();

//	@Autowired
//	private LogDao logDao;
	
	@Override
	public int createLog(Log log) {
		//return this.logDao.insertSelective(log);
		System.out.println("模拟日志入库"+log);
		Logs.put(log.getLogId(), log);
		return 1;
	}
	
	@Override
	public int updateLog(Log log) {
		//return this.logDao.updateByPrimaryKeySelective(log);
		System.out.println("模拟日志更新"+log);
		Logs.put(log.getLogId(), log);
		return 1;
	}


	





}
