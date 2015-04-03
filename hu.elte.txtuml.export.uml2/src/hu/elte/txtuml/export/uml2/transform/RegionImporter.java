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

/**
 * Instances of this class are responsible for importing state machine regions.
 * @author Ádám Ancsin
 *
 */
class RegionImporter extends AbstractImporter {

	/**
	 * Creates a RegionImporter instance.
	 * @param ownerClass The owner class (either a model class, or a composite state) which contains the region to be imported.
	 * @param ownerInstance The dummy instance of the owner.
	 * @param currentModel The UML2 model.
	 * @param currentRegion The UML2 region.
	 * @throws ImportException
	 */
	RegionImporter(Class<?> ownerClass,ModelElement ownerInstance,Model currentModel,Region currentRegion) throws ImportException
	{
		this.ownerClass=ownerClass;
		this.ownerInstance=ownerInstance;
		this.currentModel=currentModel;
		this.region=currentRegion;
	}
	
	/**
	 * Imports the region.
	 * @return The imported UML2 region.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	Region importRegion() throws ImportException
	{
		importVertices();
		importTransitions();
		return region;
	}
	
	/**
	 * Gets the UML2 region.
	 * @return The UML2 region.
	 *
	 * @author Ádám Ancsin
	 */
	Region getRegion()
	{
		return region;
	}
	
