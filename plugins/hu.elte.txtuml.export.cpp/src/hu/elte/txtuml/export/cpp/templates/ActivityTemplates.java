package hu.elte.txtuml.export.cpp.templates;

/***********************************************************
 * Author: Hack János
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 **********************************************************/

import java.util.LinkedList;
import java.util.List;

import hu.elte.txtuml.utils.Pair;

public class ActivityTemplates
{
	public static final String AddSimpleTypeOp="+=";
	public static final String ReplaceSimpleTypeOp="=";
	public static final String AddCompositTypeOp=".push_back";
	public static final String ReplaceCompositTypeOp=ReplaceSimpleTypeOp;
	public static final String Self="this";
	public static final String AccessOperatorForSets=GenerationNames.SimpleAccess;
		
	public static String GeneralSetValue(String leftValueName_,String rightValueName_,String operator_)
	{
		if(operator_ == AddCompositTypeOp)
		{
			return leftValueName_+operator_+"("+rightValueName_+");\n";
		}
		return leftValueName_+operator_+rightValueName_+";\n";
	}
	
	public static String SignalSend(String signalName_,String targetName_,String targetTypeName_,String accessOperator_,List<String> params_,Boolean rt_)
	{
		String source=targetName_+accessOperator_;
		String signal=GenerationNames.EventClassName(signalName_)+"(";
		if(rt_)
		{
			signal+=GenerationNames.DerefenrencePointer(targetName_)+",";
		}
		signal+=targetTypeName_+"::"+GenerationNames.EventEnumName(signalName_);
		String paramList=OperationCallParamList(params_);
		if(!paramList.isEmpty())
		{
			signal+=","+paramList;
		}
		signal+=")";
		
		if(rt_)
		{
			source+=RuntimeTemplates.SendSignal(signal);
		}
		else
		{
			source+=""+GenerationNames.ProcessEventFName+"("+signal;
		}
		
		return source+");\n";
	}
	
	public static String TransitionActionCall(String operationName_)
	{
		List<String> params=new LinkedList<String>();
		params.add(GenerationNames.EventFParamName);
		return OperationCall(operationName_,params);
	}
	
	public static String OperationCall(String operationName_)
	{
		return OperationCall(operationName_,null);
	}
	
	
	public static String OperationCall(String operationName_,List<String> params_)
	{
		String source=operationName_+"(";
		if(params_!=null)
		{
			source+=OperationCallParamList(params_);
		}
		source+=");\n";
		return source;
	}
	
	public static String OperationCall(String ownerName_,String accessOperator_,String operationName_,List<String> params_)
	{
		String source=OperationCall(operationName_, params_);
		if(ownerName_!=null && ownerName_!="")
		{
			source=ownerName_+accessOperator_+source;
		}
		return source;
	}
	
	private static String OperationCallParamList(List<String> params_)
	{
		String source="";
		for(String param:params_)
		{
			source+=param+",";
		}
		if(!params_.isEmpty())
		{
			source=source.substring(0,source.length()-1);
		}
		return source;
	}
	
	public static String SimpleCondControlStruct(String control_,String cond_,String body_)
	{
		return control_+"("+cond_+")\n{\n"+body_+"}\n";
	}
	
	public static String SimpleIf(String cond_,String body_)
	{
		return SimpleCondControlStruct("if", cond_, body_);
	}
	
	public static String ElseIf(List<Pair<String,String>> branches_)
	{
		String source="";
		int i=1;
		for(Pair<String,String> part:branches_)
		{
			if(i == 1)
			{
				source+=SimpleIf(part.getFirst(),part.getSecond());
			}
			else if(i == branches_.size() && (part.getFirst().isEmpty() || part.getFirst().equals("else")))
			{
				source+="else\n{\n"+part.getSecond()+"}\n";
			}
			else
			{
				source+=SimpleCondControlStruct("else if", part.getFirst(), part.getSecond());
			}
			++i;
		}
		return source;
	}
	
	public static String While(String cond_,String body_)
	{
		return SimpleCondControlStruct("while", cond_, body_);
	}
	
	public static String TransitionActionParameter(String paramName_)
	{
		return GenerationNames.RealEventName+"."+paramName_;
	}
		
	public static String CreateObject(String typenName_,String objName_,Boolean rt_,Boolean isSm_)
	{
		String source=GenerationNames.PointerType(typenName_)+" "+objName_+"= "+GenerationNames.MemoryAllocator+" "+typenName_+"();\n";
		if(rt_ && isSm_)
		{
			source+=RuntimeTemplates.CreateObject(objName_);
		}
		return source;
	}
	
	public static String getOperationFromType(boolean isMultivalued_, boolean isReplace_) 
	{
		String source="";
		if(isMultivalued_)//if the type is a collection
		{
			if(isReplace_)
			{
				source=ReplaceCompositTypeOp;
			}
			else
			{
				source=AddCompositTypeOp;
			}
		}
		else //simple class or basic type
		{
			source=ReplaceSimpleTypeOp;
		}
		return source;
	}
	
	public static String AccesOperatoForType(String typeName_)//Everything is pointer
	{
		return GenerationNames.PointerAccess;
	}
	
	public static class Operators
	{
		public static final String Not="!";
		public static final String And="&&";
		public static final String Or="||";
		public static final String NotEqual="!=";
		public static final String Equal="==";
		public static final String Remove="remove";
		public static final String First="front";
		public static final String Last="back";
	}
}