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

class XtxtUMLExternalityHelper {

	val Map<EObject, TUExternality> externality = new HashMap

	def isNonExternal(EObject object) {
		return object.externality === NON_EXTERNAL
	}

	def isExternal(EObject object) {
		return object.externality === EXTERNAL
	}

	def isExternalBody(EObject object) {
		return object.externality === EXTERNAL_BODY
	}

	def TUExternality getExternality(EObject object) {
		val ret = externality.get(object)

		if (ret == null) {
			object.calculateExternality
		} else {
			ret
		}
	}

	private def calculateExternality(EObject object) {
		val sup = object.eContainer?.externality ?: NON_EXTERNAL
		switch (sup) {
			case EXTERNAL:
				return EXTERNAL
			case EXTERNAL_BODY:
				return if(object instanceof XBlockExpression) EXTERNAL else NON_EXTERNAL
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
				return modifiers?.externality ?: NON_EXTERNAL
			}
		}
	}

}
