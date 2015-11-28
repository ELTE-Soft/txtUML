package hu.elte.txtuml.export.cpp.templates;

/***********************************************************
 * Author: Hack János
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 **********************************************************/

import java.util.List;
import java.util.Map;

import hu.elte.txtuml.utils.Pair;

class PrivateFunctionalTemplates
{
	
	/*
	 * Map<Pair<String, String>,<String,String>
	 *                <event, state>,<guard,handlerName>
	 *                
	 * Map<String,String>
	 * 	  <event,SubmachineName>
	 * */
	public static String HierarchicalStateMachineClassConstructorSharedBody(String className_,String parentClassName_,Map<Pair<String,String>,Pair<String,String>> machine_,Map<String,String> subMachines_,String intialState_,Boolean rt_)
	{
		String source="";
		for(Map.Entry<String,String> entry:subMachines_.entrySet())
		{
			source+=GenerationNames.CompositeStateMapName+".emplace("+GenerationNames.StateEnumName(entry.getKey())+","+GenerationNames.CompositeStateMapSmType+"("+GenerationNames.MemoryAllocator+" "+entry.getValue()+"("+parentClassName_+")"+"));\n";
		}
		
		source+="\n"+StateMachineClassConstructorSharedBody(className_,parentClassName_, machine_, intialState_)+"}\n\n";
		if(rt_)
		{
			source+=RuntimeTemplates.RTFunctionDecl(className_);
		}
		return source+GenerationNames.HierachicalProcessEventDef(className_)+"\n"+
				GenerationNames.ActionCallerDef(className_)+"\n"+
				GenerationNames.HierachicalSetStateDef(className_)+"\n"+
				PrivateFunctionalTemplates.SetInitialState(className_,intialState_)+"\n";
	}
	
	public static String SimpleStateMachineClassConstructorSharedBody(String className_,Map<Pair<String,String>,Pair<String,String>> machine_,String intialState_,Boolean rt_)
	{
		String source="";
		if(rt_)
		{
			source+=RuntimeTemplates.RTFunctionDecl(className_);
		}		
		return source+GenerationNames.SimpleProcessEventDef(className_)+"\n"+
				GenerationNames.SimpleSetStateDef(className_)+"\n"+
				PrivateFunctionalTemplates.SetInitialState(className_,intialState_)+"\n";
	}
	
	public static String StateMachineClassConstructorSharedBody(String className_,Map<Pair<String,String>,Pair<String,String>> machine_,String intialState_)
	{
		return StateMachineClassConstructorSharedBody(className_, null, machine_, intialState_);
	}

	public static String StateMachineClassConstructorSharedBody(String className_,String parentClassName_,Map<Pair<String,String>,Pair<String,String>> machine_,String intialState_)
	{
		String source="";
		for (Map.Entry<Pair<String,String>, Pair<String,String>> entry : machine_.entrySet())
		{
			source+=GenerationNames.TransitionTableName+".emplace("+GenerationNames.EventStateTypeName+"(";
			if(parentClassName_!=null && parentClassName_!="this")
			{
				source+=parentClassName_+"::";
			}
			source+=GenerationNames.EventEnumName(entry.getKey().getFirst())+","+GenerationNames.StateEnumName(entry.getKey().getSecond())+"),";
			String guardName=GenerationNames.DefaultGuardName;
			if(entry.getValue().getFirst() != null)
			{
				guardName=entry.getValue().getFirst();
			}		
			source+=GenerationNames.GuardActionName+"("+GenerationNames.GuardFuncTypeName+"(&"+className_+"::"+guardName+"),"
					+GenerationNames.FunctionPtrTypeName+"(&"+className_+"::"+entry.getValue().getSecond()+")));\n";
		
		}
		source+=GenerationNames.SetInitialStateName+"();\n";
		
		return source;
	}
	
	public static String ClassHeaderIncludes(Boolean rt_)
	{
		String source=Include(GenerationNames.StatemachineBaseHeaderName);
					  
		if(rt_)
		{
			source+="\n"+Include(RuntimeTemplates.RTPath+RuntimeTemplates.SMIHeaderName);
		}
		return source+"\n";
	}
	
	public static String Include(String className_)
	{
		if(className_.contains("std::"))
		{
			return OuterInclude(className_);
		}
		else
		{
			return LocalInclude(className_);
		}
	}
	
	public static String LocalInclude(String className_)
	{
		return "#include \""+className_+"."+GenerationNames.HeaderExtension+"\"\n";
	}
	
	public static String OuterInclude(String className_)
	{
		if(StdType(className_))
		{
			className_=className_.substring(5);
		}
		return "#include <"+className_+">\n";
	}
	
	public static String StateMachineClassFixPublicParts(String className_,Boolean rt_)
	{
		String source=GenerationNames.ProcessEventDecl+GenerationNames.SetInitialStateDecl+"\n";
		if(rt_)
		{
			source+="//RuntimeFunctions\n"+RuntimeTemplates.HeaderFuncs+"\n";
		}
		return source;
	}
	
