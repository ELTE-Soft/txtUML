package hu.elte.txtuml.export.cpp.templates;

/***********************************************************
 * Author: Hack János
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 **********************************************************/

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;

import hu.elte.txtuml.utils.Pair;

public class GenerationTemplates 
{		
	
	public static String EventHeader = GenerationNames.EventHeaderName + "."+GenerationNames.HeaderExtension;
	public static String StateMachineBaseHeader = GenerationNames.StatemachineBaseHeaderName +"."+GenerationNames.HeaderExtension;
	public static String RuntimeHeader=RuntimeTemplates.RuntimeHeaderName;
	public static String RuntimePath=RuntimeTemplates.RTPath;
	public static String StandardIOinclude=GenerationNames.StandardIOinclude;
	
	public static String EventBase()
	{
		String eventBase="";
		
		if(Options.Runtime())
		{
			eventBase+=RuntimeTemplates.RTEventHeaderInclude()+"\n";
		}
		
		eventBase+= GenerationNames.ClassType+" "+GenerationNames.EventBaseName;
		if(Options.Runtime())
		{
			eventBase+=":"+RuntimeTemplates.EventIName;
		}
		eventBase+="\n{\n"+GenerationNames.EventBaseName+"(";
		if(Options.Runtime())
		{
			eventBase+=RuntimeTemplates.SMParam+",";
		}
		eventBase+="int t_):";
		if(Options.Runtime())
		{
			eventBase+=RuntimeTemplates.EventIName+"("+GenerationNames.FormatIncomignParamName(RuntimeTemplates.SMRefName)+"),";
		}
		return eventBase+"t(t_){}\nint t;\n};\ntypedef const "+GenerationNames.EventBaseName+"& "+GenerationNames.EventBaseRefName+";\n\n";
	}
	
	public static String EventClass(String className_,List<Pair<String,String>> params_)
	{
		String source=GenerationNames.ClassType+" "+GenerationNames.EventClassName(className_)+":public "+GenerationNames.EventBaseName+"\n{\n"+GenerationNames.EventClassName(className_)+"(";
		if(Options.Runtime())
		{
			source+=RuntimeTemplates.SMParam+",";
		}
		source+="int t_";
		String paramList=PrivateFunctionalTemplates.ParamList(params_);
		if(paramList!="")
		{
			source +=","+paramList;
		}
		source+="):"+GenerationNames.EventBaseName+"(";
		if(Options.Runtime())
		{
			source+=GenerationNames.FormatIncomignParamName(RuntimeTemplates.SMRefName)+",";
		}
		source+="t_)";
		String body="{}\n";
		for(Pair<String, String> param:params_)
		{
			source+=","+param.getSecond()+"("+GenerationNames.FormatIncomignParamName(param.getSecond())+")";
			body+=PrivateFunctionalTemplates.CppType(param.getFirst())+" "+param.getSecond()+";\n";
		}
		return source+body+"};\n\n";
	}
	
	public static String EventEnum(Set<SignalEvent> events_)//TODO works only with signal events! (Time,Change,.. not handled)
	{
		if(events_== null || events_.isEmpty())
		{
			return "";
		}
		String EventList = "enum Events{";
		for(SignalEvent item: events_)
		{
			EventList+=GenerationNames.EventEnumName(item.getSignal().getName())+",";
		}
		return EventList.substring(0,EventList.length()-1)+"};\n";
	}
	
	
	
	public static String HeaderName(String className_)
	{
		return className_+"."+GenerationNames.HeaderExtension;
	}
	
	public static String SourceName(String className_)
	{
		return className_+"."+GenerationNames.SourceExtension;
	}
	
	public static String EventHeaderGuard(String source_)
	{
		return HeaderGuard(source_, GenerationNames.EventHeaderName);
	}
	
	public static String HeaderGuard(String source_,String className_)
	{
		return "#ifndef __"+className_.toUpperCase()+"_"+GenerationNames.HeaderExtension.toUpperCase()+"__\n"+
				"#define __"+className_.toUpperCase()+"_"+GenerationNames.HeaderExtension.toUpperCase()+"__\n\n"+
				source_+
				"\n\n#endif //__"+className_.toUpperCase()+"_"+GenerationNames.HeaderExtension.toUpperCase()+"_";
	}
	
	public static String SetState(String state_)
	{
		return GenerationNames.setStateFuncName+"("+GenerationNames.StateEnumName(state_)+");\n";
	}

