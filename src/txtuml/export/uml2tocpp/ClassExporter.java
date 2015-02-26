package txtuml.export.uml2tocpp;

/***********************************************************
 * Author: Hack János
 * Version 0.9 2014.02.25
 * Email:zodiakus (at) elte.hu
 **********************************************************/

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import txtuml.export.uml2tocpp.Util.Pair;
import txtuml.export.uml2tocpp.templates.ActivityTemplates;
import txtuml.export.uml2tocpp.templates.GenerationTemplates;

public class ClassExporter 
{
	private static final int _UMLMany = -1;
	private static String _unknownGuardName="guard";
	private static String _unknownEntryName="entry";
	private static String _unknownExitName="exit";
	
	private Map<String, String> _guardMap;//<guardConstraint,guardName>
	private Map<String,Util.Pair<String,String>> _entryMap;//<name,<state,func>>
	private Map<String,Util.Pair<String,String>> _exitMap;//<name,<state,func>>
	private Map<String,Util.Pair<String,Region>> _submachineMap;// <stateName,<machinename,behavior>>
	private List<String> _subSubMachines;
	private enum FuncTypeEnum {Entry,Exit};
	
	public ClassExporter()
	{
		_guardMap=new HashMap<String, String>();
		_entryMap= null;
		_exitMap=null;
		_submachineMap=null;
		_subSubMachines=new LinkedList<String>();
	}
	
	public void createSource(Class class_,String dest_,Boolean rt_) throws FileNotFoundException, UnsupportedEncodingException
	{
		String source="";
		List<StateMachine> smList=new ArrayList<StateMachine>();
		Shared.getTypedElements(smList,class_.allOwnedElements(),UMLPackage.Literals.STATE_MACHINE);
		if(!smList.isEmpty())
		{
			Region region=smList.get(0).getRegions().get(0);
			_submachineMap=getSubMachines(region);
			createFuncTypeMap(region,FuncTypeEnum.Entry,rt_);
			createFuncTypeMap(region,FuncTypeEnum.Exit,rt_);
			
			for(Map.Entry<String, Pair<String, Region>> entry:_submachineMap.entrySet())
			{
				ClassExporter classExporter=new ClassExporter();
				classExporter.createSubSmSource(entry.getValue().getKey(),class_.getName(),entry.getValue().getValue(),dest_);
				_subSubMachines.addAll(classExporter.getSubmachines());
			}
		}
		
		source=createClassHeaderSource(class_,rt_);
		Shared.writeOutSource(dest_,GenerationTemplates.HeaderName(class_.getName()), GenerationTemplates.HeaderGuard(source,class_.getName()));
		source=createClassCppSource(class_,rt_);
		Shared.writeOutSource(dest_,GenerationTemplates.SourceName(class_.getName()),GenerationTemplates.CppInclude(class_.getName())+getAllDependency(class_,false,rt_)+source);
	}
	
	public List<String> getSubmachines()
	{
		List<String> ret=new LinkedList<String>();
		if(_submachineMap!=null)
		{
			for(Map.Entry<String, Pair<String, Region>> entry:_submachineMap.entrySet())
			{
				ret.add(entry.getValue().getKey());
			}
			ret.addAll(_subSubMachines);
		}
		return ret;
	}
	
	private void createSubSmSource(String className_,String parentClass_, Region region_, String dest_) throws FileNotFoundException, UnsupportedEncodingException
	{
		String source="";
		_submachineMap=getSubMachines(region_);
		createFuncTypeMap(region_,FuncTypeEnum.Entry,false);
		createFuncTypeMap(region_,FuncTypeEnum.Exit,false);
		
		for(Map.Entry<String, Pair<String, Region>> entry:_submachineMap.entrySet())
		{
			createSubSmSource(entry.getValue().getKey(),parentClass_,entry.getValue().getValue(),dest_);
		}
		
		source=createSubSmClassHeaderSource(className_,parentClass_,region_);
		Shared.writeOutSource(dest_,GenerationTemplates.HeaderName(className_),GenerationTemplates.HeaderGuard(source,className_));
		source=createSubSmClassCppSource(className_,parentClass_,region_);
		Shared.writeOutSource(dest_,GenerationTemplates.SourceName(className_),GenerationTemplates.CppInclude(className_)+"\n"+source);
	}
	
