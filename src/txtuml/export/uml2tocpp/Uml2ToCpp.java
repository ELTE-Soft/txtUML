package txtuml.export.uml2tocpp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

public class Uml2ToCpp {

	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed:input file, output directory");
			return;
		}
		
		Model model= Util.loadModel(args[0]);
		buildCppCode(model,args[1]);
	}
	
	
	
	public static void buildCppCode(Model model_,String outputDir_) {
		try {
			EList<Element> elements=model_.allOwnedElements();
			
			String source=createEventSource(elements);
			writeOutSource(outputDir_,(GenerationTemplates.EventHeaderName + ".hh"),source);

			
			List<Class> classList=new ArrayList<Class>();
			getTypedElements(classList,elements,UMLPackage.Literals.CLASS);
			for(Class item:classList)
			{
				source="";
				source=createClassHeaderSource(item);
				writeOutSource(outputDir_,item.getName()+".hh", GenerationTemplates.HeaderGuard(source,item.getName()));
				source="";
				source=createClassCppSource(item);
				writeOutSource(outputDir_,item.getName()+".cpp","#include \""+item.getName()+".hh\"\n\n"+source);
			}
		} catch(IOException ioe) {
			System.out.println("IO error.");
        }
	}



	private static String createClassHeaderSource(Class class_) {
		String source="";
		List<StateMachine> smList=new ArrayList<StateMachine>();
		getTypedElements(smList,class_.allOwnedElements(),UMLPackage.Literals.STATE_MACHINE);
		if(!smList.isEmpty())
		{
			source=GenerationTemplates.StateMachineClassHeader(createDependency(class_),class_.getName(), createParts(class_,"public")+
															   GenerationTemplates.StateEnum(getStateList(smList.get(0)))+
															   GenerationTemplates.EventEnum(getEventList(smList.get(0))),
															   createTransitionFunctionDecl(smList.get(0))+createParts(class_,"private"));
		}
		else
		{
			source=GenerationTemplates.ClassHeader(createDependency(class_),class_.getName(), createParts(class_,"public"), createParts(class_,"private"));
		}
		return source;
	}
	
	private static String getCppType(String type_)
	{
		String tmp=GenerationTemplates.CppType(type_);
		if(tmp!=GenerationTemplates.unknown)
		{
			type_=tmp;
		}
		return type_;
	}
	
	private static String createDependency(Class class_) {
		String source="";
		List<String> types=new ArrayList<String>();
		//collecting each item type for dependency analysis
		EList<Operation> operations=class_.getAllOperations();
		for(Operation item:operations)
		{
				if(item.getReturnResult() != null)
				{
					types.add(item.getReturnResult().getType().getName());
				}
				types.addAll(operationParamTypes(item));
		}
		
		EList<Property> propertis=class_.getAttributes();
		for(Property item:propertis)
		{
			types.add(item.getType().getName());
		}
		
		//dependency analysis
		String tmp;
		String header;
		for(String t:types)
		{
			tmp=GenerationTemplates.CppType(t);
			if(tmp==GenerationTemplates.unknown)
			{
				header=GenerationTemplates.LocalInclude(t);
				if(!source.contains(header))
				{
					source+=header;
				}
			}
			if(tmp==GenerationTemplates.cppString)
			{
				header=GenerationTemplates.OuterInclude(tmp);
				if(!source.contains(header))
				{
					source+=header;
				}
			}
		}
		
		return source+"\n";
	}

	private static String createTransitionFunctionDecl(StateMachine machine_) 
	{
		String source="";
		EList<Region> regions=machine_.getRegions();
		Region r=regions.get(0);
		EList<Transition> transitions=r.getTransitions();
		for(Transition item:transitions)
		{
			source+=GenerationTemplates.TransitionActionDecl(item.getName());
		}
		
		return source+"\n";
	}
	
	private static String createTransitionFunctionsDef(String className_,StateMachine machine_) 
	{
		String source="";
		EList<Region> regions=machine_.getRegions();
		Region r=regions.get(0);
		EList<Transition> transitions=r.getTransitions();
		for(Transition item:transitions)
		{
			String body="";
			Behavior b=item.getEffect();
			if(b != null && b.eClass().equals(UMLPackage.Literals.ACTIVITY))
			{
				Activity a=(Activity)b;
				body=createfunctionBody(a);
			}
			source+=GenerationTemplates.TransitionActionDef(className_,item.getName(),body+GenerationTemplates.setState(item.getTarget().getName())+"\n");
		}
		
		return source+"\n";
	}


	private static String createfunctionBody(Activity activity_) {
		String source="";
		//el kell maj indulni a start node-tól és addig konvertálni amíg el nem érjük az end node-ot
		activity_.getNodes();
		activity_.getEdges();
		//
		return source;
	}

	private static String createParts(Class class_,String modifyer_)
	{
		String source="";
		EList<Operation> operations=class_.getAllOperations();
		for(Operation item:operations)
		{
			if(item.getVisibility().toString().equals(modifyer_))
			{
				String returnType="void";
				if(item.getReturnResult() != null)
				{
					returnType=item.getReturnResult().getType().getName();
				}
				source+=GenerationTemplates.FunctionDecl(getCppType(returnType), item.getName(),operationParamTypes(item));
			}
		}
		
		EList<Property> propertis=class_.getAttributes();
		for(Property item:propertis)
		{
			if(item.getVisibility().toString().equals(modifyer_))
			{
				source+=GenerationTemplates.Property(getCppType(item.getType().getName()),item.getName());
			}
		}
		
		return source;
	}
	
	private static List<String> operationParamTypes(Operation op_)//ki kell szedni a param typest
	{
		List<String> ret=new ArrayList<String>();
		return ret;
	}
	
	private static List<Util.Pair<String,String>> operationParams(Operation op_)
	{
		List<Util.Pair<String,String>> ret=new ArrayList<Util.Pair<String,String>>();
		return ret;
	}
	
	private static String createClassCppSource(Class class_) {
		String source="";
		List<StateMachine> smList=new ArrayList<StateMachine>();
		getTypedElements(smList,class_.allOwnedElements(),UMLPackage.Literals.STATE_MACHINE);
		if(!smList.isEmpty())
		{
			StateMachine sm=smList.get(0);
			int eventNum=getEventList(sm).size();//pazarlás
			int stateNum=getStateList(sm).size();//pazarlás
			Map<Util.Pair<String,String>,String> smMap=createMachine(sm);
			source+=GenerationTemplates.StateMachineClassConstructor(class_.getName(),smMap, eventNum, stateNum);
			
			source+=createTransitionFunctionsDef(class_.getName(),smList.get(0));
			
			source+=GenerationTemplates.Entry(class_.getName(), null);
			source+=GenerationTemplates.Exit(class_.getName(), null);
			
		}
		
		else 
		{
			source += GenerationTemplates.FunctionDef(class_.getName(),"",class_.getName(),null,"");
		}
		
		
		EList<Operation> operations=class_.getAllOperations();
		for(Operation item:operations)
		{
			String returnType="void";
			if(item.getReturnResult() != null)
			{
				returnType=item.getReturnResult().getType().getName();
			}
			source+=GenerationTemplates.FunctionDef(class_.getName(),getCppType(returnType), item.getName(),operationParams(item),"");
		}
		
		
		
		return source;
	}
	
	/*
	 * Map<Util.Pair<String,String>,String>
	 * event,state,handlerName
	 * */
	private static Map<Util.Pair<String,String>,String> createMachine(StateMachine machine_)
	{
		Map<Util.Pair<String,String>,String> smMap=new HashMap<Util.Pair<String,String>,String>();
		
		EList<Region> regions=machine_.getRegions();
		Region r=regions.get(0);
		EList<Transition> transitions=r.getTransitions();
		for(Transition item:transitions)
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
				smMap.put(eventSignalPair,item.getName());
			}
		}
		return smMap;
	}
	
	private static List<State> getStateList(StateMachine machine_)
	{
		List<State> stateList=new ArrayList<State>();
		EList<Region> regions=machine_.getRegions();
		Region r=regions.get(0);
		EList<Vertex> states=r.getSubvertices();
		for(Vertex item:states)
		{
			if(item.eClass().equals(UMLPackage.Literals.STATE))
			{
				stateList.add((State)item);
			}
		}
		return stateList;
	}
	
	private static List<SignalEvent> getEventList(StateMachine machine_)
	{
		List<SignalEvent> eventList=new ArrayList<SignalEvent>();
		EList<Region> regions=machine_.getRegions();
		Region r=regions.get(0);
		EList<Transition> transitions=r.getTransitions();
		for(Transition item:transitions)
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
		return eventList;
	}
	
	//paraméteres signal kezelés még nincs
	private static String createEventSource(EList<Element> elements_)
	{
		List<Signal> signalList=new ArrayList<Signal>();
		getTypedElements(signalList,elements_,UMLPackage.Literals.SIGNAL);
		String source = GenerationTemplates.EventBase+"\n";
		
		for(Signal item:signalList)
		{
			source+=GenerationTemplates.EventClass(item.getName(),new ArrayList<Util.Pair<String,String>>());//ki kell majd szedni a paramétereket
		}
		
		return GenerationTemplates.HeaderGuard(source,GenerationTemplates.EventHeaderName);
	}
	
	
	@SuppressWarnings("unchecked")
	private static <ElementType,EClassType> 
	void getTypedElements(Collection<ElementType> dest_,Collection<Element> source_,EClassType eClass_)
	{
		for(Element item:source_)
		{
			if(item.eClass().equals(eClass_))
			{
				dest_.add((ElementType)item);//it is safe ...
			}
		}
	} 
	
	
	private static void writeOutSource(String path_,String fileName_,String source_) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(path_+fileName_, "UTF-8");
        writer.println(source_);
        writer.close();
	}
}