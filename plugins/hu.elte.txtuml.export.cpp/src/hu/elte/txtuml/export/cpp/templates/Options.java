package hu.elte.txtuml.export.cpp.templates;

public class Options 
{
	private static boolean debugLog = false;
	private static boolean runtime = false;
	private static boolean threadManagement = false;
	
	
	
	public static void setDebugLog(){
		setDebugLog(true);
	}
	
	public static  void setDebugLog(boolean value_){
		debugLog=value_;
	}
	
	public static  boolean DebugLog(){
		return debugLog;
	}
	
	public static void setRuntime(){
		setRuntime(true);
	}
	
	public static void setRuntime(boolean value_){
		runtime = value_;
	}

	public static boolean Runtime(){
		return runtime;
	}
	
	public static void setThreadManagement(boolean value){
		threadManagement = value;
	}
	
	public static void setThreadManagement(){
		setThreadManagement(true);
	}
	
	public static boolean ThreadManagement(){
		return threadManagement;
	}
	
	
}
