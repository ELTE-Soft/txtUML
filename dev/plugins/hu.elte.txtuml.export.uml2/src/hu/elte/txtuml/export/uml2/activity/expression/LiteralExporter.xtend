package hu.elte.txtuml.export.uml2.activity.expression

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.export.uml2.activity.ActionExporter
import org.eclipse.jdt.core.dom.BooleanLiteral
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.uml2.uml.ValueSpecificationAction
import org.eclipse.jdt.core.dom.CharacterLiteral
import org.eclipse.jdt.core.dom.NullLiteral
import org.eclipse.jdt.core.dom.NumberLiteral
import org.eclipse.uml2.uml.ValueSpecification

abstract class LiteralExporter<T> extends ActionExporter<T, ValueSpecificationAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(T access) { factory.createValueSpecificationAction }

	override exportContents(T source) {
		val literal = createValueSpec(source)
		result.value = literal
		result.name = literal.name
		result.createResult(literal.name + "_result", literal.type)
	}

	def ValueSpecification createValueSpec(T source)

}

class StringLiteralExporter extends LiteralExporter<StringLiteral> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override createValueSpec(StringLiteral source) {
		val literal = factory.createLiteralString
		literal.type = stringType
		literal.value = source.literalValue
		literal.name = '"' + literal.value + '"'
		return literal
	}
}

class BooleanLiteralExporter extends LiteralExporter<BooleanLiteral> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override createValueSpec(BooleanLiteral source) {
		val literal = factory.createLiteralBoolean
		literal.type = booleanType
		literal.value = source.booleanValue
		literal.name = literal.value.toString
		return literal
	}
}

class CharacterLiteralExporter extends LiteralExporter<CharacterLiteral> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override createValueSpec(CharacterLiteral source) {
		val literal = factory.createLiteralString
		literal.type = stringType
		literal.value = Character.toString(source.charValue)
		literal.name = source.charValue.toString
		return literal
	}
}

class NumberLiteralExporter extends LiteralExporter<NumberLiteral> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override createValueSpec(NumberLiteral source) {
		val typeName = source.resolveTypeBinding().getQualifiedName();
		switch typeName {
			case "int",
			case "byte",
			case "short": {
				val lit = factory.createLiteralInteger
				lit.type = integerType
				lit.value = Integer.parseInt(source.token)
				lit.name = lit.value.toString
				return lit
			}
			case "float",
			case "double": {
				val lit = factory.createLiteralReal
				lit.type = realType
				lit.value = Double.parseDouble(source.token)
				lit.name = lit.value.toString
				return lit
			}
		}
	}

	def createIntegerLiteral(int value) {
		val lit = factory.createLiteralInteger
		lit.type = integerType
		lit.value = value
		lit.name = value + ""
		val specAct = factory.createValueSpecificationAction
		specAct.value = lit
		specAct.name = lit.name
		specAct.createResult(lit.name, lit.type)
		storeNode(specAct)
		return specAct
	}

}

class NullLiteralExporter extends LiteralExporter<NullLiteral> {

	new(Exporter<?, ?, ?> parent) {
		super(parent)
	}

	override createValueSpec(NullLiteral source) {
		val literal = factory.createLiteralNull
		literal.type = collectionType
		literal.name = "null"
		return literal
	}
}