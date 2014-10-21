package txtuml.export.uml2tocpp;

import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;

public class GenerationTemplates {
	
	public static String EventHeaderName="event";
	public static String EventBaseName="EventBase";
	public static String EventBaseRefName=EventBaseName+"CRef";
	public static String unknown="?";
	public static String cppString="std::string";

	
	public static String EHI = "#include\""+EventHeaderName+"\"";
	public static String ClassHeaderIncludes ="#include <functional>\n#include <vector>\n#include \""+EventHeaderName+".hh\"\n\n";
	public static String EventBase = "struct "+EventBaseName+"\n{\n"+EventBaseName+"(int t_):t(t_){}\nint t;\n};\ntypedef const "+EventBaseName+"& "+EventBaseRefName+";\n\n";
	
	private static String EntryName="Entry";
	private static String ExitName="Exit";
	private static String TransitionMatrixName="_mM";
	private static String setStateFuncName="setState";
	private static String CurrentStateName="_cS";
	private static String SetState = "void "+setStateFuncName+"(int s_){"+ExitName+"();"+CurrentStateName+"=s_;"+EntryName+"();}";
	private static String ProcessEvent ="void process_event("+EventBaseRefName+" e_){"+TransitionMatrixName+"[e_.t]["+CurrentStateName+"](*this,e_);}\n";
	private static String TransitionMatrix = "std::vector<std::vector< FuncPtrType > > "+TransitionMatrixName+";\n";
	
	
	public static String HeaderGuard(String source_,String name_)
	{
		return "#ifndef __"+name_.toUpperCase()+"_HPP__\n"+
				"#define __"+name_.toUpperCase()+"_HPP__\n\n"+
				source_+
				"\n\n#endif //__"+name_.toUpperCase()+"_HPP_";
	}
	
	public static String setState(String state_)
	{
		return setStateFuncName+"("+state_+");";
	}
	
	public static String EventEnum(Iterable<SignalEvent> events_)
	{
		String EventList = "enum Events{";
		for(SignalEvent item: events_)
		{
			EventList+=item.getSignal().getName()+",";
		}
		return EventList.substring(0,EventList.length()-1)+"};\n";
	}
	
	public static String EventClass(String name_,List<Util.Pair<String,String>> params_)//kell még params
	{
		return "struct "+name_+ ":public EventBase\n{\n"+name_+"(int t_):EventBase(t_){}"+"\n};\n\n";
	}
	
	
	public static String StateMachineClassHeader(String dependency_,String name_,String public_,String private_)
	{
		return ClassHeader(ClassHeaderIncludes+dependency_,name_,
				StateMachineClassFixPublicParts(name_)+public_,
				StateMachineClassFixPrivateParts(name_)+private_);
	}
	
	public static String ClassHeader(String dependency_,String name_,String public_,String private_)
	{
		String source=dependency_+"struct "+name_+"{\npublic:\n"+name_+"();\n";
		if(!public_.isEmpty())
		{
			source +=public_;
		}
		if(!private_.isEmpty())
		{
			source+="\nprivate:\n"+private_;
		}
				
		return source + "\n};\n\n";
	}
	
	//kell sima header generálás is
	
	private static String StateMachineClassFixPublicParts(String name_)
	{
		return ProcessEvent;
	}
	
	private static String StateMachineClassFixPrivateParts(String name_)
	{
		return SetState+"\nvoid "+EntryName+"();\nvoid "+ExitName+"();\n\nint "+CurrentStateName+";\n"+FuncPtr(name_)+TransitionMatrix;
	}
	
	private static String FuncPtr(String name_)
	{ 
		return "typedef std::function<void("+name_+"&,"+EventBaseRefName+")> FuncPtrType;\n";
	}
	
	public static String TransitionActionDecl(String name_)
	{
		return "void "+name_+"("+EventBaseRefName+");\n";
	}
	
	public static String TransitionActionDef(String machineName_,String name_,String body_)
	{
		return "void "+machineName_+"::"+name_+"("+EventBaseRefName+" e_)\n{\n"+
				body_+
				"}\n\n";
	}
	
