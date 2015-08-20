package hu.elte.txtuml.export.uml2.transform.aspects;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelInt;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.blocks.BlockBody;
import hu.elte.txtuml.api.model.blocks.Condition;
import hu.elte.txtuml.api.model.blocks.ParameterizedBlockBody;
import hu.elte.txtuml.export.uml2.transform.ActionImporter;

/**
 * This aspect contains advices for importing actions.
 * @author Adam Ancsin
 *
 */
public privileged aspect ActionImporterAspect extends AbstractImporterAspect {

	/**
	 * This advice imports a "for" statement if Action.For is called from a txtUML method body during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around():call(void Action.For(ModelInt, ModelInt, ParameterizedBlockBody<ModelInt>) ) && isActive()
	{
		ModelInt from=(ModelInt)(thisJoinPoint.getArgs()[0]);
		ModelInt to=(ModelInt)(thisJoinPoint.getArgs()[1]);
		@SuppressWarnings("unchecked")
		ParameterizedBlockBody<ModelInt> body=(ParameterizedBlockBody<ModelInt>)(thisJoinPoint.getArgs()[2]);
		ActionImporter.importForStatement(from, to,body);
	}
	
	/**
	 * This advice imports a "while" statement if Action.While is called from a txtUML method body during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around():call(void Action.While(Condition,BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody body=(BlockBody)(thisJoinPoint.getArgs()[1]);
		ActionImporter.importWhileStatement(cond,body);
	}
	
	/**
	 * This advice imports an "if" (with no else block) statement if Action.If (2 params) is called
	 * from a txtUML method body during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around():call(void Action.If(Condition,BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody thenBody=(BlockBody)(thisJoinPoint.getArgs()[1]);
		ActionImporter.importIfStatement(cond,thenBody);
	}
	
	/**
	 * This advice imports an "if" (with else block) statement if Action.If (3 params) is called
	 * from a txtUML method body during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around():call(void Action.If(Condition,BlockBody , BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody thenBody=(BlockBody)(thisJoinPoint.getArgs()[1]);
		BlockBody elseBody=(BlockBody)(thisJoinPoint.getArgs()[2]);
		ActionImporter.importIfStatement(cond,thenBody,elseBody);
	}
	
	/**
	 * This advice imports a create link action if Action.link is called from a txtUML method body during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around(): call(void Action.link(..)) && isActive()
	{
		Class<?> leftEnd=(Class<?>)(thisJoinPoint.getArgs()[0]);
		ModelClass leftObj=(ModelClass)(thisJoinPoint.getArgs()[1]);
		Class<?> rightEnd=(Class<?>)(thisJoinPoint.getArgs()[2]);
		ModelClass rightObj=(ModelClass)(thisJoinPoint.getArgs()[3]);
		ActionImporter.importCreateLinkAction(leftEnd,leftObj,rightEnd,rightObj);	
	}
    		
	/**
	 * This advice imports a destroy link action if Action.unlink is called from a txtUML method body during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around(): call(void Action.unlink(..)) && isActive()
	{
		Class<?> leftEnd=(Class<?>)(thisJoinPoint.getArgs()[0]);
		ModelClass leftObj=(ModelClass)(thisJoinPoint.getArgs()[1]);
		Class<?> rightEnd=(Class<?>)(thisJoinPoint.getArgs()[2]);
		ModelClass rightObj=(ModelClass)(thisJoinPoint.getArgs()[3]);
		ActionImporter.importDestroyLinkAction(leftEnd,leftObj,rightEnd,rightObj);	
	}
    	
	/**
	 * This advice imports a send signal action if Action.send is called from a txtUML method body during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around(): call(void Action.send(ModelClass, Signal)) && isActive()
	{
		ModelClass receiverObj=(ModelClass)(thisJoinPoint.getArgs()[0]);
		Signal event=(Signal)(thisJoinPoint.getArgs()[1]);
		
		ActionImporter.importSendSignalAction(receiverObj, event);	
			
	}
	
	/**
	 * This advice imports a destroy instance action if Action.destroy is called from a txtUML method body during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around(): call(void Action.delete(ModelClass)) && isActive()
	{
		ModelClass obj = (ModelClass) thisJoinPoint.getArgs()[0];
		ActionImporter.importDeleteObjectAction(obj);
	}
	
	/**
	 * This advice imports a start object action if Action.start is called from a txtUML method body
	 * during model import.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around(): call(void Action.start(ModelClass)) && isActive()
	{
		ModelClass obj = (ModelClass) thisJoinPoint.getArgs()[0];
		ActionImporter.importStartObjectAction(obj);
	}
}