	private String createClassHeaderSource(Class class_,Boolean rt_) 
	{
		String source="";
		List<StateMachine> smList=new ArrayList<StateMachine>();
		Shared.getTypedElements(smList,class_.allOwnedElements(),UMLPackage.Literals.STATE_MACHINE);
		String dependency=getAllDependency(class_,true,rt_);
		String privateParts=createParts(class_,"private");
		String protectedParts=createParts(class_,"protected");
		String publicParts=createParts(class_,"public");
		if(!smList.isEmpty())
		{
			Region region=smList.get(0).getRegions().get(0);
			privateParts+=createEntryFunctionsDecl(region) + createExitFunctionsDecl(region) +
					   createGuardFunctions(region)+
					   createTransitionFunctionDecl(region);
			publicParts+=GenerationTemplates.StateEnum(getStateList(region))+
					   	GenerationTemplates.EventEnum(getEventList(region));
			
			if(_submachineMap.isEmpty())
			{
				source=GenerationTemplates.SimpleStateMachineClassHeader(dependency,class_.getName(),publicParts,protectedParts,privateParts,rt_);
			}
			else
			{
				source=GenerationTemplates.HierarchicalStateMachineClassHeader(dependency,class_.getName(),getSubmachines(),publicParts,protectedParts,privateParts,rt_);
			}
		}
		else
		{
			source=GenerationTemplates.ClassHeader(dependency,class_.getName(),publicParts,protectedParts,privateParts );
		}
		return source;
	}

	private String createSubSmClassHeaderSource(String className_,String parentclass_, Region region_) 
	{
		String source="";
		String dependency=GenerationTemplates.CppInclude(parentclass_)+"\n";
		
		String privateParts=createEntryFunctionsDecl(region_) + createExitFunctionsDecl(region_) +
				   GenerationTemplates.formatSubSmFunctions(createGuardFunctions(region_))+
				   createTransitionFunctionDecl(region_);
		String protectedParts="";
		String publicParts=GenerationTemplates.StateEnum(getStateList(region_));
		
		if(_submachineMap.isEmpty())
		{
			source=GenerationTemplates.SimpleSubStateMachineClassHeader(dependency,className_,parentclass_,publicParts,protectedParts,privateParts);
		}
		else
		{
			source=GenerationTemplates.HierarchicalSubStateMachineClassHeader(dependency,className_,parentclass_,getSubmachines(),publicParts,protectedParts,privateParts);
		}
		return source;
	}

	private String createClassCppSource(Class class_,Boolean rt_) 
	{
		String source="";
		List<StateMachine> smList=new ArrayList<StateMachine>();
		Shared.getTypedElements(smList,class_.allOwnedElements(),UMLPackage.Literals.STATE_MACHINE);
		if(!smList.isEmpty())
		{
			Region region=smList.get(0).getRegions().get(0);
			Map<Util.Pair<String,String>,Util.Pair<String,String>> smMap=createMachine(region);
			if(_submachineMap.isEmpty())
			{
				source+=GenerationTemplates.SimpleStateMachineClassConstructor(class_.getName(),smMap,getInitialState(region),rt_);
			}
			else
			{
				source+=GenerationTemplates.HierarchicalStateMachineClassConstructor(class_.getName(),smMap,getEventSubmachineNameMap(),getInitialState(region),rt_);
			}
			source+=createEntryFunctionsDef(class_.getName(),region)+
					createExitFunctionsDef(class_.getName(),region)+
					createTransitionFunctionsDef(class_.getName(),region,rt_);
			
			source+=GenerationTemplates.Entry(class_.getName(), createStateActionMap(_entryMap,region))+"\n";
			source+=GenerationTemplates.Exit(class_.getName(), createStateActionMap(_exitMap,region))+"\n";
		}
		else 
		{
			source += GenerationTemplates.ConstructorDef(class_.getName());
		}
		
		
		for(Operation item:class_.getAllOperations())
		{
			String returnType=getReturnType(item.getReturnResult());
			Behavior behavior=item.getMethods().get(0);
			String funcBody="";
			if(behavior.eClass().equals(UMLPackage.Literals.ACTIVITY))
			{
				funcBody=ActivityExport.createfunctionBody((Activity)behavior,rt_);
			}
			else
			{
				//TODO exception, unknown for me, need the model
			}
			
			source+=GenerationTemplates.FunctionDef(class_.getName(),
													returnType,
													item.getName(),getOperationParams(item),
													funcBody);
		}
		return source;
	}
	
