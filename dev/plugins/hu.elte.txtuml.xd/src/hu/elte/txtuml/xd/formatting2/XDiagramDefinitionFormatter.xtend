/*
 * generated by Xtext 2.10.0
 */
package hu.elte.txtuml.xd.formatting2

import com.google.inject.Inject
import hu.elte.txtuml.xd.services.XDiagramDefinitionGrammarAccess
import hu.elte.txtuml.xd.xDiagramDefinition.XDArgumentExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDArgumentExpressionList
import hu.elte.txtuml.xd.xDiagramDefinition.XDBinaryIdentifierInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDBinaryListInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDDiagramSignature
import hu.elte.txtuml.xd.xDiagramDefinition.XDDiamondInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDGroupInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDModel
import hu.elte.txtuml.xd.xDiagramDefinition.XDPackageDeclaration
import hu.elte.txtuml.xd.xDiagramDefinition.XDPhantomInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDPriorityInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDTypeExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDTypeExpressionList
import hu.elte.txtuml.xd.xDiagramDefinition.XDUnaryListInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDUnaryNumberInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDWrappedArgumentExpressionList
import hu.elte.txtuml.xd.xDiagramDefinition.XDWrappedNumericExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDWrappedTypeExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDWrappedTypeExpressionList
import org.eclipse.xtext.formatting2.IFormattableDocument
import org.eclipse.xtext.xbase.formatting2.XbaseFormatter
import hu.elte.txtuml.xd.xDiagramDefinition.XDNumericExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDDiagram

class XDiagramDefinitionFormatter extends XbaseFormatter {

	@Inject extension XDiagramDefinitionGrammarAccess

	def dispatch void format(XDDiagramSignature it, extension IFormattableDocument document) {
		regionFor().keyword("for").prepend[oneSpace].append[oneSpace];
		regionFor().keyword("class-diagram").prepend[noSpace].append[oneSpace];
		regionFor().keyword("state-machine-diagram").prepend[noSpace].append[oneSpace];
	}

	def dispatch void format(XDPackageDeclaration it, extension IFormattableDocument document) {
		regionFor().keyword(";").prepend[noSpace].append[newLines = 2];
	}

	def dispatch void format(XDModel it, extension IFormattableDocument document) {
		format(package, document);
		format(imports, document);
		format(diagram, document);
	}
	
	def dispatch void format(XDDiagram it, extension IFormattableDocument document){
		regionFor().keyword("{").prepend[oneSpace].append[newLine];
		interior(regionFor.keyword('{'), regionFor.keyword('}'), [indent])
		regionFor().keyword("}").prepend[newLine].append[newLine];
		format(signature, document);
		instructions.forEach[instruction|format(instruction, document)];
	}

	def dispatch void format(XDInstruction it, extension IFormattableDocument document) {
		regionFor().keyword(";").prepend[noSpace].append[newLine];
		format(wrapped, document);
	}

	def dispatch void format(XDBinaryIdentifierInstruction it, extension IFormattableDocument document) {
		regionFor.keyword(op).prepend[oneSpace].append[oneSpace];
		format(^val);
		format(of);
	}

	def dispatch void format(XDBinaryListInstruction it, extension IFormattableDocument document) {
		regionFor.keyword(op).prepend[oneSpace].append[oneSpace];
		if (linkEnd != null) {
			regionFor.keyword(linkEnd).prepend[oneSpace].append[oneSpace];
		}
		format(^val);
		format(of);
	}

	def dispatch void format(XDPhantomInstruction it, extension IFormattableDocument document) {
		regionFor.keyword(op).prepend[oneSpace].append[oneSpace];
	}

	def dispatch void format(XDUnaryListInstruction it, extension IFormattableDocument document) {
		regionFor.keyword(op).prepend[oneSpace].append[oneSpace];
		format(^val);
	}

	def dispatch void format(XDGroupInstruction it, extension IFormattableDocument document) {
		regionFor.keyword(op).prepend[oneSpace].append[oneSpace];
		format(^val);
		if (align != null) {
			regionFor.keyword(align).prepend[oneSpace].append[noSpace];
		}
	}

	def dispatch void format(XDUnaryNumberInstruction it, extension IFormattableDocument document) {
		regionFor.keyword(op).prepend[oneSpace].append[oneSpace];
		format(^val);
	}

	def dispatch void format(XDDiamondInstruction it, extension IFormattableDocument document) {
		regionFor.keyword(op).prepend[oneSpace].append[oneSpace];
		format(args);
	}

	def dispatch void format(XDPriorityInstruction it, extension IFormattableDocument document) {
		regionFor.keyword(op).prepend[oneSpace].append[oneSpace];
		format(^val);
		format(prior);
	}

	def dispatch void format(XDWrappedTypeExpressionList it, extension IFormattableDocument document) {
		regionFor.keyword("{").prepend[oneSpace].append[oneSpace];
		regionFor.keyword("}").prepend[oneSpace]
		format(wrapped);
	}

	def dispatch void format(XDWrappedTypeExpression it, extension IFormattableDocument document) {
		regionFor.keyword("{").prepend[oneSpace].append[oneSpace];
		regionFor.keyword("}").prepend[oneSpace]
		format(wrapped);
	}

	def dispatch void format(XDWrappedArgumentExpressionList it, extension IFormattableDocument document) {
		regionFor.keyword("{").prepend[oneSpace].append[oneSpace];
		regionFor.keyword("}").prepend[oneSpace]
		format(wrapped);
	}

	def dispatch void format(XDTypeExpressionList it, extension IFormattableDocument document) {
		regionFor.keywords(",").forEach[prepend[noSpace].append[oneSpace]];
		it.expressions.forEach[exp|format(exp, document)]
	}

	def dispatch void format(XDTypeExpression it, extension IFormattableDocument document) {
	}

	def dispatch void format(XDArgumentExpressionList it, extension IFormattableDocument document) {
		regionFor.keywords(",").forEach[prepend[noSpace].append[oneSpace]];
		it.expressions.forEach[exp|format(exp, document)]
	}

	def dispatch void format(XDArgumentExpression it, extension IFormattableDocument document) {
		regionFor.keyword(":").prepend[noSpace].append[oneSpace];
	}
	
	def dispatch void format(XDWrappedNumericExpression it, extension IFormattableDocument document){
		regionFor.keyword("{").prepend[oneSpace].append[oneSpace];
		regionFor.keyword("}").prepend[oneSpace]
		format(wrapped);
	}
	
	def dispatch void format(XDNumericExpression it, extension IFormattableDocument document){
		if (perc != null) {
			regionFor.keyword(perc).prepend[noSpace];
		}
	}
}
