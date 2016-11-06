package hu.elte.txtuml.export.cpp.templates;

import java.io.File;

public class RuntimeTemplates {
	public static final String RTPath = "runtime" + File.separator;
	public static final String RuntimeHeaderName = RTPath + "runtime";
	public static final String EventIName = "IEvent";
	public static final String EventIHeaderName = "ievent";
	public static final String STMIName = "StateMachineI";
	public static final String SMIHeaderName = "statemachineI";
	public static final String SMRefName = "dest";
	public static final String SMParam = STMIName + "& " + GenerationNames.formatIncomingParamName(SMRefName);
	public static final String HeaderFuncs = "virtual void processEventVirtual();\nvirtual void processInitTranstion();\n";
	public static final String RuntimeIterfaceName = "RuntimeI";
	public static final String RuntimeSetter = "setRuntime";
	public static final String UsingRuntime = "Runtime";
	public static final String RuntimeParameterName = "rt";
	public static final String RuntimeIntanceMethod = "createRuntime";
	public static final String GetRuntimeInstance = UsingRuntime + "::" + RuntimeIntanceMethod + "()";
	public static final String ObjectSetterForRuntime = "setupObject";
	public static final String ObjectRemoverForRuntime = "removeObject";



	public static String initStateMachineForRuntime() {
		return GetRuntimeInstance + GenerationNames.PointerAccess + ObjectSetterForRuntime + "(" + GenerationNames.Self
				+ ");\n";
	}

	public static String processEventVirtual(String className) {
		return GenerationNames.NoReturn + " " + className + "::processEventVirtual()\n{\n" + EventIName
				+ "* base=getNextMessage().get();\n" + GenerationNames.EventBaseName + "* "
				+ GenerationNames.RealEventName + "=static_cast<" + GenerationNames.EventBaseName + "*>(base);\n"
				+ GenerationNames.ProcessEventFName + "(*" + GenerationNames.RealEventName
				+ ");\ndeleteNextMessage();\n}\n";
	}

	public static String processInitTransition(String className) {
		return GenerationNames.NoReturn + " " + className + "::processInitTranstion()\n{\n"
				+ GenerationNames.InitialEventName + "_EC init;\n"
				+ GenerationNames.ProcessEventFName + "(init);\n}\n";

	}

	public static String rtFunctionDef(String className) {
		return processEventVirtual(className) + "\n" + processInitTransition(className) + "\n";
	}

	public static String eventHeaderInclude() {
		return PrivateFunctionalTemplates.include(RTPath + EventIHeaderName);
	}

}
