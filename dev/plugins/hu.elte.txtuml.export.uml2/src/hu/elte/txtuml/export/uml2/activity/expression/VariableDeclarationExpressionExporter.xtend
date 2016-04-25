package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.VariableDeclarationExpression
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.uml2.uml.SequenceNode

class VariableDeclarationExpressionExporter extends ActionExporter<VariableDeclarationExpression,SequenceNode> {
	
	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(VariableDeclarationExpression access) { factory.createSequenceNode }
	
	override exportContents(VariableDeclarationExpression source) {
		source.fragments.forEach[ 
			val variable = factory.createVariable
			variable.type = fetchType(source.type.resolveBinding)
			val decl = it as VariableDeclarationFragment
			variable.name = decl.name.identifier
			storeVariable(variable)
			
			if (decl.initializer != null) {
				variable.write(exportExpression(decl.initializer))
			}
		]
	}
	
	
	
}