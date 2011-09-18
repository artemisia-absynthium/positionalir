package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	@Override
	public String format(LogRecord record) {
		return format(new Date(record.getMillis())) + " " + record.getLevel().getName() + ": (" + record.getLoggerName() + ") " + record.getMessage() + "\n";
	}
	
	private synchronized String format(Date date) {
		return dateFormat.format(date);
	}
	
}
