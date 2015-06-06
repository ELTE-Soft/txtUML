package hu.elte.txtuml.export.uml2.transform.aspects;

import hu.elte.txtuml.api.Collection;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.blocks.ParameterizedCondition;
import hu.elte.txtuml.export.uml2.transform.CollectionOperationImporter;

import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * This aspect contains advices for importing operations of Collections
 * (e.g. Collection.selectAny, Collection.selectAll, etc.) inside method bodies.
 * 
 * @author Adam Ancsin
 *
 */
public privileged aspect CollectionOperationImporterAspect extends AbstractImporterAspect
{
	/**
	 * This advice imports a selectAny call on a Collection if called from a txtUML method body
	 * during model import.
	 * 
	 * @param target The target collection.
	 * @return The dummy instance of the result of the call.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	Object around(Collection target):target(target) && call(* selectAny()) && isActive()
	{
		return CollectionOperationImporter.importSelectAny(target);
	}

	/**
	 * This advice imports a selectAll call on a Collection if called from a txtUML method body
	 * during model import.
	 * 
	 * @param target The target collection.
	 * @param condition The condition of the selection.
	 * @return The dummy instance of the result of the call.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	Object around(Collection target, ParameterizedCondition condition):
		target(target) && 
		call(* selectAll(ParameterizedCondition)) &&
		isActive() &&
		args(condition)
	{
		return CollectionOperationImporter.importSelectAll(target, condition);
	}
	
	/**
	 * This advice imports the "isEmpty" query of the specified target Collection if called from
	 * a txtUML method body during model import.
	 * @param target The target collection.
	 * @return The dummy ModelBool instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	ModelBool around(Collection target):target(target) && call(ModelBool isEmpty()) && isActive()
	{
		return CollectionOperationImporter.importIsEmpty(target);
	}
	
	/**
	 * This advice imports the "contains" query of the specified target Collection if called from
	 * a txtUML method body during model import.
	 * @param target The target collection.
	 * @param object The ModelClass object to check.
	 * @return The dummy ModelBool instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	ModelBool around(Collection target, ModelClass object):
		target(target) &&
		call(ModelBool contains(ModelClass))
		&& isActive()
		&& args(object)
	{
		return CollectionOperationImporter.importContains(target, object);
	}
	
	/**
	 * This advice imports the "count" query of the specified target Collection if called from
	 * a txtUML method body during model import.
	 * @param target The target collection.
	 * @return The dummy ModelInt instance of the result.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	ModelInt around(Collection target):target(target) && call(ModelInt size()) && isActive()
	{
		return CollectionOperationImporter.importCount(target);
	}
	
	/**
	 * This advice imports the "add" operation of the specified target Collection if called from
	 * a txtUML method body during model import.
	 * @param target The target collection.
	 * @param object The ModelClass object to be added to the Collection.
	 * @return The dummy instance of the result collection.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	Collection around(Collection target, ModelClass object):
		target(target) &&
		call(Collection add(ModelClass))
		&& isActive()
		&& args(object)
	{
		return CollectionOperationImporter.importAdd(target, object);
	}
	
	/**
	 * This advice imports the "addAll" operation of the specified target Collection if called from
	 * a txtUML method body during model import.
	 * @param target The target collection.
	 * @param object The collection containing the elements to be added to the target collection..
	 * @return The dummy instance of the result collection.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	Collection around(Collection target, Collection objects):
		target(target) &&
		call(Collection addAll(Collection))
		&& isActive()
		&& args(objects)
	{
		return CollectionOperationImporter.importAddAll(target, objects);
	}
	
	/**
	 * This advice imports the "remove" operation of the specified target Collection if called from
	 * a txtUML method body during model import.
	 * @param target The target collection.
	 * @param object The ModelClass object to be removed from the Collection.
	 * @return The dummy instance of the result collection.
	 *
	 * @author Adam Ancsin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SuppressAjWarnings
	Collection around(Collection target, ModelClass object):
		target(target) &&
		call(Collection remove(ModelClass))
		&& isActive()
		&& args(object)
	{
		return CollectionOperationImporter.importRemove(target, object);
	}
}
