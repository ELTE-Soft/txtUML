package hu.elte.txtuml.xtxtuml.validation

import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExternality

class XtxtUMLModifierValidator extends XtxtUMLConnectorValidator {

	@Check
	def checkStaticIsNotUsedOnConstructor(TUConstructor const) {
		if (const.modifiers.static) {
			error('''Constructors cannot be static''',
				const.modifiers, TU_MODIFIERS__STATIC, CONSTRUCTOR_IS_STATIC);
		}		
	}

	@Check
	def checkExternalBodyIsNotUsedOnAttribute(TUAttribute attr) {
		if (attr.prefix.modifiers.externality === TUExternality.EXTERNAL_BODY) {
			error('''Attributes cannot be marked to have an external body''',
				attr.prefix.modifiers, TU_MODIFIERS__EXTERNALITY, ATTRIBUTE_HAS_EXTERNAL_BODY);
		}		
	}

	@Check
	def checkStaticIsNotUsedOnAttribute(TUAttribute attr) {
		val modifiers = attr.prefix.modifiers
		if (modifiers.static && modifiers.externality !== TUExternality.EXTERNAL) {
			error('''Only external attributes can be static''',
				modifiers, TU_MODIFIERS__STATIC, ATTRIBUTE_IS_STATIC);
		}
	}

}