	/**
	 * Imports the vertices (states and pseudostates) of the region.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private void importVertices() throws ImportException
	{
		for(Class<?> c : ownerClass.getDeclaredClasses())
        {
			if(!ElementTypeTeller.isModelElement(c))
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
            if(ElementTypeTeller.isVertex(c)) 	   
            	importVertex(c);        
        }
	}
	
	/**
	 * Imports a sub-region (a region belonging to a vertex).
	 * @param txtUMLVertexClass The class of the owner txtUML vertex. 
	 * @param txtUMLVertexInstance The dummy instance of the owner txtUML vertex.
	 * @param uml2Vertex The UML2 vertex.
	 * @return The imported sub-region.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private Region importSubRegion(Class<?> txtUMLVertexClass, StateMachine.Vertex txtUMLVertexInstance, Vertex uml2Vertex) 
			throws ImportException
	{
		Region subRegion= new RegionImporter(
				txtUMLVertexClass,
				txtUMLVertexInstance,
				currentModel,
				((State) uml2Vertex).createRegion(txtUMLVertexClass.getSimpleName())
			).importRegion();
		
		subRegion.setState((State) uml2Vertex);
	
		return subRegion;
	}
	
	/**
	 * Imports a vertex.
	 * @param txtUMLVertexClass The class of the txtUML vertex.
	 * @return The imported UML2 vertex.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private  Vertex importVertex(Class<?> txtUMLVertexClass) throws ImportException
	{
		Vertex vertex=createVertex(txtUMLVertexClass);

		StateMachine.Vertex vertexInstance=(hu.elte.txtuml.api.StateMachine.Vertex) 
				DummyInstanceCreator.createDummyInstance(txtUMLVertexClass,ownerInstance);
		
		if(ElementTypeTeller.isCompositeState(txtUMLVertexClass))
		{
			Region subRegion = importSubRegion(txtUMLVertexClass, vertexInstance, vertex);
			if(subRegion.getSubvertices().size() != 0 && !containsInitial(subRegion)) 
			{
				importWarning(
						txtUMLVertexClass.getName() +
						" has one or more vertices but no initial pseudostate (state machine will not be created)"
					);
				return null;
			}
		}
		
		if(ElementTypeTeller.isState(txtUMLVertexClass))
		{
			importStateEntryAction(txtUMLVertexClass, (State) vertex, (StateMachine.State) vertexInstance);
			importStateExitAction(txtUMLVertexClass, (State) vertex, (StateMachine.State) vertexInstance);
		}
		return vertex;
	}
	
	/**
	 * Imports the entry action of a state.
	 * @param txtUMLStateClass The class of the txtUML state.
	 * @param importedState The imported UML2 state.
	 * @param stateInstance The dummy instance of the txtUML state.
	 *
	 * @author Ádám Ancsin
	 */
	private void importStateEntryAction(Class<?> txtUMLStateClass,State importedState, StateMachine.State stateInstance)
	{
		
		try 
		{
			Method entryMethod=txtUMLStateClass.getDeclaredMethod("entry");
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
	
	/**
	 * Imports the exit action of a state.
	 * @param txtUMLStateClass The class of the txtUML state.
	 * @param importedState The imported UML2 state.
	 * @param stateInstance The dummy instance of the txtUML state.
	 *
	 * @author Ádám Ancsin
	 */
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
	
	/**
	 * Imports the transitions of the region.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private  void importTransitions() throws ImportException
	{
		for(Class<?> c : ownerClass.getDeclaredClasses())
	    {		
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
	    	if(ElementTypeTeller.isTransition(c))
	        {
				if (ElementTypeTeller.isVertex(c))
				{
					throw new ImportException(
							ownerClass.getName() + "." + c.getSimpleName() + " cannot be a vertex and a transition at the same time"
							);
				}		
				importTransition(c);			
	        }       
	    }

	}
	/**
	 * Creates a vertex of the right type (state/initial/choice) based on the given txtUML vertex class.
	 * @param txtUMLVertexClass The class of the txtUML vertex.
	 * @return The created vertex.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private Vertex createVertex(Class<?> txtUMLVertexClass) throws ImportException
	{	
		if(ElementTypeTeller.isInitial(txtUMLVertexClass))
        {
			if (containsInitial(region)) 
            	throw new ImportException(ownerClass.getName() + " has two initial pseudostates");

			return createInitial(txtUMLVertexClass);
        }
		else if(ElementTypeTeller.isChoice(txtUMLVertexClass))
			return createChoice(txtUMLVertexClass);
		else
			return region.createSubvertex(txtUMLVertexClass.getSimpleName(),UMLPackage.Literals.STATE);		
	}
	
	/**
	 * Creates an UML2 initial pseudostate.
	 * @param txtUMLVertexClass The class of the txtUML vertex.
	 * @return The created UML2 initial pseudostate.
	 *
	 * @author Ádám Ancsin
	 */
	private Vertex createInitial(Class<?> txtUMLVertexClass)
	{
		return region.createSubvertex(txtUMLVertexClass.getSimpleName(), UMLPackage.Literals.PSEUDOSTATE);
	}
	
	/**
	 * Creates an UML2 choice pseudostate.
	 * @param txtUMLVertexClass The class of the txtUML vertex.
	 * @return The created UML2 choice pseudostate.
	 *
	 * @author Ádám Ancsin
	 */
	private Pseudostate createChoice(Class<?> txtUMLVertexClass)
	{
		Pseudostate ret= (Pseudostate)region.createSubvertex(txtUMLVertexClass.getSimpleName(),UMLPackage.Literals.PSEUDOSTATE);
		ret.setKind(PseudostateKind.CHOICE_LITERAL);
		return ret;
	}

	/**
	 * Imports a transition.
	 * @param txtUMLTransitionClass The class of the txtUML transition.
	 * @return The imported UML2 transition.
	 *
	 * @author Ádám Ancsin
	 */
	private org.eclipse.uml2.uml.Transition importTransition(Class<?> txtUMLTransitionClass)
	{
		String transitionName = txtUMLTransitionClass.getSimpleName();
        From fromAnnotation = txtUMLTransitionClass.getAnnotation(From.class);
        To toAnnotation = txtUMLTransitionClass.getAnnotation(To.class);
        hu.elte.txtuml.api.Trigger triggerAnnotation=txtUMLTransitionClass.getAnnotation(hu.elte.txtuml.api.Trigger.class);
        
        String sourceName = fromAnnotation.value().getSimpleName();
        String targetName = toAnnotation.value().getSimpleName();
        
        Vertex source = region.getSubvertex(sourceName);
        Vertex target = region.getSubvertex(targetName);
        
        org.eclipse.uml2.uml.Transition importedTransition=createTransitionBetweenVertices(transitionName,source,target);
         
        StateMachine.Transition transitionInstance = (Transition)
        		DummyInstanceCreator.createDummyInstance(txtUMLTransitionClass,ownerInstance); 
        
        importTrigger(triggerAnnotation,importedTransition);
        importEffectAction(txtUMLTransitionClass,importedTransition,transitionInstance);
        importGuard(txtUMLTransitionClass,importedTransition,transitionInstance);
        
        return importedTransition;
    }   
	
	/**
	 * Imports the trigger of a transition.
	 * @param triggerAnnotation The @Trigget annotation of the txtUML transition class.
	 * @param importedTransition The imported UML2 transition.
	 *
	 * @author Ádám Ancsin
	 */
	private void importTrigger( hu.elte.txtuml.api.Trigger triggerAnnotation,org.eclipse.uml2.uml.Transition importedTransition)
	{
		 if(triggerAnnotation!=null)
	     {
        	String eventName=triggerAnnotation.value().getSimpleName();
	        Trigger trigger=importedTransition.createTrigger(eventName);
	        trigger.setEvent((Event) currentModel.getPackagedElement(eventName+"_event"));
	     }
	}
	
	/**
	 * Imports the effect action of a transition.
	 * @param txtUMLTransitionClass The class of the txtUML transition.
	 * @param importedTransition The imported UML2 transition.
	 * @param txtUMLTransitionInstance The dummy instance of the txtUML transition.
	 *
	 * @author Ádám Ancsin
	 */
	private void importEffectAction(
			Class<?> txtUMLTransitionClass,
			org.eclipse.uml2.uml.Transition importedTransition, 
			StateMachine.Transition txtUMLTransitionInstance
		)
	{
		try 
		{
			Method effectMethod=txtUMLTransitionClass.getDeclaredMethod("effect");
			Activity activity=(Activity)
					importedTransition.createEffect(
							importedTransition.getName()+"_effect",
							UMLPackage.Literals.ACTIVITY
						);
			MethodImporter.importMethod(currentModel,activity, effectMethod, txtUMLTransitionInstance);
		}
		catch (NoSuchMethodException e)
		{	
			//if there's no effect method, do nothing
		} 
		
	}
	
	/**
	 * Imports the guard constraint of a transition.
	 * @param txtUMLTransitionClass The class of the txtUML transition.
	 * @param importedTransition The imported UML2 transition.
	 * @param txtUMLTransitionInstance The dummy instance of the txtUML transition.
	 *
	 * @author Ádám Ancsin
	 */
	private void importGuard(
			Class<?> txtUMLTransitionClass,
			org.eclipse.uml2.uml.Transition importedTransition, 
			StateMachine.Transition txtUMLTransitionInstance
		)
	{
		try
		{
			Method guardMethod=txtUMLTransitionClass.getDeclaredMethod("guard");
			String guardExpression=MethodImporter.importGuardMethod(currentModel,guardMethod,txtUMLTransitionInstance);

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
	
	/**
	 * Creates a UML2 transition from the source UML2 vertex to the target UML2 vertex.
	 * @param name The name of the transition.
	 * @param source The source UML2 vertex.
	 * @param target The target UML2 vertex.
	 * @return The created UML2 transition.
	 *
	 * @author Ádám Ancsin
	 */
	private org.eclipse.uml2.uml.Transition createTransitionBetweenVertices(String name,Vertex source, Vertex target)
	{
		org.eclipse.uml2.uml.Transition transition=region.createTransition(name);
        transition.setSource(source);
        transition.setTarget(target);
        return transition;
	}
	
	private ModelElement ownerInstance;
	private Class<?> ownerClass; 
	private Model currentModel;
	private Region region;
}