	public static String HierarchicalSubStateMachineClassHeader(String dependency_,String className_,String parentclass_,List<String> subMachines_, String public_,String protected_,String private_)
	{
		List<String> parentParam=new LinkedList<String>();
		parentParam.add(parentclass_);
		
		return  HierarchicalStateMachineClassHeader(dependency_,className_,parentParam,subMachines_,public_,protected_,(PrivateFunctionalTemplates.SubStateMachineClassFixPrivateParts(parentclass_)+private_),false);
	}
	
	public static String HierarchicalStateMachineClassHeader(String dependency_,String className_,List<String> subMachines_, String public_,String protected_,String private_,Boolean rt_)
	{
		return HierarchicalStateMachineClassHeader(dependency_, className_, null, subMachines_, public_, protected_, private_, rt_);
	}
	
	public static String HierarchicalStateMachineClassHeader(String dependency_,String className_,List<String> constructorParams_,List<String> subMachines_, String public_,String protected_,String private_,Boolean rt_)
	{
		return ClassHeader(PrivateFunctionalTemplates.ClassHeaderIncludes(rt_)+dependency_,className_,constructorParams_,
				PrivateFunctionalTemplates.StateMachineClassFixPublicParts(className_,rt_)+public_,protected_,
				PrivateFunctionalTemplates.HierarchicalStateMachineClassFixPrivateParts(className_,subMachines_)+private_,true,rt_);
	}
	
	public static String SimpleSubStateMachineClassHeader(String dependency_,String className_,String parentclass_,String public_,String protected_,String private_)
	{
		List<String> parentParam=new LinkedList<String>();
		parentParam.add(parentclass_);
		
		return SimpleStateMachineClassHeader(dependency_, className_,parentParam,public_, protected_, (PrivateFunctionalTemplates.SubStateMachineClassFixPrivateParts(parentclass_)+private_),false);
	}
	
	public static String SimpleStateMachineClassHeader(String dependency_,String className_,String public_,String protected_,String private_,Boolean rt_)
	{
		return SimpleStateMachineClassHeader(dependency_, className_, null, public_, protected_, private_, rt_);
	}
	
	public static String SimpleStateMachineClassHeader(String dependency_,String className_,List<String> constructorParams_,String public_,String protected_,String private_,Boolean rt_)
	{
		return ClassHeader(PrivateFunctionalTemplates.ClassHeaderIncludes(rt_)+dependency_,className_,constructorParams_,
				PrivateFunctionalTemplates.StateMachineClassFixPublicParts(className_,rt_)+public_,protected_,
				PrivateFunctionalTemplates.SimpleStateMachineClassFixPrivateParts(className_)+private_,true,rt_);
	}
	
	public static String ClassHeader(String dependency_,String name_,String public_,String protected_,String private_)
	{
		return ClassHeader(dependency_,name_,null,public_,protected_,private_,false,false);
	}
	
	public static String ClassHeader(String dependency_,String className_,List<String> constructorParams_,String public_,String protected_,String private_,Boolean sm_,Boolean rt_)
	{
		String source=dependency_;
		if(!sm_)
		{
			source+=PrivateFunctionalTemplates.LocalInclude(GenerationNames.EventHeaderName)+"\n";
		}
		
		source+=GenerationNames.ClassType+" "+className_;
		if(sm_)
		{
			source+=":public "+GenerationNames.StatemachineBaseName;
			if(rt_)
			{
				source+=",public "+RuntimeTemplates.STMIName;
			}
		}
		
		source+="\n{\npublic:\n"+className_+"("+PrivateFunctionalTemplates.ParamTypeList(constructorParams_)+");\n";
		if(!sm_)
		{
			source+=GenerationNames.DummyProcessEventDef;
		}
		
		if(!public_.isEmpty())
		{
			source +=public_;
		}
		if(!protected_.isEmpty())
		{
			source+="\nprotected:\n"+protected_;
		}
		if(!private_.isEmpty())
		{
			source+="\nprivate:\n"+private_;
		}
				
		return source + "\n};\n\n";
	}
	
	public static String ParamName(String paramName_)
	{
		return GenerationNames.FormatIncomignParamName(paramName_);
	}
	
	public static String VariableDecl(String typeName_,String variableName_)
	{
		return PrivateFunctionalTemplates.CppType(typeName_)+" "+variableName_+";\n";
	}
	
