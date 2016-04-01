package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.LinkAction
import org.eclipse.uml2.uml.ReadLinkAction

class AssocNavigationExporter extends ActionExporter<MethodInvocation, ReadLinkAction> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (API_CLASSES.contains(access.resolveMethodBinding.declaringClass.qualifiedName) &&
			access.resolveMethodBinding.name == "assoc")
			factory.createReadLinkAction
	}

	override exportContents(MethodInvocation source) {
		val classArg = source.arguments.get(0)
		val otherSide = (classArg as TypeLiteral).type.resolveBinding
		val assocType = otherSide.declaringClass
		val thisSide = assocType.declaredTypes.findFirst[it != otherSide]
		result.addEnd(fetchElement(assocType) as Association, thisSide, source.expression)
		result.name = '''«result.endData.get(0).value.name»->«otherSide.name»'''
	}

	def void addEnd(LinkAction action, Association assoc, ITypeBinding binding, Expression expression) {
		val endData = action.createEndData
		endData.end = assoc.ownedEnds.findFirst[name == binding.name]
		val baseExpr = exportExpression(expression)[storeNode]
		baseExpr.store.result.objectFlow(endData.value)
		endData.value.name = baseExpr.name
	}

}