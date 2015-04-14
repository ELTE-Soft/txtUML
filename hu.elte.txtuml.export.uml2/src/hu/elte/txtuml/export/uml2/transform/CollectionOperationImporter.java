package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.Collection;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.blocks.ParameterizedCondition;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceManager;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider;

import java.lang.reflect.ParameterizedType;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

/**
 * This class is responsible for importing operations of Collections (e.g. Collection.selectOne, Collection.selectAll, etc.)
 * inside method bodies.
 * 
 * @author Ádám Ancsin
 *
 */
public class CollectionOperationImporter extends AbstractMethodImporter {

	/**
	 * Imports a select one instruction of a Collection in a method body.
	 * @param target The dummy instance of the target Collection.
	 * @return The dummy instance of the result.
	 *
	 * @author Ádám Ancsin
	 */
	static <T extends ModelClass> T importSelectOne(Collection<T> target) 
	{
		Class<?> targetClass = target.getClass();
		Class<?> typeClass = getCollectionGenericParameterType(target);
	
		@SuppressWarnings("unchecked")
		T result=(T) DummyInstanceCreator.createDummyInstance(typeClass);
	
		String resultName=result.getIdentifier();
	
		String targetExpression = getExpression(target);
		
		String expression = targetExpression;
		if(MultiplicityProvider.isOne(targetClass) || MultiplicityProvider.isZeroToOne(targetClass))
			expression = targetExpression;
		else
			expression = targetExpression + "->any(true)";
		
		String resultExpression = expression;
		if(currentActivity != null)
		{
			OpaqueAction selectOneAction=	(OpaqueAction)
					currentActivity.createOwnedNode("selectOne_"+targetExpression,UMLPackage.Literals.OPAQUE_ACTION);
			
			selectOneAction.getBodies().add(expression);
		
			createControlFlowBetweenActivityNodes(lastNode, selectOneAction);
		
			Type type=ModelImporter.importType(typeClass);
		
			OutputPin outputPin=selectOneAction.createOutputValue(selectOneAction.getName()+"_output", type);
		
			Variable variable=createVariableForInstance(result,type);
			AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+resultName);
		
			InputPin inputPin=setVarAction.createValue(setVarAction.getName()+"_input",type);
		
			createObjectFlowBetweenActivityNodes(outputPin,inputPin);
			lastNode=setVarAction;
			
			if(result != null)
				resultExpression = result.getIdentifier();
		}

		if(result!=null)
		{
			InstanceManager.createLocalInstancesMapEntry(result, InstanceInformation.create(resultExpression));
			InstanceManager.createLocalFieldsRecursively(result);
		}
		return result;
	}
	
	/**
	 * Imports a select all instruction of a Collection in a method body.
	 * @param target The dummy instance of the target Collection.
	 * @param condition The condition of the select all instruction.
	 * @return The dummy instance of the result.
	 *
	 * @author Ádám Ancsin
	 */
	static <T extends ModelClass> Collection<T> importSelectAll(Collection<T> target, ParameterizedCondition<T> condition)
	{
		Class<?> typeClass=getCollectionGenericParameterType(target);
	
		@SuppressWarnings("unchecked")
		T conditionParameter = (T) DummyInstanceCreator.createDummyInstance(typeClass);
		InstanceManager.createLocalInstancesMapEntry(
				conditionParameter, 
				InstanceInformation.create(getObjectIdentifier(conditionParameter))
			);
		InstanceManager.createLocalFieldsRecursively(conditionParameter);
		
		String conditionExpression = importParameterizedCondition(condition, conditionParameter);
		
		@SuppressWarnings("unchecked")
		Collection<T> result = (Collection<T>) DummyInstanceCreator.createDummyInstance(target.getClass());
		String resultName=getObjectIdentifier(result);
		
		String targetExpression = getExpression(target);
		String selectExpression = targetExpression +"->select(" + conditionExpression + ")";
		String resultExpression = selectExpression;
		if(currentActivity != null)
		{
			OpaqueAction selectAllAction=	(OpaqueAction)
					currentActivity.createOwnedNode("selectAll_"+targetExpression,UMLPackage.Literals.OPAQUE_ACTION);
		
			selectAllAction.getBodies().add(selectExpression);
			
			createControlFlowBetweenActivityNodes(lastNode, selectAllAction);
		
			Type type=ModelImporter.importType(typeClass);
			
			Variable variable=createVariableForInstance(result,type);
			int lowerBound = variable.getLower();
			int upperBound = variable.getUpper();
			
			AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+resultName);
			
			OutputPin outputPin=selectAllAction.createOutputValue(selectAllAction.getName()+"_output", type);
			outputPin.setLower(lowerBound);
			outputPin.setUpper(upperBound);
			
			InputPin inputPin=setVarAction.createValue(setVarAction.getName()+"_input",type);
			inputPin.setLower(lowerBound);
			inputPin.setUpper(upperBound);
			
			createObjectFlowBetweenActivityNodes(outputPin,inputPin);
			lastNode=setVarAction;
			
			if(result != null)
				resultExpression = resultName;
		}
		
		if(result!=null)
		{
			InstanceManager.createLocalInstancesMapEntry(result, InstanceInformation.create(resultExpression));
			InstanceManager.createLocalFieldsRecursively(result);
		}
		return result;
	}

	/**
	 * Imports a parameterized condition.
	 * @param condition The condition.
	 * @param parameter The parameter of the condition.
	 * @return The condition converted to an expression.
	 *
	 * @author Ádám Ancsin
	 */
	private static <T extends ModelClass> String importParameterizedCondition(ParameterizedCondition<T> condition, T parameter)
	{
		String parameterTypeName =ModelImporter.importType(parameter.getClass()).getName();
		Activity currActivityBackup=currentActivity;
		currentActivity=null;
		ModelBool checkedCond=condition.check(parameter);
		String ret= getObjectIdentifier(parameter)+" : "+parameterTypeName+" | "+getExpression(checkedCond);
		currentActivity=currActivityBackup;
		return ret;
	}
	
	/**
	 * Gets the type of the generic parameter of the specified collection.
	 * @param collection The specified collection.
	 * @return The specified collection's generic parameter type.
	 *
	 * @author Ádám Ancsin
	 */
	@SuppressWarnings("unchecked")
	private static <T extends ModelClass> Class<? extends ModelClass>
		getCollectionGenericParameterType(Collection<T> collection)
	{
		ParameterizedType genericSuperclass = (ParameterizedType) 
				collection.getClass().getGenericSuperclass();
		
		String typeName=genericSuperclass.getActualTypeArguments()[0].getTypeName();
	
		return (Class<? extends ModelClass>) ElementFinder.findDeclaredClass(typeName, modelClass);
	}
}