	public static String ManyMultiplicityDependacy()
	{
		return PrivateFunctionalTemplates.OuterInclude(GenerationNames.AssocMultiplicityDataStruct);
	}
	
	public static String VariableDecl(String typeName_,String variableName_,Integer multiplicity_)
	{
		if(multiplicity_ > 1)
		{
			return GenerationNames.AssocMultiplicityDataStruct+"<"+PrivateFunctionalTemplates.CppType(typeName_)+">"+" "+variableName_+";\n";
		}
		return VariableDecl(typeName_, variableName_);
	}
	
	public static String ConstructorDef(String className_)//TODO too simple ....
	{
		return className_+"::"+className_+"()+{}\n\n";
	}

	public static String TransitionActionDecl(String transitionActionName_)
	{
		List<String> params=new LinkedList<String>();
		params.add(GenerationNames.EventBaseRefName);
		
		return FunctionDecl(transitionActionName_, params);
	}

	public static String TransitionActionDef(String className_,String transitionActionName_,String body_)
	{
		List<Pair<String,String>> params=new LinkedList<Pair<String,String>>();
		params.add(new Pair<String, String>(GenerationNames.EventBaseRefName, GenerationNames.EventParamName));
		
		return FunctionDef(className_,transitionActionName_, params, PrivateFunctionalTemplates.DebugLogMessage(className_, transitionActionName_)+body_);
	}

	public static String FunctionDecl(String functionName_)
	{
		return FunctionDecl(functionName_,null);
	}
	
	public static String FunctionDecl(String functionName_,List<String> params_)
	{
		return FunctionDecl(GenerationNames.NoReturn,functionName_, params_);
	}
	
	public static String FunctionDecl(String returnTypeName_,String functionName_,List<String> params_)//TODO modifiers
	{
		return PrivateFunctionalTemplates.CppType(returnTypeName_)+" "+functionName_+"("+PrivateFunctionalTemplates.ParamTypeList(params_)+");\n" ;
	}
	
	public static String FunctionDef(String className_,String functionName_,String body_)
	{
		return FunctionDef(className_,GenerationNames.NoReturn,functionName_, body_);
	}
	
	public static String FunctionDef(String className_,String returnTypeName_,String functionName_,String body_)
	{
		return FunctionDef(className_,returnTypeName_,functionName_,null, body_);
	}

	public static String FunctionDef(String className_,String functionName_,List<Pair<String,String>> params_,String body_)
	{
		return FunctionDef(className_,GenerationNames.NoReturn,functionName_, params_, body_);
	}

	public static String FunctionDef(String className_,String returnTypeName_,String functionName_,List<Pair<String,String>> params_,String body_)//TODO modifiers
	{
		return PrivateFunctionalTemplates.CppType(returnTypeName_)+" "+className_+"::"+functionName_+"("+PrivateFunctionalTemplates.ParamList(params_)+")\n{\n"+body_+"}\n\n";
	}

	public static String HierarchicalSubStateMachineClassConstructor(String className_,String parentClassName_,Map<Pair<String,String>,Pair<String,String>> machine_,Map<String,String> subMachines_,String intialState_)
	{
		String parentParamName=GenerationNames.FormatIncomignParamName(GenerationNames.ParentSmName);
		String source=className_+"::"+className_+"("+GenerationNames.PointerType(parentClassName_)+" "+parentParamName+"):"+GenerationNames.DefaultStateInitialization+","+GenerationNames.CurrentMachineName+"("+GenerationNames.NullPtr+"),"+
				GenerationNames.ParentSmMemberName+"("+parentParamName+")"+"\n{\n";
		return source+PrivateFunctionalTemplates.HierarchicalStateMachineClassConstructorSharedBody(className_, parentParamName, machine_, subMachines_, intialState_, false);
	}
	
	public static String HierarchicalStateMachineClassConstructor(String className_,Map<Pair<String,String>,Pair<String,String>> machine_,Map<String,String> subMachines_,String intialState_,Boolean rt_)
	{
		String source= className_+"::"+className_+"():"+GenerationNames.DefaultStateInitialization+","+GenerationNames.CurrentMachineName+"("+GenerationNames.NullPtr+")\n{\n";
		return source+PrivateFunctionalTemplates.HierarchicalStateMachineClassConstructorSharedBody(className_, "this", machine_, subMachines_, intialState_, rt_);
	}
	
