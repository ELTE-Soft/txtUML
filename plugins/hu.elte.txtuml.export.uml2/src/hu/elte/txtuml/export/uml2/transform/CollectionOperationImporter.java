package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.Collection;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelInt;
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
 * This class is responsible for importing operations of Collections (e.g. Collection.selectAny, Collection.selectAll, etc.)
 * inside method bodies.
 * 
 * @author Adam Ancsin
 *
 */
public class CollectionOperationImporter extends AbstractMethodImporter {

	private enum OperationTypes
	{
		SELECT_ANY_LITERAL,
		SELECT_ALL_LITERAL,
		ADD_ONE_LITERAL,
		ADD_ALL_LITERAL, REMOVE_LITERAL
	}
	
	/**
	 * Imports a select any instruction of a Collection in a method body.
	 * @param target The dummy instance of the target Collection.
	 * @return The dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	static <T extends ModelClass> T importSelectAny(Collection<T> target) 
	{
		Class<?> targetClass = target.getClass();
		Class<?> typeClass = getCollectionGenericParameterType(target);
	
		@SuppressWarnings("unchecked")
		T result=(T) DummyInstanceCreator.createDummyInstance(typeClass);

		String targetExpression = getExpression(target);
		
		String selectExpression;
		if(MultiplicityProvider.isOne(targetClass) || MultiplicityProvider.isZeroToOne(targetClass))
			selectExpression = targetExpression;
		else
			selectExpression = targetExpression + "->any(true)";
		
		String resultExpression = selectExpression;
		
		if(currentActivity != null)
		{
			createActivityElementsForCollectionOperation(
					"selectAny_"+targetExpression,
					targetExpression, 
					selectExpression, 
					result, 
					typeClass
				);
			
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
	 * @author Adam Ancsin
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
		
		return importOperationResultingACollection(target, conditionExpression, OperationTypes.SELECT_ALL_LITERAL);
	}

	/**
	 * Imports the "isEmpty" query of the specified target Collection inside a method body.
	 * @param target The specified target Collection.
	 * @return The dummy ModelBool instance of the query result.
	 *
	 * @author Adam Ancsin
	 */
	static <T extends ModelClass> ModelBool importIsEmpty(Collection<T> target)
	{
		String targetExpression = getExpression(target);
		String resultExpression = targetExpression+"->isEmpty()";
		
		return createAndAssignResultForQuery(ModelBool.class,resultExpression);
	}
	
	/**
	 * Imports the "contains" query of the specified target Collection inside a method body.
	 * @param target The specified target Collection.
	 * @param object The ModelClass object to check.
	 * @return The dummy ModelBool instance of the query result.
	 *
	 * @author Adam Ancsin
	 */
	static <T extends ModelClass> ModelBool importContains(Collection<T> target, ModelClass object)
	{
		String targetExpression = getExpression(target);
		String objectExpression = getExpression(object);
		String resultExpression = targetExpression + "->includes(" + objectExpression +")";
		
		return createAndAssignResultForQuery(ModelBool.class,resultExpression);
	}

	/**
	 * Imports the "count" query of the specified target Collection inside a method body.
	 * @param target The specified target Collection.
	 * @return The dummy ModelInt instance of the query result.
	 *
	 * @author Adam Ancsin
	 */
	static <T extends ModelClass> ModelInt importCount(Collection<T> target)
	{
		String targetExpression = getExpression(target);
		String resultExpression = targetExpression+"->size()";
		
		return createAndAssignResultForQuery(ModelInt.class,resultExpression);
	}
	
	/**
	 * Imports the "add" operation of the specified target Collection inside a method body.
	 * @param target The specified target Collection.
	 * @param object The ModelClass object to be added to the collection.
	 * @return The dummy instance of the result collection.
	 *
	 * @author Adam Ancsin
	 */
	static <T extends ModelClass> Collection<T> importAdd(Collection<T> target, ModelClass object)
	{
		String objectExpression = getExpression(object);
		return importOperationResultingACollection(target,objectExpression, OperationTypes.ADD_ONE_LITERAL);
	}
	
	/**
	 * Imports the "addAll" operation of the specified target Collection inside a method body.
	 * @param target The specified target Collection.
	 * @param objects The collection containing the elements to be added to the target collection.
	 * @return The dummy instance of the result collection.
	 *
	 * @author Adam Ancsin
	 */
	static <T extends ModelClass> Collection<T> importAddAll(Collection<T> target, Collection<T> objects)
	{
		String objectsExpression = getExpression(objects);
		return importOperationResultingACollection(target,objectsExpression, OperationTypes.ADD_ALL_LITERAL);
	}
	
	/**
	 * Imports the "remove" operation of the specified target Collection inside a method body.
	 * @param target The specified target Collection.
	 * @param object The object to be removed from the collection.
	 * @return The dummy instance of the result collection.
	 *
	 * @author Adam Ancsin
	 */
	static <T extends ModelClass> Collection<T> importRemove(Collection<T> target, T object)
	{
		String objectExpression = getExpression(object);
		return importOperationResultingACollection(target,objectExpression, OperationTypes.REMOVE_LITERAL);
	}
	