	public static String Property(String type_,String name_)
	{
		return type_+" "+name_+";\n";
	}
	
	public static String FunctionDef(String className_,String return_,String name_,List<Util.Pair<String,String>> params_,String body_)//kell még a modifiers ....
	{
		return return_+" "+className_+"::"+name_+"("+generateParamList(params_)+")\n{\n"+body_+"}\n\n";
	}
	
	public static String FunctionDecl(String return_,String name_,List<String> params_)//kell még a modifiers ....
	{
		return return_+" "+name_+"("+generateParamTypeList(params_)+");\n" ;
	}
	
	private static String generateParamList(List<Util.Pair<String, String>> params_)
	{
		if(params_ == null || params_.size() == 0)return "";
		String source="";
		for(Util.Pair<String,String> item:params_)
		{
			source+=item.getKey()+" "+item.getValue()+",";
		}
		return source.substring(0,source.length()-1);
	}
	
	private static String generateParamTypeList(List<String> params_)
	{
		if(params_ == null || params_.size() == 0) return "";
		String source="";
		for(String item:params_)
		{
			source+=item+",";
		}
		return source.substring(0,source.length()-1);
	}
	
	/*
	 * Map<Util.Pair<String,String>,String>
	 * event,state,handlerName
	 * */
	public static String StateMachineClassConstructor(String name_,Map<Util.Pair<String,String>,String> machine_,int eventNum_,int stateNum_)
	{
		String source= name_+"::"+name_+"():"+TransitionMatrixName+"("+eventNum_+")\n{\n"+
				"for(int i=0;i<"+eventNum_+";++i)"+TransitionMatrixName+".resize("+stateNum_+");\n\n";
		for (Map.Entry<Util.Pair<String,String>, String> entry : machine_.entrySet())
		{
			source+=TransitionMatrixName+"["+entry.getKey().getKey()+"]["+entry.getKey().getValue()+"]=&"+name_+"::"+entry.getValue()+";\n";
		}
				
		return source+"}\n\n";
	}
	
	/*
	 * Map<String,String>
	 * state, action
	 * */
	public static String Entry(String machineName_,Map<String,String> states_)
	{
		return EntryExitTemplate(EntryName,machineName_,states_);
	}
	
	/*
	 * Map<String,String>
	 * state, action
	 * */
	public static String Exit(String machineName_,Map<String,String> states_)
	{
		return EntryExitTemplate(ExitName,machineName_,states_);
	}
	
	private static String EntryExitTemplate(String type_,String machineName_,Map<String,String> states_)
	{
		String source="void "+machineName_+"::"+type_+"()\n{\n";
		if(states_!=null)
		{
			source+="switch("+CurrentStateName+")\n{\n";
			for (Map.Entry<String,String> entry : states_.entrySet())
			{
				source+="case("+entry.getKey()+"):\n{\n"+entry.getValue()+"}\n";
			}
		}
		return source+"}\n\n";
	}
	
	public static String StateEnum(Iterable<State> states_)//az öszetett állaptok miatt majd más lesz
	{
		String StateList = "enum States{";
		for(State item: states_)
		{
			StateList+=item.getName()+",";
		}
		return StateList.substring(0,StateList.length()-1)+"};\n";
	}
	
	public static String CppType(String type_)
	{
		String cppType=unknown;
		switch(type_)
		{
			case "Integer": cppType="int";break;
			case "Real": cppType="double";break;
			case "Boolean": cppType="bool";break;
			case "String": cppType=cppString;break;
		}
		return cppType;
	}
	
	public static String LocalInclude(String className_)
	{
		return "#include \""+className_+".hh\"\n";
	}
	
	public static String OuterInclude(String name_)
	{
		if(name_.contains("std::"))
		{
			name_=name_.substring(5);
		}
		return "#include <"+name_+">\n";
	}
	
	public class ActionLanguage
	{
		//ide jönnek az akciók template-jei
	}
}
