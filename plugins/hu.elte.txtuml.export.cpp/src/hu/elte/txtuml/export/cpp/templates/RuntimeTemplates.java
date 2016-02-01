package hu.elte.txtuml.export.cpp.templates;

import java.io.File;

class RuntimeTemplates
{
	public static final String RTPath="runtime"+File.separator;
	public static final String RuntimeHeaderName=RTPath+"runtime";
	public static final String EventIName="EventI";
	public static final String EventIHeaderName="eventI";
	public static final String STMIName="StateMachineI";
	public static final String SMIHeaderName="statemachineI";
	public static final String SMRefName="dest";
	public static final String SMParam=STMIName+"& "+GenerationNames.FormatIncomignParamName(SMRefName);
	public static final String HeaderFuncs="virtual void processEventVirtual();\nvirtual void startSM();\n";
			
	
	public static String CreateObject(String objName_)
	{
		return "_runtime->setupObject("+objName_+");\n"+
				"_runtime->startObject("+objName_+");\n";
	}
	
	public static String ProcessEventVirtual(String className_)
	{
		return GenerationNames.NoReturn+" "+className_+"::processEventVirtual()\n{\n"+EventIName+"* base=getNextMessage().get();\n"+
			GenerationNames.EventBaseName+"* "+GenerationNames.RealEventName+"=static_cast<"+GenerationNames.EventBaseName+"*>(base);\n"+
			GenerationNames.ProcessEventFName+"(*"+GenerationNames.RealEventName+");\ndeleteNextMessage();\n}\n";
	}
	
	public static String SendSignal(String signalName_)
	{
		return "send(EventPtr("+GenerationNames.MemoryAllocator+" "+signalName_+")";
	}
	
	public static String StartSM(String className_)
	{
		return GenerationNames.NoReturn+" "+className_+"::startSM()\n{\n"+GenerationNames.SetInitialStateName+"();\n}\n";
	}
	
	public static String RTFunctionDecl(String className_)
	{
		return ProcessEventVirtual(className_)+"\n"+
			   StartSM(className_)+"\n";
	}
	
	public static String RTEventHeaderInclude()
	{
		return PrivateFunctionalTemplates.Include(RTPath+EventIHeaderName);
	}
}