	private String createSubSmClassCppSource(String className_,String parentClass_, Region region_) 
	{
		String source="";
		Map<Util.Pair<String,String>,Util.Pair<String,String>> smMap=createMachine(region_);
		if(_submachineMap.isEmpty())
		{
			source+=GenerationTemplates.SimpleSubStateMachineClassConstructor(className_,parentClass_,smMap,getInitialState(region_));
		}
		else
		{
			source+=GenerationTemplates.HierarchicalSubStateMachineClassConstructor(className_,parentClass_,smMap,getEventSubmachineNameMap(),getInitialState(region_));
		}
		String subSmSpec=createEntryFunctionsDef(className_,region_)+
				createExitFunctionsDef(className_,region_)+
				createTransitionFunctionsDef(className_,region_,false)+
				GenerationTemplates.Entry(className_, createStateActionMap(_entryMap,region_))+"\n"+
				GenerationTemplates.Exit(className_, createStateActionMap(_exitMap,region_))+"\n";
		
		return source+GenerationTemplates.formatSubSmFunctions(subSmSpec);
	}
	
	private Map<String,Util.Pair<String,Region>> getSubMachines(Region region_) 
	{
		Map<String,Util.Pair<String,Region>> submachineMap=new HashMap<String, Util.Pair<String,Region>>();
		for(State state:getStateList(region_))
		{
			//either got a submachine or a region, both is not permitted
			StateMachine m=state.getSubmachine();
			if(m!=null)
			{
				submachineMap.put(state.getName(),new Util.Pair<String, Region>(m.getName(),m.getRegions().get(0)));
			}
			else
			{
				List<Region> r=state.getRegions();
				if(!r.isEmpty())
				{
					submachineMap.put(state.getName(),new Util.Pair<String, Region>(state.getName()+"_subSM",r.get(0)));
				}
			}	
		}
		return submachineMap;
	}

	private void createFuncTypeMap(Region region,FuncTypeEnum funcType_,Boolean rt_)
	{
			Map<String,Util.Pair<String, String>> map=new HashMap<String,Util.Pair<String,String>>();
			String source="";
			String name="";
			for(State item:getStateList(region))
			{
				Behavior behavior=null;
				String unknownName=null;
				switch(funcType_)
				{
					case Entry:
					{
						behavior=item.getEntry();
						unknownName=_unknownEntryName;
						break;
					}
					case Exit:
					{
						behavior=item.getExit();
						unknownName=_unknownExitName;
						break;
					}
				}

				if(behavior!= null)
				{
					if(behavior.eClass().equals(UMLPackage.Literals.ACTIVITY))
					{
						source=ActivityExport.createfunctionBody((Activity)behavior,rt_);
						name = behavior.getName();
						if(name == null || name.isEmpty())
						{
							name=item.getName()+"_"+unknownName;
						}
						map.put(name,new Util.Pair<String, String>(item.getName(),source));
					}
				}
			}
			
			if(funcType_== FuncTypeEnum.Entry)
			{
				_entryMap=map;
			}
			else if(funcType_ == FuncTypeEnum.Exit)
			{
				_exitMap=map;
			}

	}
	
	private String createEntryFunctionsDecl(Region region_) 
	{
		String source="";
		for(Map.Entry<String,Util.Pair<String, String>> entry:_entryMap.entrySet())
		{
			source+=GenerationTemplates.FunctionDecl(entry.getKey());
		}
		return source;
	}
	
