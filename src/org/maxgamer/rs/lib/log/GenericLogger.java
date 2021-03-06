package org.maxgamer.rs.lib.log;

import java.io.PrintStream;

/**
 * @author netherfoam
 */
public class GenericLogger implements Logger {
	private LogLevel level;
	private PrintStream ps;
	
	public GenericLogger(LogLevel level, PrintStream ps) {
		this.level = level;
		this.ps = ps;
	}
	
	public void log(String s) {
		ps.println(s);
	}
	
	@Override
	public void debug(String s) {
		if (level.getLevel() > LogLevel.DEBUG.getLevel()) return;
		StackTraceElement e = getCaller();
		log("[DEBUG] " + e.getClassName().substring(e.getClassName().lastIndexOf('.') + 1) + "." + e.getMethodName() + "() " + s);
	}
	
	@Override
	public void info(String s) {
		if (level.getLevel() > LogLevel.INFO.getLevel()) return;
		StackTraceElement e = getCaller();
		log("[INFO] " + e.getClassName().substring(e.getClassName().lastIndexOf('.') + 1) + "." + e.getMethodName() + "() " + s);
	}
	
	@Override
	public void warning(String s) {
		if (level.getLevel() > LogLevel.WARNING.getLevel()) return;
		StackTraceElement e = getCaller();
		log("[WARN] " + e.getClassName().substring(e.getClassName().lastIndexOf('.') + 1) + "." + e.getMethodName() + "() " + s);
	}
	
	@Override
	public void severe(String s) {
		if (level.getLevel() > LogLevel.SEVERE.getLevel()) return;
		StackTraceElement e = getCaller();
		log("[SEVERE] " + e.getClassName().substring(e.getClassName().lastIndexOf('.') + 1) + "." + e.getMethodName() + "() " + s);
	}
	
	@Override
	public LogLevel getLevel() {
		return Logger.LogLevel.DEBUG;
	}
	
	private static StackTraceElement getCaller() {
		StackTraceElement e = Thread.currentThread().getStackTrace()[4];
		return e;
	}
	
	@Override
	public void log(LogLevel level, String s) {
		switch (level) {
			case DEBUG:
				debug(s);
				break;
			
			case INFO:
				info(s);
				break;
			
			case SEVERE:
				severe(s);
				break;
			
			case WARNING:
				warning(s);
				break;
		}
	}
}