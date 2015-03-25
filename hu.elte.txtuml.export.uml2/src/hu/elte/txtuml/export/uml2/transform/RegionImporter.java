package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.From;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.StateMachine;
import hu.elte.txtuml.api.StateMachine.Transition;
import hu.elte.txtuml.api.To;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
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
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
            if(ElementTypeTeller.isState(c)) 	   
            	importState(c);        
        }
		return region;
	}
	
	private Region importSubRegion(Class<?> state, StateMachine.State stateInstance, Vertex vertex) throws ImportException
	{
		Region subRegion= new RegionImporter
				(state,stateInstance,currentModel,((State) vertex).createRegion(state.getSimpleName()))
				.importRegion();
		subRegion.setState((State) vertex);
	
		return subRegion;
	}
	private  Vertex importState(Class<?> state)	throws ImportException
	{
		Vertex vertex=createState(state);

		StateMachine.State stateInstance=(hu.elte.txtuml.api.StateMachine.State) 
				DummyInstanceCreator.createDummyInstance(state,ownerInstance);
		
		if(ElementTypeTeller.isCompositeState(state))
		{
			Region subRegion = importSubRegion(state,stateInstance, vertex);
			if(subRegion.getSubvertices().size() != 0 && !containsInitialState(subRegion)) 
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
	
	private void importStateEntryAction(Class<?> stateClass,State importedState, StateMachine.State stateInstance)
	{
		
		try 
		{
			Method entryMethod=stateClass.getDeclaredMethod("entry");
			Activity activity = (Activity)
					importedState.createEntry(
							importedState.getName()+"_entry",
							UMLPackage.Literals.ACTIVITY
						);
			MethodImporter.importMethod(currentModel,activity, entryMethod, stateInstance);
			
		}
		catch (NoSuchMethodException e) 
		{
			//if there's no entry method, do nothing
		} 
		
	}
	
	private void importStateExitAction(Class<?> stateClass,State importedState, StateMachine.State stateInstance)
	{
		
		try 
		{
			Method exitMethod=stateClass.getDeclaredMethod("exit");
			Activity activity = (Activity)
					importedState.createExit(
							importedState.getName()+"_exit",
							UMLPackage.Literals.ACTIVITY
						);
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
					throw new ImportException(
							sourceClass.getName() + "." + c.getSimpleName() + " cannot be a state and a transition at the same time"
							);
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
			if (containsInitialState(region)) 
            	throw new ImportException(sourceClass.getName() + " has two initial states");

			return createInitialState(state);
        }
		else if(ElementTypeTeller.isChoice(state))
			return createChoice(state);
		else
			return region.createSubvertex(state.getSimpleName(),UMLPackage.Literals.STATE);		
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

	private org.eclipse.uml2.uml.Transition importTransition(Class<?> transition)
	{
		String transitionName = transition.getSimpleName();
        From fromAnnotation = transition.getAnnotation(From.class);
        To toAnnotation = transition.getAnnotation(To.class);
        hu.elte.txtuml.api.Trigger triggerAnnotation=transition.getAnnotation(hu.elte.txtuml.api.Trigger.class);
        
        String sourceName = fromAnnotation.value().getSimpleName();
        String targetName = toAnnotation.value().getSimpleName();
        
        Vertex source = region.getSubvertex(sourceName);
        Vertex target = region.getSubvertex(targetName);
        
        org.eclipse.uml2.uml.Transition importedTransition=createTransitionBetweenVertices(transitionName,source,target);
         
        StateMachine.Transition transitionInstance = (Transition)
        		DummyInstanceCreator.createDummyInstance(transition,ownerInstance); 
        
        importTrigger(triggerAnnotation,importedTransition);
        importEffectAction(transition,importedTransition,transitionInstance);
        importGuard(transition,importedTransition,transitionInstance);
        
        return importedTransition;
    }   
	
	private void importTrigger( hu.elte.txtuml.api.Trigger triggerAnnotation,org.eclipse.uml2.uml.Transition importedTransition)
	{
		 if(triggerAnnotation!=null)
	     {
        	String eventName=triggerAnnotation.value().getSimpleName();
	        Trigger trigger=importedTransition.createTrigger(eventName);
	        trigger.setEvent((Event) currentModel.getPackagedElement(eventName+"_event"));
	     }
	}
	
	private void importEffectAction
		(Class<?> transitionClass,org.eclipse.uml2.uml.Transition importedTransition, StateMachine.Transition transitionInstance)
	{
		try 
		{
			Method effectMethod=transitionClass.getDeclaredMethod("effect");
			Activity activity=(Activity)
					importedTransition.createEffect(
							importedTransition.getName()+"_effect",
							UMLPackage.Literals.ACTIVITY
						);
			MethodImporter.importMethod(currentModel,activity, effectMethod, transitionInstance);
		}
		catch (NoSuchMethodException e)
		{	
			//if there's no effect method, do nothing
		} 
		
	}
	
	private void importGuard
		(Class<?> transitionClass,org.eclipse.uml2.uml.Transition importedTransition, StateMachine.Transition transitionInstance)
	{
		try
		{
			Method guardMethod=transitionClass.getDeclaredMethod("guard");
			String guardExpression=MethodImporter.importGuardMethod(currentModel,guardMethod,transitionInstance);

			OpaqueExpression opaqueExpression=(OpaqueExpression) UMLFactory.eINSTANCE.createOpaqueExpression();
			opaqueExpression.getBodies().add(guardExpression);

			Constraint constraint=UMLFactory.eINSTANCE.createConstraint();
			constraint.setSpecification(opaqueExpression);

			importedTransition.setGuard(constraint);

		}
		catch (NoSuchMethodException e) 
		{
			//no guard for this transition -> do nothing
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
