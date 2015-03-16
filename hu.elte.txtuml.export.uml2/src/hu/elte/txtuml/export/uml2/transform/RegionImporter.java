package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.From;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.StateMachine;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.api.To;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.ImportException;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;

import java.lang.reflect.Method;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

class RegionImporter extends AbstractImporter {

	
	
	RegionImporter(Class<?> sourceClass,ModelElement ownerInstance,Model currentModel,Region currentRegion) throws ImportException
	{
		this.sourceClass=sourceClass;
		this.ownerInstance=ownerInstance;
		this.currentModel=currentModel;
		this.region=currentRegion;
	}
	
	Region importRegion() throws ImportException
	{
		importStates();
		importTransitions();
		return region;
	}
	
	Region getRegion()
	{
		return region;
	}
	
	private Region importStates() throws ImportException
	{
		for(Class<?> c : sourceClass.getDeclaredClasses())
        {
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
            if(ElementTypeTeller.isState(c)) 
            {	   
            	importState(c);
            }
             
        }
		return region;
	}
	
	
	private  Vertex importState(Class<?> state)	throws ImportException
	{
		Vertex vertex=createState(state);

		StateMachine.State stateInstance=(hu.elte.txtuml.api.StateMachine.State) 
				DummyInstanceCreator.createDummyInstance(state,ownerInstance);
		if(ElementTypeTeller.isCompositeState(state))
		{
			Region subRegion= new RegionImporter
					(state,stateInstance,currentModel,((State) vertex).createRegion(state.getSimpleName()))
			.importRegion();
			subRegion.setState((State) vertex);

			if(subRegion.getSubvertices().size() != 0 && !isContainsInitialState(subRegion)) 
			{
				importWarning(state.getName() + " has one or more states but no initial state (state machine will not be created)");
				return null;
			}
		}
		
		if(!ElementTypeTeller.isInitialState(state) && !ElementTypeTeller.isChoice(state))
		{
			importStateEntryAction(state, (State) vertex, stateInstance);
			importStateExitAction(state, (State) vertex, stateInstance);
		}
		return vertex;
	}
	

	private void importStateEntryAction(Class<?> stateClass,State state, StateMachine.State stateInstance)
	{
		
		try 
		{
			Method entryMethod=stateClass.getDeclaredMethod("entry");
			Activity activity=(Activity)state.createEntry(state.getName()+"_entry",UMLPackage.Literals.ACTIVITY);
			MethodImporter.importMethod(currentModel,activity, entryMethod, stateInstance);

		}
		catch (NoSuchMethodException e) 
		{
			//if there's no entry method, do nothing
		} 
		
		
	}
	

	private void importStateExitAction(Class<?> stateClass,State state, StateMachine.State stateInstance)
	{
		
		try 
		{
			Method exitMethod=stateClass.getDeclaredMethod("exit");
			Activity activity=(Activity)state.createExit(state.getName()+"_exit",UMLPackage.Literals.ACTIVITY);
			MethodImporter.importMethod(currentModel,activity, exitMethod, stateInstance);

		}
		catch (NoSuchMethodException e)
		{
			//if there's no exit method, do nothing
		} 
		
	}
	
	
	private  Region importTransitions() throws ImportException
	{
		for(Class<?> c : sourceClass.getDeclaredClasses())
	    {		
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
	    	if(ElementTypeTeller.isTransition(c))
	        {
				if (ElementTypeTeller.isState(c))
				{
					throw new ImportException(sourceClass.getName() + "." + c.getSimpleName() + " cannot be a state and a transition at the same time");
				}		
				importTransition(c);
				
	        }
	         
	    }
		return region;
	}
	
	private  Vertex createState(Class<?> state)	throws ImportException
	{
		
		if(ElementTypeTeller.isInitialState(state))
        {
			if (isContainsInitialState(region)) 
			{
            	throw new ImportException(sourceClass.getName() + " has two initial states");
			}
			return createInitialState(state);
        }
		/*else if(ElementTypeTeller.isCompositeState(state))
		{
			return createCompositeState(state);
		}*/
		else if(ElementTypeTeller.isChoice(state))
		{
			return createChoice(state);
		}
		else
		{
			return region.createSubvertex(state.getSimpleName(),UMLPackage.Literals.STATE);
		}
			
	}
	
	private Vertex createInitialState(Class<?> state)
	{
		return region.createSubvertex(state.getSimpleName(), UMLPackage.Literals.PSEUDOSTATE);
	}
	