	/*
	 * Map<Pair<String, String>,<String,String>
	 *                <event, state>,<guard,handlerName>
	 * */
	public static String SimpleSubStateMachineClassConstructor(String className_,String parentClass_,Map<Pair<String,String>,Pair<String,String>> machine_,String intialState_)
	{
		String parentParam=GenerationNames.FormatIncomignParamName(GenerationNames.ParentSmName);
		String source=className_+"::"+className_+"("+GenerationNames.PointerType(parentClass_)+" "+parentParam+"):"+GenerationNames.DefaultStateInitialization+","+
				GenerationNames.ParentSmMemberName+"("+parentParam+")"+"\n{\n"+PrivateFunctionalTemplates.StateMachineClassConstructorSharedBody(className_,parentClass_,machine_, intialState_)+"}\n\n";	
		return source+PrivateFunctionalTemplates.SimpleStateMachineClassConstructorSharedBody(className_, machine_, intialState_, false);
	}

	/*
	 * Map<Pair<String, String>,<String,String>
	 *                <event, state>,<guard,handlerName>
	 * */
	public static String SimpleStateMachineClassConstructor(String className_,Map<Pair<String,String>,Pair<String,String>> machine_,String intialState_,Boolean rt_)
	{
		String source=className_+"::"+className_+"():"+GenerationNames.DefaultStateInitialization+"\n{\n"+PrivateFunctionalTemplates.StateMachineClassConstructorSharedBody(className_, machine_, intialState_)+"}\n\n";
		return source+PrivateFunctionalTemplates.SimpleStateMachineClassConstructorSharedBody(className_, machine_, intialState_, rt_);
	}
	
	public static String GuardFunction(String guardFunctionName_,String constraint_,String eventName_)
	{
		String source="bool "+guardFunctionName_+"("+GenerationNames.EventBaseRefName;
		if(eventName_!=null && !eventName_.isEmpty() && constraint_.contains(eventName_))
		{
			source+=" "+GenerationNames.EventFParamName+")\n{"+GetRealEvent(eventName_);
			constraint_=EventParamUsage(eventName_,constraint_);
		}
		else
		{
			source+="){";
		}
		
		return source+"return "+constraint_+";}\n";
	}

	/*
	 * Map<String,String>
	 * 	   state ,actionName
	 * */
	public static String Entry(String className_,Map<String,String> states_)
	{
		return PrivateFunctionalTemplates.EntryExitTemplate(GenerationNames.EntryName,className_,states_);
	}
	
	/*
	 * Map<String,String>
	 *     state ,action
	 * */
	public static String Exit(String className_,Map<String,String> states_)
	{
		return PrivateFunctionalTemplates.EntryExitTemplate(GenerationNames.ExitName,className_,states_);
	}
	

	
	public static String StateEnum(Iterable<State> states_) 
	{
		String StateList = "enum States{";
		for(State item: states_)
		{
			StateList+=GenerationNames.StateEnumName(item.getName())+",";
		}
		return StateList.substring(0,StateList.length()-1)+"};\n";
	}
	
	public static String ForwardDeclaration(String className_)
	{
		String source="";
		String cppType=PrivateFunctionalTemplates.CppType(className_);
		if(PrivateFunctionalTemplates.StdType(cppType))
		{
			source=PrivateFunctionalTemplates.Include(cppType);
		}
		else
		{
			source=GenerationNames.ClassType+" "+className_+";\n";
		}
		return source;
	}
	
	public static String GetRealEvent(String eventName_) {
		return "const "+eventName_+GenerationNames.EventClassTypeId+"& "+
				GenerationNames.RealEventName+"=static_cast<const "+
				eventName_+GenerationNames.EventClassTypeId+"&>("+GenerationNames.EventFParamName+");\n";
	}
	
	public static String CppInclude(String className_)
	{
		return PrivateFunctionalTemplates.Include(className_);
	}
	
	public static String formatSubSmFunctions(String source_)
	{
		return source_.replaceAll(ActivityTemplates.Self,GenerationNames.ParentSmMemberName);
	}

	public static String EventParamUsage(String eventName_, String body_)
	{
		return body_.replaceAll((eventName_+"\\"+GenerationNames.SimpleAccess), (GenerationNames.RealEventName+GenerationNames.SimpleAccess));
	}
	
}
