package test;

public class ErrorTracker {
	 /**
	   * 获取调用的类名
	 * 
	 * @return String
	 */
	public static String getClassName() {
	    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	    StackTraceElement e = stacktrace[2];
	    String className = e.getClassName();
	    return className;
	}
	
	/**
	 * 获取调用的方法名
	 * 
	 * @return String
	 */
	public static String getMethodName() {
	    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	    StackTraceElement e = stacktrace[2];
	    String methodName = e.getMethodName();
	    return methodName;
	}
	/**
	 * 获取当前文件名
	 * @return String
	 */
	public static String getFileName() {
	    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	    StackTraceElement e = stacktrace[2];
	    String methodName = e.getFileName();
	    return methodName;
	}
	/**
	 * 获取当前的行数
	 * @return int
	 */
	public static int getLineNumber() {
	    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	    StackTraceElement e = stacktrace[2];
	    int line = e.getLineNumber();
	    return line;
	}
	
	public static String getTestMessage() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	    StackTraceElement e = stacktrace[2];
	    String a = "当前文件名为：" + e.getFileName() +
	    			"\n当前方法名为：" + e.getMethodName() +
	    			"\n当前行数为：" + e.getLineNumber() + "\n";
		return a;
	}
    
}