	private String createExitFunctionsDecl(Region region_) 
	{
		String source="";
		for(Map.Entry<String,Util.Pair<String, String>> entry:_exitMap.entrySet())
		{
			source+=GenerationTemplates.FunctionDecl(entry.getKey());
		}
		return source;
	}
	

	private String createGuardFunctions(Region region_)
	{
		String source="";
		Integer unknownGuardCount=0;
		for(Transition item:region_.getTransitions())
		{
			Constraint constraint=item.getGuard();
			if(constraint!=null)
			{
				String guardName=_unknownGuardName+unknownGuardCount.toString();
				unknownGuardCount++;
				if(constraint.getName() != null && !constraint.getName().isEmpty())
				{
					guardName=constraint.getName();
					unknownGuardCount--;
				}
				
				String guard=Shared.getGuard(constraint);
				if(guard.equals("else"))
				{
					guard=Shared.calculateSmElseGuard(item);

				}
				
				_guardMap.put(guard,guardName);
				source+=GenerationTemplates.GuardFunction(guardName,guard);
			}
			
		}
		
		return source+"\n";
	}

	private String getAllDependency(Class class_,Boolean isHeader_,Boolean rt_)
	{
		String source="";
		List<String> types=new ArrayList<String>();
		
		//collecting each item type for dependency analysis
		for(Operation item:class_.getAllOperations())
		{
				if(item.getReturnResult() != null)
				{
					types.add(item.getReturnResult().getType().getName());
				}
				types.addAll(getOperationParamTypes(item));
		}
		
		Boolean multip=false;
		for(Property item:Shared.getProperties(class_))
		{
			if(item.getType() != null )
			{
				types.add(item.getType().getName());
				if( (item.getUpper() > 1 || item.getUpper() == _UMLMany ) && !multip)
				{
					multip=true;
					source+=GenerationTemplates.ManyMultiplicityDependacy();
				}
			}
			//TODO else throw except, if we want to terminate the compile

		}
		
		if(_submachineMap!=null)
		{
			for(Map.Entry<String,Util.Pair<String,Region>> entry:_submachineMap.entrySet())
			{
				types.add(entry.getValue().getKey());
			}
		}
		
		//dependency analysis
		String header;
		for(String t:types)
		{				
			if(!Shared.isBasicType(t) && t!=class_.getName())
			{
				if(isHeader_)
				{
					header=GenerationTemplates.ForwardDeclaration(t);
				}
				else
				{
					header=GenerationTemplates.CppInclude(t);
				}
					
				if(!source.contains(header))
				{
					source+=header;
				}
			}
		}
		
		if(rt_ && !isHeader_)
		{
			source+=GenerationTemplates.CppInclude(GenerationTemplates.RuntimeHeader);
		}
		
		return source+"\n";
	}

	private String createTransitionFunctionDecl(Region region_) 
	{
		String source="";
		for(Transition item:region_.getTransitions())
		{
			source+=GenerationTemplates.TransitionActionDecl(item.getName());
		}
		
		return source+"\n";
	}
	
	private String createTransitionFunctionsDef(String className_,Region region_,Boolean rt_) 
	{
		String source="";
		for(Transition item:region_.getTransitions())
		{
			String body="";
			String eventName=isParameterisedEventTrigger(item);
			
			Behavior b=item.getEffect();
			if(b != null && b.eClass().equals(UMLPackage.Literals.ACTIVITY))
			{
				Activity a=(Activity)b;
				body+=ActivityExport.createfunctionBody(a,rt_);
			}
			if(!eventName.isEmpty() && !body.isEmpty())
			{
				body=GenerationTemplates.GetRealEvent(eventName)+GenerationTemplates.EventParamUsage(eventName,body);
			}
			
			source+=GenerationTemplates.TransitionActionDef(className_,item.getName(),body+createSetState(item)+"\n");
		}
		
		
		
		return source+"\n";
	}


