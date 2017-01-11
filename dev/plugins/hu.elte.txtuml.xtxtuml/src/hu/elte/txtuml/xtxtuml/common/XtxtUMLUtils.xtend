package hu.elte.txtuml.xtxtuml.common

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.IResourceDescriptions

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
		val classOwnsPort = [TUClass klass | klass.fullyQualifiedName == portEnclosingClassName];
		return clazz.travelClassHierarchy(classOwnsPort);
	}

	/**
	 * Travels the class hierarchy of <code>clazz</code> upwards (inclusive), until a class
	 * which satisfies <code>predicate</code> is found, or the hierarchy ends, or a cycle in
	 * the hierarchy is found.
	 * 
	 * @return
	 * <ul>
	 * 	<li><code>true</code> if an appropriate class has been found,</li>
	 * 	<li><code>false</code> if no appropriate class has been found,</li>
	 * 	<li><code>null</code> if a cycle has been found during the traversal.</li>
	 * </ul> 
	 */
	def travelClassHierarchy(TUClass clazz, (TUClass)=>Boolean predicate) {
		if (predicate.apply(clazz)) {
			return true;
		}

		val visitedClassNames = newHashSet;
		visitedClassNames.add(clazz.fullyQualifiedName);

		var currentClass = clazz.superClass;
		while (currentClass != null) {
			if (visitedClassNames.contains(currentClass.fullyQualifiedName)) {
				return null;
			}

			if (predicate.apply(currentClass)) {
				return true;
			}

			visitedClassNames.add(currentClass.fullyQualifiedName);
			currentClass = currentClass.superClass;
		}

		return false;
	}

}
