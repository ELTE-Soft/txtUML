package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.expression.MethodCallExporter
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.ConstructorInvocation
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.SuperConstructorInvocation
import org.eclipse.uml2.uml.SequenceNode
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.api.model.ModelClass

class BlockExporter extends ControlExporter<Block, SequenceNode> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(Block access) { factory.createSequenceNode }

	override exportContents(Block source) {
		val method = source.parent
		// add implicit ctor call if necessary
		switch method {
			MethodDeclaration case method.constructor &&
				!ElementTypeTeller.isSignal(method.resolveBinding.declaringClass) &&
				(source.statements.isEmpty || !isCtorCall(source.statements.get(0) as Statement)): {
				val superClass = method.resolveBinding.declaringClass.superclass
				if (superClass.qualifiedName != ModelClass.canonicalName) {
					val ctor = superClass.declaredMethods.filter[isConstructor].findFirst[parameterTypes.isEmpty]
					result.executableNodes += new MethodCallExporter(this).createCall(ctor, null, #[])
				}

			}
		}
		result.executableNodes += source.statements.map[exportStatement]
	}

	def boolean isCtorCall(Statement stmt) {
		switch stmt {
			ConstructorInvocation: true
			SuperConstructorInvocation: true
			default: false
		}
	}

}