package hu.elte.txtuml.export.cpp.templates;

public class Options 
{
	private static boolean _debugLog=false;
	private static boolean _runtime=false;
	
	public static void setDebugLog()
	{
		setDebugLog(true);
	}
	
	public static void setDebugLog(boolean value_)
	{
		_debugLog=value_;
	}
	
	public static boolean DebugLog()
	{
		return _debugLog;
	}
	
	public static void setRuntime()
	{
		setRuntime(true);
	}
	
	public static void setRuntime(boolean value_)
	{
		_runtime=value_;
	}

	public static boolean Runtime()
	{
		return _runtime;
	}
}
