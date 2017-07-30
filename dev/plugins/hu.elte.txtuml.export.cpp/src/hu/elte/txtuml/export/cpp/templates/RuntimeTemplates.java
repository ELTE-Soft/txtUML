package hu.elte.txtuml.export.cpp.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Path;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.PointerAndMemoryNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.StateMachineMethodNames;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.StateMachineTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.utils.Pair;

public class RuntimeTemplates {
	public static final String RTPath = "runtime" + Path.SEPARATOR;
	public static final String RuntimeHeaderName = RTPath + "runtime";
	public static final String EventIName = "IEvent";
	public static final String EventIHeaderName = "ievent";
	public static final String STMIName = "IStateMachine";
	public static final String SMIHeaderName = "istatemachine";
	public static final String SMRefName = "dest";
	public static final String SMParam = STMIName + "& " + GenerationNames.formatIncomingParamName(SMRefName);
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
	
	public static String processEventVirtualDecl() {	
		return "virtual" + " " + FunctionTemplates.functionDecl(EventTemplates.ProcessEventFunctionName, 
				Arrays.asList(GenerationNames.PointerAndMemoryNames.EventPtr));
	}

	public static String processEventVirtual(String className) {
		String eventName = "event";
		List<Pair<String,String>> params = new ArrayList<>();
		params.add(new Pair<String,String>(GenerationNames.PointerAndMemoryNames.EventPtr, eventName));
		return FunctionTemplates.functionDef(className, 
				EventTemplates.ProcessEventFunctionName, 
				params, StateMachineMethodNames.ProcessEventFName + "(" + GenerationNames.formatIncomingParamName(eventName) + ");");
	}
	
	public static String processInitTransitionDecl() {
		return "virtual" + " " + FunctionTemplates.functionDecl(StateMachineTemplates.ProcessInitTransitionFunctionName, 
				Arrays.asList(GenerationNames.PointerAndMemoryNames.EventPtr)); 
	}

	public static String processInitTransition(String className) {
		String eventName = "event";
		List<Pair<String,String>> params = new ArrayList<>();
		params.add(new Pair<String,String>(GenerationNames.PointerAndMemoryNames.EventPtr, eventName));
		return FunctionTemplates.functionDef(className, 
				StateMachineTemplates.ProcessInitTransitionFunctionName, 
				params, StateMachineMethodNames.InitTansitionFunctionName + "(" + GenerationNames.formatIncomingParamName(eventName) + ");");

	}
	


	public static String rtFunctionDef(String className) {
		return processEventVirtual(className) + "\n" + processInitTransition(className) + "\n";
	}

	public static String eventHeaderInclude() {
		return PrivateFunctionalTemplates.include(RTPath + EventIHeaderName);
	}

}