	/* handle the choice in the statemachine
	 * looks: state -transition- choiceNode < (guard1/tran1) (guard2/tran2)
	 * */
	private String createSetState(Transition transition_)
	{
		String source="";
		Vertex targetState=transition_.getTarget();
		if(targetState.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) //choice handling
		{
			List<Util.Pair<String,String>> branches= new LinkedList<Util.Pair<String,String>>();
			Util.Pair<String,String> elseBranch=null;
			for(Transition trans:targetState.getOutgoings())
			{
				String guard=Shared.getGuard(trans.getGuard());
				String body=ActivityTemplates.TransitionActionCall(trans.getName());
				
				if(guard.isEmpty() || guard.equals("else"))
				{
					elseBranch=new Util.Pair<String,String>(guard,body);
				}
				else
				{
					branches.add(new Util.Pair<String,String>(guard,body));
				}
			}
			if(elseBranch!=null)
			{
				branches.add(elseBranch);
			}
			source=ActivityTemplates.ElseIf(branches);
		}
		else if(targetState.eClass().equals(UMLPackage.Literals.STATE))
		{
			source=GenerationTemplates.SetState(targetState.getName());
		}
		else
		{
			source=GenerationTemplates.SetState("UNKNOWN_TRANSITION_TARGET");
		}
		return source;
	}
	

