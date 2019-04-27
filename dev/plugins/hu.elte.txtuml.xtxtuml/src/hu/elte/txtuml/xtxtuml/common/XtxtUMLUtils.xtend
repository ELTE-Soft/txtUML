package hu.elte.txtuml.xtxtuml.common

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDataType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.IResourceDescriptions
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassOrDataTypeOrSignal

public class XtxtUMLUtils {

	@Inject IResourceDescriptions index;
	@Inject extension IQualifiedNameProvider;

	/**
	 * Returns the IEObjectDescription of the specified <code>objectDescription</code>'s
	 * container, if exists. Returns <code>null</code> otherwise.
	 */
	def getEContainerDescription(IEObjectDescription objectDescription) {
		val objectUriFragment = objectDescription.EObjectURI.fragment;
		val containerUriFragment = objectUriFragment.substring(0, objectUriFragment.lastIndexOf('/'));
		val resourceDescription = index.getResourceDescription(objectDescription.EObjectURI.trimFragment);

		return resourceDescription.exportedObjects.findFirst [
			EObjectURI.fragment == containerUriFragment
		];
	}

	def ownsPort(TUClass clazz, EObject port) {
		if (clazz == null || port == null) {
			return true;
		}

		val portEnclosingClassName = port.eContainer?.fullyQualifiedName;
		val classOwnsPort = [TUClassOrDataTypeOrSignal klass | (klass as TUClass).fullyQualifiedName == portEnclosingClassName];
		return clazz.travelTypeHierarchy(classOwnsPort);
	}
	
	/**
	 * Travels the type hierarchy of <code>signal</code>, <code>class</code> or <code>datatype</code> 
	 * upwards (inclusive), until a signal, class or data type which satisfies <code>predicate</code>
	 * is found, or the hierarchy ends, or a cycle in the hierarchy is found.
	 *
	 * @return
	 * <ul>
	 * 	<li><code>true</code> if an appropriate signal, class or data type has been found,</li>
	 * 	<li><code>false</code> if no appropriate signal, class or data type has been found,</li>
	 * 	<li><code>null</code> if a cycle has been found during the traversal.</li>
	 * </ul>
	 */
	def travelTypeHierarchy(TUClassOrDataTypeOrSignal type, (TUClassOrDataTypeOrSignal)=>Boolean predicate) {
		// TODO technically a duplicate of travelClassHierarchy, should be eliminated ASAP
		val visitedTypeNames = newHashSet;
		var currentType = type;

		while (currentType != null) {
			if (visitedTypeNames.contains(currentType.fullyQualifiedName)) {
				return null;
			}

			if (predicate.apply(currentType)) {
				return true;
			}

			visitedTypeNames.add(currentType.fullyQualifiedName);
			currentType = currentType.superType;
		}

		return false;
	}
	
	def dispatch TUClassOrDataTypeOrSignal superType(TUSignal it){
		superSignal
	}
	
	def dispatch TUClassOrDataTypeOrSignal superType(TUDataType it){
		superDataType
	}
	
	def dispatch TUClassOrDataTypeOrSignal superType(TUClass it){
		superClass
	}
	

}
