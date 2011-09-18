package com.googlecode.positionalir.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class LogUtil {
	
	private static final Map<String, Long> TASKS = new LinkedHashMap<String, Long>();

	private static final String TASK_SEPARATOR = "-->";
	
	public static Logger getLogger(Class<?> clazz) {
		return Logger.getLogger(clazz.getCanonicalName());
	}
	
	public static String logTaskStart(String taskName) {
		TASKS.put(taskName, System.currentTimeMillis());
		return log("START.");
	}
	
	public static String log(String logMsg) {
		StringBuilder log = new StringBuilder();
		for (String task : TASKS.keySet()) {
			log.append(task).append(TASK_SEPARATOR);
		}
		if(!TASKS.isEmpty()) {
			log.delete(log.length() - TASK_SEPARATOR.length(), log.length());
		}
		if(!logMsg.isEmpty())
			log.append(": ").append(logMsg);
		return log.toString();
	}
	
	public static String logTaskEnd(String taskName) {
		final long endTime = System.currentTimeMillis();
		final Long startTime = TASKS.get(taskName);
		final String msg;
		if(startTime == null)
			msg = log("END.");
		else
			msg = log("END " + (endTime - startTime) + " millis.");
		TASKS.remove(taskName);
		return msg;
	}
	
}
