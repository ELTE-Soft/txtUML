package hu.elte.txtuml.api.model.seqdiag;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.error.seqdiag.ValidationError;

/**
 * Interaction Class, representing a full Interaction( SequenceDiagram )
 *
 */
public abstract class Interaction implements Runnable /*extends Descriptor<InteractionWrapper>*/{
	
	protected static Map<String,Lifeline<?>> lifelines;
	protected static ArrayList<ValidationError> errors;
	
	protected Interaction(){}
	
	/**
	 * 
	 * @deprecated
	 */
	public void execute()
	{				
		if(lifelines == null)
		{
			lifelines = new HashMap<String, Lifeline<?>>();
		}
		
		ArrayList<Class<?>> innerClasses = new ArrayList<Class<?>>(Arrays.asList(getClass().getDeclaredClasses()));
		
		for(Class<?> innerClass : innerClasses)
		{			
			Type superClass = innerClass.getGenericSuperclass();
			
			if(superClass instanceof ParameterizedType)
			{
				ParameterizedType superClassType = (ParameterizedType)superClass;
				
				Type typeArg = superClassType.getActualTypeArguments()[0];
				
				try {
					Lifeline<? extends ModelClass> CurLifeline = (Lifeline<? extends ModelClass>)innerClass.getConstructors()[0].newInstance(this);
					Interaction.lifelines.put(innerClass.getName(), CurLifeline);
					
					Class<? extends ModelClass> currClass = (Class<? extends ModelClass>)Class.forName(typeArg.getTypeName());
	
					//CurLifeline.setInstanceClass(currClass);
					CurLifeline.getInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		for(Lifeline<? extends ModelClass> lifeline: lifelines.values())
		{
			//lifeline.run();
		}
	}
	
	public static void addError(ValidationError err)
	{
		if(errors == null)
		{
			errors = new ArrayList<>();
		}
		
		errors.add(err);
	}
	
	public static ArrayList<ValidationError> getErrors()
	{
		return errors;
	}
	
	public void initialize(){};

	/*@Override
	InteractionWrapper createRuntimeInfo() {
		
		return ((Runtime)Runtime.currentRuntime()).createInteractionWrapper(this);
	}*/
}