	public static String SimpleStateMachineClassFixPrivateParts(String className_)
	{
		return GenerationNames.SetStateDecl+GenerationNames.NoReturn+" "+GenerationNames.EntryName+"();\n"+
				GenerationNames.NoReturn+" "+GenerationNames.ExitName+"();\n\n"+
				"int "+GenerationNames.CurrentStateName+";\n"
				+Typedefs(className_)+GenerationNames.TransitionTable;
	}
	
	public static String HierarchicalStateMachineClassFixPrivateParts(String className_, List<String> subMachines_)
	{
		return 	"//Hierarchical Machine Parts\n"+
				GenerationNames.ActionCallerDecl+
				GenerationNames.CurrentMachine+
				GenerationNames.CompositeStateMap+
				SubMachineFriendDecls(subMachines_)+
				"//Simple Machine Parts\n"+
				SimpleStateMachineClassFixPrivateParts(className_);
	}
	
	private static String SubMachineFriendDecls(List<String> subMachines_)
	{
		String source="";
		for(String subMachine:subMachines_)
		{
			source+=GenerationNames.FriendClassDecl(subMachine);
		}
		return source;
	}
	
	public static String Typedefs(String className_)
	{ 
		return "typedef std::function<"+GenerationNames.NoReturn+"("+className_+"&,"+GenerationNames.EventBaseRefName+")> "+GenerationNames.FunctionPtrTypeName+";\n"+
				"typedef std::function<bool("+className_+"&,"+GenerationNames.EventBaseRefName+")> "+GenerationNames.GuardFuncTypeName+";\n"+
				"typedef std::pair<"+GenerationNames.GuardFuncTypeName+","+GenerationNames.FunctionPtrTypeName+"> "+GenerationNames.GuardActionName+";\n";
	}
	
	
	public static String PointerBaseType(String typeName_)
	{
		return typeName_.substring(0,typeName_.indexOf("*"));
	}
	
	public static String ParamList(List<Pair<String, String>> params_)
	{
		if(params_ == null || params_.size() == 0)return "";
		String source="";
		for(Pair<String,String> item:params_)
		{
			source+=PrivateFunctionalTemplates.CppType(item.getFirst())+" "+GenerationNames.FormatIncomignParamName(item.getSecond())+",";
		}
		return source.substring(0,source.length()-1);
	}
	
	public static String ParamTypeList(List<String> params_)
	{
		if(params_ == null || params_.size() == 0) return "";
		String source="";
		for(String item:params_)
		{
			source+=CppType(item)+",";
		}
		return source.substring(0,source.length()-1);
	}
	
	public static String CppType(String typeName_)
	{
		String cppType=typeName_;
		if(typeName_!=GenerationNames.EventBaseRefName && typeName_!=GenerationNames.NoReturn)
		{
			if(typeName_!=null)
			{
				switch(typeName_)
				{
					case "Integer": cppType="int";break;
					case "Real": cppType="double";break;
					case "Boolean": cppType="bool";break;
					case "String": cppType=GenerationNames.cppString;break;
					default: cppType=GenerationNames.PointerType(typeName_);break;
				}
			}
			else
			{
				cppType="void";
			}
		}
		return cppType;
	}
	
	public static String EntryExitTemplate(String typeName_,String className_,Map<String,String> states_)
	{
		String source=GenerationNames.NoReturn+" "+className_+"::"+typeName_+"()\n{\n";
		if(states_!=null && !states_.isEmpty())
		{
			source+="switch("+GenerationNames.CurrentStateName+")\n{\n";
			for (Map.Entry<String,String> entry : states_.entrySet())
			{
				source+="case("+GenerationNames.StateEnumName(entry.getKey())+"):{"+entry.getValue()+"();break;}\n";
			}
			source+="}\n";
		}
		return source+"}\n";
	}
	
	public static String SetInitialState(String className_,String initialState_)
	{
		return GenerationNames.NoReturn+" "+className_+"::"+GenerationNames.SetInitialStateName+"(){"+GenerationNames.setStateFuncName+"("+GenerationNames.StateEnumName(initialState_)+");}\n";
	}
	
	public static String SubStateMachineClassFixPrivateParts(String parentclass_) 
	{
		return GenerationNames.PointerType(parentclass_)+" "+GenerationNames.ParentSmMemberName+";\n";
	}

	public static boolean StdType(String cppType_) 
	{
		if(cppType_.contains("std::"))
		{
			return true;
		}
		return false;
	}
	
	public static String DebugLogMessage(String className_,String functionName_)
	{
		return "#ifdef "+GenerationNames.DebugSimbol+"\n"+
			   "\tstd::cout<<\""+className_+"::"+functionName_+"\"<<std::endl;\n"+
			   "#endif\n";
	}
	
}