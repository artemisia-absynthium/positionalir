package util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class LogUtil {
	
	private static final Map<String, Long> tasks = new LinkedHashMap<String, Long>();

	public static Logger getLogger(Class<?> clazz) {
		return Logger.getLogger(clazz.getCanonicalName());
	}
	
	public static String logTaskStart(String taskName) {
		tasks.put(taskName, System.currentTimeMillis());
		return log("START.");
	}

	public static String log(String logMsg) {
		final String string = "-->";
		StringBuilder log = new StringBuilder();
		for (String task : tasks.keySet()) {
			log.append(task).append(string);
		}
		if(!tasks.isEmpty()) {
			log.delete(log.length() - string.length(), log.length());
		}
		if(!logMsg.isEmpty())
			log.append(": ").append(logMsg);
		return log.toString();
	}
	
	public static String logTaskEnd(String taskName) {
		final long endTime = System.currentTimeMillis();
		final Long startTime = tasks.get(taskName);
		final String msg;
		if(startTime == null)
			msg = log("END.");
		else
			msg = log("END " + (endTime - startTime) + " millis.");
		tasks.remove(taskName);
		return msg;
	}
	
}
