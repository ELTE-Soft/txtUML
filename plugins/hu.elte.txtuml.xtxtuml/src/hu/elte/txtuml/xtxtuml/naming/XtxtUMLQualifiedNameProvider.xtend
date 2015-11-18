package hu.elte.txtuml.xtxtuml.naming

import hu.elte.txtuml.xtxtuml.xtxtUML.TUModel
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.scoping.XbaseQualifiedNameProvider

class XtxtUMLQualifiedNameProvider extends XbaseQualifiedNameProvider {

	/**
	 * Provides lower-case qualified names for packages generated from models.
	 */
	override QualifiedName getFullyQualifiedName(EObject obj) {
		val name = super.getFullyQualifiedName(obj)
		if (obj instanceof TUModel) {
			return name.toLowerCase
		}
		return name
	}
}