	private Pseudostate createChoice(Class<?> state)
	{
		Pseudostate ret= (Pseudostate)region.createSubvertex(state.getSimpleName(),UMLPackage.Literals.PSEUDOSTATE);
		ret.setKind(PseudostateKind.CHOICE_LITERAL);
		return ret;
	}

	/*private State createCompositeState(Class<?> state) throws ImportException
	{
		State compositeState=(State) region.createSubvertex(state.getSimpleName(),UMLPackage.Literals.STATE);
		Region subRegion= new RegionImporter
						(state,ownerInstance,currentModel,compositeState.createRegion(state.getSimpleName()))
						.importRegion();
		subRegion.setState(compositeState);
		       
        if(subRegion.getSubvertices().size() != 0 && !isContainsInitialState(subRegion)) 
        {
        	importWarning(state.getName() + " has one or more states but no initial state (state machine will not be created)");
        	return null;
        }
        return compositeState;
	}*/
	
	
	
	
		
	private org.eclipse.uml2.uml.Transition importTransition(Class<?> trans)
	{
		String trName = trans.getSimpleName();
        From fromAnnot = trans.getAnnotation(From.class);
        To toAnnot = trans.getAnnotation(To.class);
        hu.elte.txtuml.api.Trigger triggerAnnot=trans.getAnnotation(hu.elte.txtuml.api.Trigger.class);
        
        Vertex source = region.getSubvertex(fromAnnot.value().getSimpleName());
        Vertex target = region.getSubvertex(toAnnot.value().getSimpleName());
        
        org.eclipse.uml2.uml.Transition transition=createTransitionBetweenVertices(trName,source,target);
         
        StateMachine.Transition transitionInstance = (Transition)
        		DummyInstanceCreator.createDummyInstance(trans,ownerInstance); 
        importTrigger(triggerAnnot,transition);
        importEffectAction(trans,transition,transitionInstance);
        importGuard(trans,transition,transitionInstance);
        
        return transition;
    }   
	
	private void importTrigger( hu.elte.txtuml.api.Trigger triggerAnnot,org.eclipse.uml2.uml.Transition transition)
	{
		 if(triggerAnnot!=null)
	     {
        	String eventName=triggerAnnot.value().getSimpleName();
	        Trigger trigger=transition.createTrigger(eventName);
	        trigger.setEvent((Event) currentModel.getPackagedElement(eventName+"_event"));
	     }
	}
	private void importEffectAction
		(Class<?> transitionClass,org.eclipse.uml2.uml.Transition transition, StateMachine.Transition transitionInstance)
	{
				
		
		try 
		{
			Method effectMethod=transitionClass.getDeclaredMethod("effect");
			Activity activity=(Activity)transition.createEffect(transition.getName()+"_effect",UMLPackage.Literals.ACTIVITY);
			MethodImporter.importMethod(currentModel,activity, effectMethod, transitionInstance);
		}
		catch (NoSuchMethodException e)
		{
			
			//if there's no effect method, do nothing
		} 
		
	}
	
	private void importGuard
		(Class<?> transitionClass,org.eclipse.uml2.uml.Transition transition, StateMachine.Transition transitionInstance)
	{
			try
			{
				Method guardMethod=transitionClass.getDeclaredMethod("guard");
				ModelBool returnValue=MethodImporter.importGuardMethod(currentModel,guardMethod,transitionInstance);
				
				if(returnValue!=null)
				{				
					String guardExpression=MethodImporter.getExpression(returnValue);
					
			
					if(isInstanceCalculated(returnValue))
					{
						guardExpression=guardExpression.substring(1,guardExpression.length()-1);
					}
					
					OpaqueExpression opaqueExpression=(OpaqueExpression) UMLFactory.eINSTANCE.createOpaqueExpression();
					opaqueExpression.getBodies().add(guardExpression);
					
					Constraint constraint=UMLFactory.eINSTANCE.createConstraint();
					constraint.setSpecification(opaqueExpression);
					
					transition.setGuard(constraint);
				}
						
			}
			catch (NoSuchMethodException e) {
				
				
			} 
		
	}
	
	

	private org.eclipse.uml2.uml.Transition createTransitionBetweenVertices(String name,Vertex source, Vertex target)
	{
		org.eclipse.uml2.uml.Transition transition=region.createTransition(name);
        transition.setSource(source);
        transition.setTarget(target);
        return transition;
	}
	
	private ModelElement ownerInstance;
	private Class<?> sourceClass; 
	private Model currentModel;
	private Region region;
}
