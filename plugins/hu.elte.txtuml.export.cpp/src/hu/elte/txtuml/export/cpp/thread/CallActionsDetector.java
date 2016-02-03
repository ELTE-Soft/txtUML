package hu.elte.txtuml.export.cpp.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.description.ThreadPoolConfiguration;

public class CallActionsDetector {
	
	
	private Map<String, ThreadPoolConfiguration > threadDescription;
	private List<Class> classList;
	private List<String> syncronousWarnings;
	
	public CallActionsDetector(Model model, Map<String, ThreadPoolConfiguration > threadDescription) {
		this.threadDescription = threadDescription;
		Shared.getTypedElements(classList,model.allOwnedElements(),UMLPackage.Literals.CLASS);
		syncronousWarnings = new ArrayList<String>();
	}
	
	public List<String> getWarnings() {
		return syncronousWarnings;
	}
	
	public void detectSynchronousCallConflicts() {
		boolean conflict = false;
		
		for(String currentClass: threadDescription.keySet() ){
			
			List<String> concurrentClasses = createConcurentList(currentClass);
			
			for(String conccurentClass: concurrentClasses){
				conflict = threreIsSyncrhonCall(currentClass,conccurentClass);
				if(conflict){
					syncronousWarnings.add("There is a CallOperationAction between " + currentClass + " and " + conccurentClass);
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
