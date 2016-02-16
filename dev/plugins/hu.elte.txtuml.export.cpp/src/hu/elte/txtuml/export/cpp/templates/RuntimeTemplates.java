package hu.elte.txtuml.export.cpp.templates;

import java.io.File;
import java.util.ArrayList;

class RuntimeTemplates {
	public static final String RTPath = "runtime" + File.separator;
	public static final String RuntimeHeaderName = RTPath + "runtime";
	public static final String EventIName = "EventI";
	public static final String EventIHeaderName = "eventI";
	public static final String STMIName = "StateMachineI";
	public static final String SMIHeaderName = "statemachineI";
	public static final String SMRefName = "dest";
	public static final String SMParam = STMIName + "& " + GenerationNames.formatIncomingParamName(SMRefName);
	public static final String HeaderFuncs = "virtual void processEventVirtual();\nvirtual void processInitTranstion();\n";
	public static final String RuntimeIterfaceName = "RuntimeI";
	public static final String RuntimeSetter = "setRuntime";
	public static final String RuntimeVarName = "_runtime";
	public static final String RuntimeParamter = "rt";

	public static String createObject(String objName) {
		return "_runtime->setupObject(" + objName + ");\n" + "_runtime->startObject(" + objName + ");\n";
	}

	public static String initStateMachineForRuntime() {
		return "_runtime->setupObject(" + GenerationNames.Self + ");\n";
		// "_runtime->startObject(this);\n\n";
	}

	public static String processEventVirtual(String className) {
		return GenerationNames.NoReturn + " " + className + "::processEventVirtual()\n{\n" + EventIName
				+ "* base=getNextMessage().get();\n" + GenerationNames.EventBaseName + "* "
				+ GenerationNames.RealEventName + "=static_cast<" + GenerationNames.EventBaseName + "*>(base);\n"
				+ GenerationNames.ProcessEventFName + "(*" + GenerationNames.RealEventName
				+ ");\ndeleteNextMessage();\n}\n";
	}

	public static String sendSignal(String signalName) {
		return "send(EventPtr(" + GenerationNames.MemoryAllocator + " " + signalName + ")";
	}

	public static String processInitTransition(String className) {
		return GenerationNames.NoReturn + " " + className + "::processInitTranstion()\n{\n"
				+ GenerationNames.ProcessEventFName + "(" + GenerationNames.InitialEventName + "_EC((*"
				+ GenerationNames.Self + "), " + className + "::" + GenerationNames.InitialEventName + "_EE));\n}\n";

	}

	public static String startSM(String className, Options options) {
		return GenerationNames.NoReturn + " " + className + "::startSM()\n{\n"
				+ ActivityTemplates.signalSend(GenerationNames.InitialEventName, GenerationNames.Self, className,
						GenerationNames.PointerAccess, new ArrayList<String>(), options.isAddRuntime())
				+ "\n}\n";
	}

	public static String rtFunctionDecl(String className) {
		return processEventVirtual(className) + "\n" + processInitTransition(className) + "\n";
	}

	public static String rtEventHeaderInclude() {
		return PrivateFunctionalTemplates.include(RTPath + EventIHeaderName);
	}
}