	private String isParameterisedEventTrigger(Transition transition_)
	{
		for(Trigger tri:transition_.getTriggers())
		{
			Event e=tri.getEvent();
			if(e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT))
			{
				SignalEvent se=(SignalEvent)e;
				Signal sig=se.getSignal();
				if(se.getSignal().getAllAttributes() != null && !se.getSignal().getAllAttributes().isEmpty())
				{
					return sig.getName();
				}
			}
		}
		return "";
	}
	
	private String createParts(Class class_,String modifyer_)
	{
		String source="";
		for(Operation item:class_.getOwnedOperations())
		{
			if(item.getVisibility().toString().equals(modifyer_))
			{
				String returnType=getReturnType(item.getReturnResult());
				source+=GenerationTemplates.FunctionDecl(returnType, item.getName(),getOperationParamTypes(item));
			}
		}
		
		for(Property item:Shared.getProperties(class_))
		{
			if(item.getVisibility().toString().equals(modifyer_))
			{
				String type="!!!UNKNOWNTYPE!!!";
				if(item.getType() != null)
				{
					type=item.getType().getName();
				}
				
				int multip = item.getUpper();
				if(item.getUpper() == _UMLMany)
				{
					multip=2;
				}
				
				String tmp=GenerationTemplates.VariableDecl(type,item.getName(),multip);
				if(!source.contains(tmp))
				{
					source+=tmp;
				}
			}
			//TODO else exception if we want to stop the compile
		}
		
		return source;
	}
	

	
	private List<String> getOperationParamTypes(Operation op_)
	{
		List<String> ret=new ArrayList<String>();
		for(Parameter param:op_.getOwnedParameters())
		{
			if(param != op_.getReturnResult())
			{
				if(param.getType() != null)
				{
					ret.add(param.getType().getName());
				}
			}
		}
		return ret;
	}
	
	private List<Util.Pair<String,String>> getOperationParams(Operation op_)
	{
		List<Util.Pair<String,String>> ret=new ArrayList<Util.Pair<String,String>>();
		for(Parameter param:op_.getOwnedParameters())
		{
			if(param != op_.getReturnResult())
			{
				if(param.getType() != null)
				{
					ret.add(new Util.Pair<String, String>(param.getType().getName(),param.getName()));
				}
				else
				{
					//TODO exception if we want to stop the compile (missing operation, seems fatal error)
					ret.add(new Util.Pair<String, String>("UNKNOWN_TYPE",param.getName()));
				}
			}
		}
		return ret;
	}
	
	private Map<String, String> getEventSubmachineNameMap() 
	{
		Map<String, String> ret=new HashMap<String, String>();
		for(Map.Entry<String, Pair<String, Region>> entry:_submachineMap.entrySet())
		{
			ret.put(entry.getKey(), entry.getValue().getKey());
		}
		return ret;
	}

	private Map<String, String> createStateActionMap(Map<String, Util.Pair<String, String>> map_,Region region_) 
	{
		Map<String,String> ret=new HashMap<String,String>();
		for(Map.Entry<String,Util.Pair<String, String>> entry:map_.entrySet())
		{
			ret.put(entry.getValue().getKey(), entry.getKey());
		}
		return ret;
	}

	private String createEntryFunctionsDef(String className_,Region region_)
	{
		String source="";
		for(Map.Entry<String,Util.Pair<String, String>> entry:_entryMap.entrySet())
		{
			source+=GenerationTemplates.FunctionDef(className_,entry.getKey(),entry.getValue().getValue());
		}
		return source;
	}
	
	private String createExitFunctionsDef(String className_,Region region_) 
	{
		String source="";
		for(Map.Entry<String,Util.Pair<String, String>> entry:_exitMap.entrySet())
		{
			source+=GenerationTemplates.FunctionDef(className_,entry.getKey(),entry.getValue().getValue());
		}
		return source;
	}

	private String getReturnType(Parameter returnResult_) 
	{
		String returnType=null;

		if(returnResult_ != null)
		{
			returnType=returnResult_.getType().getName();
		}
		return returnType;
	}

	private String getInitialState(Region region_)
	{
		String source="NO_INITIAL_STATE";
		for(Transition item:region_.getTransitions())
		{
			if(item.getSource().eClass().equals(UMLPackage.Literals.PSEUDOSTATE))
			{
				source=item.getTarget().getName();//TODO only works if the end is a state (not choice,etc..)!!!!
				break;
			}
		}
		return source;
	}

	/*
	 * Map<Util.Pair<String, String>,<String,String>
	 *                <event, state>,<guard,handlerName>
	 * */
	private Map<Util.Pair<String,String>,Util.Pair<String,String>> createMachine(Region region_)
	{
		Map<Util.Pair<String,String>,Util.Pair<String,String>> smMap=new HashMap<Util.Pair<String,String>,Util.Pair<String,String>>();
		for(Transition item:region_.getTransitions())
		{
			Util.Pair<String,String> eventSignalPair=null;
			for(Trigger tri:item.getTriggers())
			{
				Event e=tri.getEvent();
				if(e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT))
				{
					SignalEvent se=(SignalEvent)e;
					if(se != null)
					{
						eventSignalPair=new Util.Pair<String, String>(se.getSignal().getName(),item.getSource().getName());
					}
				}
			}
			if(eventSignalPair != null)
			{
				Util.Pair<String,String> guardTransitionPair=null;
				if(item.getGuard() != null)
				{
					guardTransitionPair=new Util.Pair<String, String>(_guardMap.get(Shared.getGuard(item.getGuard())),item.getName());
				}
				else
				{
					guardTransitionPair=new Util.Pair<String, String>(null,item.getName());
				}
				smMap.put(eventSignalPair,guardTransitionPair);
			}
		}
		return smMap;
	}
	
	private List<State> getStateList(Region region_)
	{
		List<State> stateList=new ArrayList<State>();
		for(Vertex item:region_.getSubvertices())
		{
			if(item.eClass().equals(UMLPackage.Literals.STATE))
			{
				stateList.add((State)item);
			}
		}
		return stateList;
	}
	
	private Set<SignalEvent> getEventList(Region region_)
	{
		Set<SignalEvent> eventList=new HashSet<SignalEvent>();
		for(Transition item:region_.getTransitions())
		{
				for(Trigger tri:item.getTriggers())
				{
					Event e=tri.getEvent();
					if(e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT))
					{
						SignalEvent se=(SignalEvent)e;
						if(se != null && ! eventList.contains(se))
						{
							eventList.add(se);
						}
					}
				}
		}
		
		//get the submachine events
		Map<String,Util.Pair<String,Region>> submachineMap=getSubMachines(region_);
		for(Map.Entry<String,Util.Pair<String,Region>> entry:submachineMap.entrySet())
		{
			eventList.addAll(getEventList(entry.getValue().getValue()));
		}
			
		return eventList;
	}
	
}
