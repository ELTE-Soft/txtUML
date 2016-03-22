package hu.elte.txtuml.export.uml2.restructured.activity

import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.uml2.uml.ValueSpecificationAction

class StringLiteralExporter extends ActionExporter<StringLiteral, ValueSpecificationAction> {
	
	new(hu.elte.txtuml.export.uml2.restructured.Exporter<?, ?, ?> parent) {
		super(parent)
	}
	
	override create(StringLiteral access) { factory.createValueSpecificationAction }
	
	override exportContents(StringLiteral source) {
		val literal = factory.createLiteralString
		literal.type = stringType
		literal.value = source.literalValue
		
		result.value = literal
		result.createResult(source + "_result", stringType)
	}
	
}