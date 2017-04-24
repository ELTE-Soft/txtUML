package hu.elte.txtuml.export.cpp.templates;

import org.eclipse.core.runtime.Path;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;

public class RuntimeTemplates {
	public static final String RTPath = "runtime" + Path.SEPARATOR;
	public static final String RuntimeHeaderName = RTPath + "runtime";
	public static final String EventIName = "IEvent";
	public static final String EventIHeaderName = "ievent";
	public static final String STMIName = "IStateMachine";
	public static final String SMIHeaderName = "istatemachine";
	public static final String SMRefName = "dest";
	public static final String SMParam = STMIName + "& " + GenerationNames.formatIncomingParamName(SMRefName);
	public static final String HeaderFuncs = "virtual void " + EventTemplates.ProcessEventFunctionName + "();\nvirtual void " + StateMachineTemplates.ProcessInitTransitionFunctionName + "();\n";
	public static final String RuntimeInterfaceName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "IRuntime";
	public static final String RuntimePtrType = "ES::RuntimePtr";
	public static final String RuntimeSetter = "setRuntime";
	public static final String UsingRuntimeType = "UsedRuntimeType";
	public static final String UsingRuntimePtr = "UsedRuntimePtr";
	public static final String RuntimeParameterName = "rt";
	public static final String RuntimeInsanceGetter = "getRuntimeInstance";
	public static final String GetRuntimeInstance = UsingRuntimeType + "::" + RuntimeInsanceGetter + "()";
	public static final String ObjectSetterForRuntime = "setupObject";
	public static final String ObjectRemoverForRuntime = "removeObject";



	public static String initStateMachineForRuntime() {
		return GetRuntimeInstance + PointerAndMemoryNames.PointerAccess + ObjectSetterForRuntime + "(" + PointerAndMemoryNames.Self
				+ ");\n";
	}

	public static String processEventVirtual(String className) {
		return ModifierNames.NoReturn + " " + className + "::" + 
				EventTemplates.ProcessEventFunctionName + "()\n{\n" + 
				GenerationNames.PointerAndMemoryNames.EventPtr
				+ " event = getNextMessage();\n" + GenerationNames.ProcessEventFName + "(event);\n}\n";
	}

	public static String processInitTransition(String className) {
		String initialVar = "init";
		return ModifierNames.NoReturn + " " + className + "::" + StateMachineTemplates.ProcessInitTransitionFunctionName + "()\n{\n"
				+ GenerationNames.PointerAndMemoryNames.EventPtr +  " " +  initialVar + "(" +GenerationNames.PointerAndMemoryNames.MemoryAllocator + " " + GenerationNames.InitialEventName + "_EC());\n"
				+ GenerationNames.ProcessEventFName + "(" + initialVar + ");\n}\n";

	}

	public static String rtFunctionDef(String className) {
		return processEventVirtual(className) + "\n" + processInitTransition(className) + "\n";
	}

	public static String eventHeaderInclude() {
		return PrivateFunctionalTemplates.include(RTPath + EventIHeaderName);
	}

}
