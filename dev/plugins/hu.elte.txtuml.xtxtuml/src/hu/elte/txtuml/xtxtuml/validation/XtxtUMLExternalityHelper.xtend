package hu.elte.txtuml.xtxtuml.validation;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExternality
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModifiers
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import java.util.HashMap
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.xbase.XBlockExpression

import static hu.elte.txtuml.xtxtuml.xtxtUML.TUExternality.*

/**
 * This class tells of any XtxtUML language element whether it is non-external,
 * external, or has an external body. For performance reasons, this class caches
 * the calculated externality for each language element it was given and all
 * their direct or non-direct containers.
 * <br/>
 * A language element is considered
 * <ul>
 * <li>to have an external body if it was declared as such,</li>
 * <li>external if
 * <ul>
 * <li>it was declared as such, or</li>
 * <li>one of its containers is external, or</li>
 * <li>the element is the body of an operation or constructor which is marked to
 * have an external body. (Note that because of the first rule, this results in all
 * parts of the block also being external.)</li>
 * </ul>
 * </li>
 * <li>non-external in all other cases.</li>
 * </ul>
 * Querying the externality of an {@link EObject} which is not an XtxtUML
 * language element does not yield an error, the object is simply considered
 * non-external (as obviously neither of the other two can be applied). 
 */
class XtxtUMLExternalityHelper {

	val Map<EObject, TUExternality> externalityMap = new HashMap

	def isNonExternal(EObject object) {
		return object.externality === NON_EXTERNAL
	}

	def isExternal(EObject object) {
		return object.externality === EXTERNAL
	}

	def isExternalBody(EObject object) {
		return object.externality === EXTERNAL_BODY
	}

	/**
	 * Returns the externality of the given object. The externality is
	 * calculated as stated in the description of this class.
	 * <br/>
	 * If the externality of the given object has been queried before, this
	 * method returns the cached value which was calculated at the first time.
	 */
	def TUExternality getExternality(EObject object) {
		val ret = externalityMap.get(object)

		if (ret == null) {
			object.calculateExternality
		} else {
			ret
		}
	}

	/**
	 * Calculates and caches the externality of the given
	 * object. The externality is calculated as stated in the description of
	 * this class.
	 * 
	 * @return the calculated externality of the given object
	 */
	private def calculateExternality(EObject object) {
		val sup = object.eContainer?.externality ?: NON_EXTERNAL
		val externality = switch (sup) {
			case EXTERNAL:
				EXTERNAL
			case EXTERNAL_BODY:
				if(object instanceof XBlockExpression) EXTERNAL else NON_EXTERNAL
			case NON_EXTERNAL: {
				var TUModifiers modifiers
				switch (object) {
					TUAttribute:
						modifiers = (object as TUAttribute).prefix.modifiers
					TUOperation:
						modifiers = (object as TUOperation).prefix.modifiers
					TUConstructor:
						modifiers = (object as TUConstructor).modifiers
				}
				modifiers?.externality ?: NON_EXTERNAL
			}
		}
		externalityMap.put(object, externality)
		return externality
	}

}
