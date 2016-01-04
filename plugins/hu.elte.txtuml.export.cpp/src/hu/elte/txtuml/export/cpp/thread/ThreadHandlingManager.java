package hu.elte.txtuml.export.cpp.thread;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.description.*;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

public class ThreadHandlingManager {
	
	Model model;
	boolean isThreadHandling;
	private static Map<String, ThreadPoolConfiguration > threadDescription;
	private Set<ThreadPoolConfiguration> pools;
	private int poolsNumber;
	
	private static final String ThreadManagerName = "ThreadPoolManager";
	private static final String ThreadManagerCppSourceName = "threadpoolmanager.cpp";
	private static final String PoolsMapName = "id_matching_map";
	private static final String ThreadPool = "StateMachineThreadPool";
	private static final String FunctionName = "LinearFunction";
	private static final String FunctionTypePointer = FunctionName + "*";
	private static final String IDType = "id_type";
	private static final String ThreadPoolTypePointer = ThreadPool + "*";
	private static final String PoolsNumberField = "number_of_pools";
	private static final String FunctionMapName = "function_matching_map";
	private static final String MaximumThreadsMapName = "maximum_thread_map";
	
	int numberOfThreads;
	
	
	private List<Class> classList;
	
	public ThreadHandlingManager(Model model,  Map<String, ThreadPoolConfiguration > description){
		
		isThreadHandling = true;
		
		this.model = model;
		ThreadHandlingManager.threadDescription = description;
		
		
		numberOfThreads = threadDescription.size();
		
		classList = new ArrayList<Class>();
		Shared.getTypedElements(classList,model.allOwnedElements(),UMLPackage.Literals.CLASS);
		
		Collection<ThreadPoolConfiguration> poolsCollection = threadDescription.values();
		pools = new LinkedHashSet<ThreadPoolConfiguration>();
		pools.addAll(poolsCollection);
		poolsNumber = pools.size();
		
		if (classList.size() != threadDescription.size())
		{
			ThreadPoolConfiguration defaultConfig = new ThreadPoolConfiguration(0,0,1);
			defaultConfig.setMaxThreads(1);
			
			pools.add(defaultConfig);
			
		}
		
		
	}
	
	public ThreadHandlingManager() {
		isThreadHandling = false;
	}
	
	public static Map<String, ThreadPoolConfiguration >  Description(){
		return threadDescription;
	}
	
