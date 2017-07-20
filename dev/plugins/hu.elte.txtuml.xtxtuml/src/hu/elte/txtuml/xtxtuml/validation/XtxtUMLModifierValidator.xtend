package hu.elte.txtuml.xtxtuml.validation

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExternality
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLModifierValidator extends XtxtUMLConnectorValidator {

	@Check
	def checkStaticIsNotUsedOnConstructor(TUConstructor ctor) {
		if (ctor.modifiers.static) {
			error('''Illegal modifier on constructor «ctor.name»(«ctor.parameters.typeNames.join(", ")»)''' +
				" – constructors cannot be static", ctor.modifiers, TU_MODIFIERS__STATIC, STATIC_CONSTRUCTOR);
		}
	}

	@Check
	def checkExternalBodyIsNotUsedOnAttribute(TUAttribute attr) {
		if (attr.prefix.modifiers.externality === TUExternality.EXTERNAL_BODY) {
			error("Illegal modifier on attribute " + attr.classQualifiedName +
				" – attributes cannot be marked to have an external body", attr.prefix.modifiers,
				TU_MODIFIERS__EXTERNALITY, EXTERNAL_BODY_ON_ATTRIBUTE);
		}
	}

	@Check
	def checkStaticIsNotUsedOnAttribute(TUAttribute attr) {
		val modifiers = attr.prefix.modifiers
		if (modifiers.static && modifiers.externality !== TUExternality.EXTERNAL) {
			error("Illegal modifier on attribute " + attr.classQualifiedName +
				" – only external attributes can be static", modifiers, TU_MODIFIERS__STATIC, STATIC_ATTRIBUTE);
		}
	}

}
