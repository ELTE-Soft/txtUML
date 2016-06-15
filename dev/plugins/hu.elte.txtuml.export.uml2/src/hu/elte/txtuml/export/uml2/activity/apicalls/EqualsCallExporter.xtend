package hu.elte.txtuml.export.uml2.activity.apicalls

import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import hu.elte.txtuml.export.uml2.activity.expression.EqualityExporter
import com.google.common.base.Objects
import org.eclipse.uml2.uml.TestIdentityAction

class EqualsCallExporter extends ActionExporter<MethodInvocation, TestIdentityAction> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		val isEquals = access.resolveMethodBinding.declaringClass.qualifiedName == Object.canonicalName &&
			access.name.identifier == "equals"
		val googleEqual = access.resolveMethodBinding.declaringClass.qualifiedName == Objects.canonicalName &&
			access.name.identifier == "equal"
		if (isEquals || googleEqual)
			factory.createTestIdentityAction
	}

	override exportContents(MethodInvocation source) {
		val firstArg = if (source.name.identifier == "equals") source.expression else source.arguments.get(0) as Expression
		val secondArg = if (source.name.identifier == "equals") source.arguments.get(0) as Expression else source.arguments.get(1) as Expression
		val exportLeft = exportExpression(firstArg)
		val exportRight = exportExpression(secondArg)
		new EqualityExporter(this).finishIdentityTest(result, exportLeft, exportRight)
	}

}