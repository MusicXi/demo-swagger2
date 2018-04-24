package hello.service;

import hello.bean.Log;

import java.util.List;

public interface LogService {
	//增删改
	int createLog(Log log);
	int updateLog(Log log);
	List<Log> listLogs();
	

	
}