	/**
	 * Imports a generic collection operation (which's result is another collection of the same element type)
	 * of the specified target Collection inside a method body.
	 * @param target The specified target Collection.
	 * @param paramExpression The expression of the parameter of the operation.
	 * @param operationType The type of the operation. (e.g. selectAll, add, remove, etc.)
	 * @return The dummy instance of the result collection.
	 *
	 * @author Adam Ancsin
	 */
	private static <T extends ModelClass> Collection<T> 
		importOperationResultingACollection(Collection<T> target, String paramExpression, OperationTypes operationType )
	{
		Class<?> typeClass = getCollectionGenericParameterType(target);
	
		@SuppressWarnings("unchecked")
		Collection<T> result = DummyInstanceCreator.createDummyInstance(target.getClass());

		String targetExpression = getExpression(target);
		String operationName,opaqueActionName;

		switch(operationType)
		{
			case ADD_ONE_LITERAL: 
				operationName = "including";
				opaqueActionName = "add_"+paramExpression+"_to_"+targetExpression;
			break;
			
			case ADD_ALL_LITERAL:
				operationName = "addAll";
				opaqueActionName = "addAll_"+paramExpression+"_to_"+targetExpression;
			break;
			
			case REMOVE_LITERAL:
				operationName = "excluding";
				opaqueActionName = "remove_"+paramExpression+"_from_"+targetExpression;
			break;
			
			case SELECT_ALL_LITERAL:
				operationName = "select";
				opaqueActionName = "selectAll_"+targetExpression;
			break;
			
			default:
				operationName = "";
				opaqueActionName = targetExpression+"->??("+paramExpression+")";
			break;
		}
		
		String expression = targetExpression + "->" + operationName + "(" + paramExpression +")";

		if(currentActivity != null)
		{
			createActivityElementsForCollectionOperation(
					opaqueActionName,
					targetExpression, 
					expression, 
					result, 
					typeClass
				);
			
			if(result != null)
				expression = getObjectIdentifier(result);
		}

		if(result!=null)
			InstanceManager.createLocalInstancesMapEntry(result, InstanceInformation.create(expression));
		
		return result;
	}
	
	/**
	 * 
	 * Creates the necessary activity elements (activity nodes and edges) for a select action.
	 * @param targetExpression The expression of the target collection.
	 * @param operationExpression The expression of the operation.
	 * @param result The result instance of the operation. (a ModelClass instance or a collection)
	 * @param typeClass The class of the result.
	 * @param operationType The type of operation. (e.g. selectAll, selectAny, remove, add, etc.)
	 *
	 * @author Adam Ancsin
	 */
	private static void createActivityElementsForCollectionOperation
		(String opaqueActionName, String targetExpression, String operationExpression, Object result, Class<?> typeClass)
	{
		String resultName=getObjectIdentifier(result);
		
		OpaqueAction opaqueAction=	(OpaqueAction)
				currentActivity.createOwnedNode(opaqueActionName,UMLPackage.Literals.OPAQUE_ACTION);
	
		opaqueAction.getBodies().add(operationExpression);
		
		createControlFlowBetweenActivityNodes(lastNode, opaqueAction);
	
		Type type=ModelImporter.importType(typeClass);
		
		Variable variable=createVariableForInstance(result,type);
		int lowerBound = variable.getLower();
		int upperBound = variable.getUpper();
		
		AddVariableValueAction setVarAction = createAddVarValAction(variable,"setVar_"+resultName);
		
		OutputPin outputPin=opaqueAction.createOutputValue(opaqueAction.getName()+"_output", type);
		outputPin.setLower(lowerBound);
		outputPin.setUpper(upperBound);
		
		InputPin inputPin=setVarAction.createValue(setVarAction.getName()+"_input",type);
		inputPin.setLower(lowerBound);
		inputPin.setUpper(upperBound);
		
		createObjectFlowBetweenActivityNodes(outputPin,inputPin);
		lastNode=setVarAction;
	}
	
	/**
	 * Imports a parameterized condition.
	 * @param condition The condition.
	 * @param parameter The parameter of the condition.
	 * @return The condition converted to an expression.
	 *
	 * @author Adam Ancsin
	 */
	private static <T extends ModelClass> String importParameterizedCondition(ParameterizedCondition<T> condition, T parameter)
	{
		String parameterTypeName = ModelImporter.importType(parameter.getClass()).getName();
		Activity currActivityBackup=currentActivity;
		currentActivity=null;
		ModelBool checkedCond=condition.check(parameter);
		String paramId = getObjectIdentifier(parameter);
		String simpleCondExpr = getConditionOrConstraintExpression(checkedCond);
		
		String ret = paramId +" : "+parameterTypeName+" | "+ simpleCondExpr;
		currentActivity=currActivityBackup;
		return ret;
	}
	
	/**
	 * Gets the type of the generic parameter of the specified collection.
	 * @param collection The specified collection.
	 * @return The specified collection's generic parameter type.
	 *
	 * @author Adam Ancsin
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
	
	/**
	 * Creates the dummy instance of the result of a query collection operation (e.g. "isEmpty", "contains", etc.) and
	 * creates a local instances map entry for it.
	 * If the currently imported method has an activity (it's not a transition guard), creates and sets the value of an
	 * activity variable associated with the result instance. 
	 * @param resultType The type class of the result.
	 * @param resultExpression The result expression.
	 * @return The created dummy instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	private static <T> T createAndAssignResultForQuery(Class<T> resultType, String resultExpression)
	{
		T result = DummyInstanceCreator.createDummyInstance(resultType);
		
		if(currentActivity != null)
			setVariableValue(result,resultExpression);
		else
			InstanceManager.createLocalInstancesMapEntry(result, InstanceInformation.createCalculated(resultExpression));
		
		return result;
	}
}
