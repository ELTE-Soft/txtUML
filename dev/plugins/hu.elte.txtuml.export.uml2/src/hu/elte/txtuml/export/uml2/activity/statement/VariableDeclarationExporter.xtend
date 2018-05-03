package hu.elte.txtuml.export.uml2.activity.statement

import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.uml2.uml.Action
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider

class VariableDeclarationExporter extends ActionExporter<VariableDeclarationStatement, Action> {
	
	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(VariableDeclarationStatement access) { factory.createSequenceNode }
	
	override exportContents(VariableDeclarationStatement source) {
		source.fragments.forEach[ 
			val variable = factory.createVariable
			val sourceTypeBinding = source.type.resolveBinding
			if(ElementTypeTeller.isCollection(sourceTypeBinding)) {
				variable.lower = MultiplicityProvider.getLowerBound(sourceTypeBinding);
				variable.upper = MultiplicityProvider.getUpperBound(sourceTypeBinding);
				variable.type = fetchType(sourceTypeBinding.typeArguments.get(0))
				
			} else {
				variable.type = fetchType(sourceTypeBinding)
			}
			
			val decl = it as VariableDeclarationFragment
			variable.name = decl.name.identifier
			storeVariable(variable)
			
			if (decl.initializer != null) {
				variable.write(exportExpression(decl.initializer))
			}
		]
	}
	
}