	public void createThreadPoolManager(String dest) {
		
		String source = createMaganerCppCource();
		
		try {
			Shared.writeOutSource(dest, ThreadManagerCppSourceName, source);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private String createMaganerCppCource() {
		String source = "";
		
		source = source + GenerationTemplates.CppInclude(ThreadManagerName.toLowerCase());
		source = source + createConstructorHead();
		source = source + createConstructorBody(isThreadHandling);
		
		return source;
	}
	
	private String createConstructorHead() {
		return ThreadManagerName + "::" + ThreadManagerName + 
				"(): "+ PoolsNumberField + "(" + (poolsNumber + 1) + ")\n";
	}
	
	private String createConstructorBody(boolean threadHandling) {
		String body = "";
		
		if (threadHandling) {
			body = "{\n" + setPoolsMap() + setFunctionMap() + maximumThreadMap() + "}\n\n";
		}
		else {
			body = GenerationTemplates.EmptyBody();
		}
		
		
		return body;
	}
	
	private String maximumThreadMap() {
		String source = "";
		
		for(ThreadPoolConfiguration pool: pools){
			source = source + "\t" + 
					insertToConfigurationMap(MaximumThreadsMapName,IDType,"int",new Integer(pool.getId()).toString(),
							new Integer(pool.getMaxThread()).toString()) + ";\n" ;
		}
		
		return source;

	}

	private String setFunctionMap() {
		
		String source = "";
		
		for(ThreadPoolConfiguration pool: pools){
			List<String> params = new ArrayList<String>();
			
			params.add(new Integer(pool.getFunction().getConstant()).toString());
			params.add(new Double(pool.getFunction().getGradient()).toString());
			source = source + "\t" + 
					insertToConfigurationMap(FunctionMapName,IDType,FunctionTypePointer,new Integer(pool.getId()).toString(),
							GenerationTemplates.AllocateObject(FunctionName, params)) + ";\n" ;
		}
		
		return source;
	}

	private String setPoolsMap() {
		
		String source = "";
		
		for(ThreadPoolConfiguration pool: pools){
			
			List<String> params = new ArrayList<String>();
			
			params.add(new Integer(pool.getFunction().getConstant()).toString());
			source = source + "\t" + 
					insertToConfigurationMap(PoolsMapName,IDType,ThreadPoolTypePointer,new Integer(pool.getId()).toString(),
							GenerationTemplates.AllocateObject(ThreadPool, params)) + ";\n" ;
		}
		
		return source;
	}
	
	private String insertToConfigurationMap(String mapName, String keyType, String valueType,String key,String value) {
		return mapName + ".insert(std::pair<" + keyType + "," + valueType + ">(" + key + "," + value + ") )";  
	}



	public void detectSynchronousCallConflicts() {
		boolean conflict = false;
		
		for(String currentClass: threadDescription.keySet() ){
			
			List<String> concurrentClasses = createConcurentList(currentClass);
			
			for(String conccurentClass: concurrentClasses){
				conflict = threreIsSyncrhonCall(currentClass,conccurentClass);
				
				//debug
				if(conflict){
					System.out.println("Thre is synchronous call between " + currentClass + " and " + conccurentClass);
				}
				
			}
			
		}
			
	}
	
	private List<String> createConcurentList(String cls) {
		List<String> concurrentList = new ArrayList<String>();
		
		int poolId = threadDescription.get(cls).getId();

		for(String concurrentCls: threadDescription.keySet()){
			if(threadDescription.get(concurrentCls).getId() != poolId){
				concurrentList.add(concurrentCls);
			}
		}
		
		return concurrentList;
	}
	
	
	
	private boolean threreIsSyncrhonCall(
			String currentClass,
			String concurrentClass) {
		Class from = getClassFromUMLModel(currentClass);
		Class to = getClassFromUMLModel(concurrentClass);
		
		
		if(!isInAssoc(from, to)){
			System.out.println(from.getName() + " and " + to.getName() + " is not in assoc");
			return false;
		}
		else{
			System.out.println(from.getName() + " and " + to.getName() + " is in assoc");
			//Detect syncrhon call
			for (Behavior b : from.getOwnedBehaviors()){
				
				if(b.eClass().equals(UMLPackage.Literals.STATE_MACHINE)){
					
					StateMachine fromSM = (StateMachine) b;
					Region fromR = fromSM.getRegion(from.getName());
					for(Vertex vertex: fromR.getSubvertices()){
						if(vertex.eClass().equals(UMLPackage.Literals.STATE)){
							State state = (State) vertex;
							Behavior entry = state.getEntry();
							Behavior exit = state.getExit();
							if(entry != null){
								containsCallOperationForClass(entry,to);
							}
							if(exit != null){
								containsCallOperationForClass(exit,to);
							}
						}
					}
					
					for(Transition trans : fromR.getTransitions()){
						
						Behavior fromEffect = trans.getEffect();
						if(fromEffect != null){
							if(containsCallOperationForClass(fromEffect,to)){
								return true;
							}
						}
					}
					
				}
				else if(b.eClass().equals(UMLPackage.Literals.ACTIVITY)){
					if(containsCallOperationForClass(b,to)){
						return true;
					}
					
				}
			}
			
			return false;
		}
		
	}
	
	boolean containsCallOperationForClass(Behavior behavior, Class cls){
		
		for(Element elem : behavior.allOwnedElements()){
				
			if(elem.eClass().getName().equals("CallOperationAction")){
					
				CallOperationAction action = (CallOperationAction) elem;
					
				if(action.getTarget().getType().equals(cls)){
					return true;
						
				}
					
					
			}
		}
		
		return false;
	}
	
	boolean isInAssoc(Class cls1, Class cls2){
		
		for(Association assoc : cls1.getAssociations()){
			for(Type endType: assoc.getEndTypes()){
				if(endType.getName().equals(cls2.getName())){
					return true;
				}
			}
		}
		return false;
	}
	
	
	private Class getClassFromUMLModel(String name) {
		
		for(Class cls: classList){
			if(cls.getName().equals(name)){
				return cls;
			}
		}
		return null;
	}

		
}
