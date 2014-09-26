package txtuml.importer;

import java.lang.reflect.Method;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import txtuml.api.From;
import txtuml.api.To;

class RegionImporter extends AbstractImporter {

	
	RegionImporter(Class<?> sourceClass,Model currentModel,Region currentRegion) throws ImportException
	{
		this.sourceClass=sourceClass;
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
			if(!isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
            if(isState(c)) 
            {	   
            	region=importState(c);
            	
            }
             
        }
		return region;
	}
	
	
	private  Region importState(Class<?> state)	throws ImportException
	{
		Vertex vertex=createState(state);
		if(!isInitialState(state))
		{
			importStateEntryAction(state,(State) vertex);
			importStateExitAction(state,(State) vertex);
		}
		return region;
	}
	

	private void importStateEntryAction(Class<?> stateClass,State state)
	{
		
		try 
		{
			Method entryMethod=stateClass.getDeclaredMethod("entry");
			Activity activity=(Activity)state.createEntry(state.getName()+"_entry",UMLPackage.Literals.ACTIVITY);
			MethodImporter.importMethod(currentModel,activity, entryMethod, stateClass);

		}
		catch (NoSuchMethodException e) 
		{
			//if there's no entry method, do nothing
		} 
		
		
	}
	

	private void importStateExitAction(Class<?> stateClass,State state)
	{
		
		try 
		{
			Method exitMethod=stateClass.getDeclaredMethod("exit");
			Activity activity=(Activity)state.createExit(state.getName()+"_exit",UMLPackage.Literals.ACTIVITY);
			MethodImporter.importMethod(currentModel,activity, exitMethod, stateClass);

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
			if(!isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
	    	if(isTransition(c))
	        {
				if (isState(c))
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
		
		if(isInitialState(state))
        {
			if (isContainsInitialState(region)) 
			{
            	throw new ImportException(sourceClass.getName() + " has two initial states");
			}
			return region.createSubvertex(state.getSimpleName(), UMLPackage.Literals.PSEUDOSTATE);
        }
		else if(isCompositeState(state))
		{
			return createCompositeState(state);
		}
		else
		{
			return region.createSubvertex(state.getSimpleName(),UMLPackage.Literals.STATE);
		}
			
	}
	
	
	
	private State createCompositeState(Class<?> state) throws ImportException
	{
		State compositeState=(State) region.createSubvertex(state.getSimpleName(),UMLPackage.Literals.STATE);
		Region subRegion= new RegionImporter(state,currentModel,compositeState.createRegion(state.getSimpleName())).importRegion();
		subRegion.setState(compositeState);
		       
        if(subRegion.getSubvertices().size() != 0 && !isContainsInitialState(subRegion)) 
        {
        	importWarning(state.getName() + " has one or more states but no initial state (state machine will not be created)");
        	return null;
        }
        return compositeState;
	}
	
	
	
	
		
	private void importTransition(Class<?> trans)
	{
		String trName = trans.getSimpleName();
        From fromAnnot = trans.getAnnotation(From.class);
        To toAnnot = trans.getAnnotation(To.class);
        txtuml.api.Trigger triggerAnnot=trans.getAnnotation(txtuml.api.Trigger.class);
        
        Vertex source = region.getSubvertex(fromAnnot.value().getSimpleName());
        Vertex target = region.getSubvertex(toAnnot.value().getSimpleName());
        
        org.eclipse.uml2.uml.Transition transition=createTransitionBetweenVertices(trName,source,target);
         
        importTrigger(triggerAnnot,transition);
        importEffectAction(trans,transition);
        importGuard(trans,transition);
    }   
	
	private void importTrigger( txtuml.api.Trigger triggerAnnot,org.eclipse.uml2.uml.Transition transition)
	{
		 if(triggerAnnot!=null)
	     {
        	String eventName=triggerAnnot.value().getSimpleName();
	        Trigger trigger=transition.createTrigger(eventName);
	        trigger.setEvent((Event) currentModel.getPackagedElement(eventName+"_event"));
	     }
	}
	private void importEffectAction(Class<?> transitionClass,org.eclipse.uml2.uml.Transition transition)
	{
				
		
		try 
		{
			Method effectMethod=transitionClass.getDeclaredMethod("effect");
			Activity activity=(Activity)transition.createEffect(transition.getName()+"_effect",UMLPackage.Literals.ACTIVITY);
			MethodImporter.importMethod(currentModel,activity, effectMethod, transitionClass);
		}
		catch (NoSuchMethodException e)
		{
			//if there's no effect method, do nothing
		} 
		
	}
	
	private void importGuard(Class<?> transitionClass,org.eclipse.uml2.uml.Transition transition)
	{
				
		
			
			try
			{
				Method guardMethod=transitionClass.getDeclaredMethod("guard");
				
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
	
	private Class<?> sourceClass; 
	private Model currentModel;
	private Region region;
}
