package hu.elte.txtuml.xtxtuml.formatting2;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUBindExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUCollectionModifiers
import hu.elte.txtuml.xtxtuml.xtxtUML.TUCollectionType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUCreateObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEnumeration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecutionAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecutionBlock
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUInterface
import hu.elte.txtuml.xtxtuml.xtxtUML.TULogExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModifiers
import hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPortMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUReception
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStartObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.xtext.formatting2.IFormattableDocument
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XForLoopExpression
import org.eclipse.xtext.xbase.XSwitchExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.formatting2.XbaseFormatter

import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

/**
 * Defines formatting rules for XtxtUML elements.
 * <p>
 * <i>Rule:</i> The formatting of an element should only affect its inside. For example, it should not prepend any
 * white space before the first part of the element or append any white space after the last part. In
 * cases where this rule cannot be followed, it has to be stated explicitly.
 */
class XtxtUMLFormatter extends XbaseFormatter {

	def dispatch void format(TUModelDeclaration it, extension IFormattableDocument document) {
		regionFor.keyword('model-package').prepend[noSpace].append[oneSpace];
		regionFor.keyword('as').surround[oneSpace];
		regionFor.feature(TU_MODEL_DECLARATION__SEMI_COLON).prepend[noSpace].append[newLine];
	}

	def dispatch void format(TUFile it, extension IFormattableDocument document) {
		regionFor.keyword('package').prepend[noSpace];
		regionFor.feature(TU_FILE__NAME).prepend[oneSpace].append[noSpace];
		regionFor.keyword(';').append[newLines = 2];

		format(importSection, document);

		for (element : elements) {
			element.append[newLines = 2];
			format(element, document);
		}
	}

	def dispatch void format(TUSignal it, extension IFormattableDocument document) {
		formatBlockElement(it, document, regionFor.keyword('signal'), attributes, false);
		regionFor.keyword('extends').surround[oneSpace];
	}

	def dispatch void format(TUClass it, extension IFormattableDocument document) {
		formatBlockElement(it, document, regionFor.keyword('class'), members, true);
		regionFor.keyword('extends').surround[oneSpace];
	}

	def dispatch void format(TUExecution it, extension IFormattableDocument document) {
		formatBlockElement(it, document, regionFor.keyword('execution'), elements, true);
	}

	def dispatch void format(TUExecutionAttribute it, extension IFormattableDocument document) {
		regionFor.feature(TU_EXECUTION_ATTRIBUTE__NAME).prepend[oneSpace];
		regionFor.keyword('=').surround[oneSpace];
		regionFor.keyword(';').prepend[noSpace];

		format(type, document);
		format(initExpression, document);
	}

	def dispatch void format(TUExecutionBlock it, extension IFormattableDocument document) {
		regionFor.feature(TU_EXECUTION_BLOCK__TYPE).append[oneSpace];
		format(body, document);
	}

	def dispatch void format(TUEnumeration it, extension IFormattableDocument document) {
		formatBlockElement(it, document, regionFor.keyword('enum'), literals, false, []);

		regionFor.keywords(',').forEach[prepend[noSpace].append[newLine]];
		if (!literals.empty) {
			regionFor.keyword('}').prepend[newLine];
		}
	}

	def dispatch void format(TUAssociation it, extension IFormattableDocument document) {
		formatBlockElement(it, document,
			regionFor.keyword(if(it instanceof TUComposition) 'composition' else 'association'), ends, false);
	}
	
	def dispatch void format(TUCollectionType it, extension IFormattableDocument document) {
		format(modifiers, document);
		regionFor.keyword('collection').append[oneSpace];
		regionFor.keyword('of').append[oneSpace];
		format(multiplicity, document);
		type.prepend[oneSpace];
		regionFor.keyword('as').surround[oneSpace];
		regionFor.keyword(';').prepend[noSpace];
	}
	
	def dispatch void format(TUCollectionModifiers it, extension IFormattableDocument document) {
		regionFor.feature(TU_COLLECTION_MODIFIERS__ORDERED).append[oneSpace];
		regionFor.feature(TU_COLLECTION_MODIFIERS__UNIQUE).append[oneSpace];
	}

	def dispatch void format(TUInterface it, extension IFormattableDocument document) {
		formatBlockElement(it, document, regionFor.keyword('interface'), receptions, false);
	}

	def dispatch void format(TUReception it, extension IFormattableDocument document) {
		formatSimpleMember(it, document, TU_RECEPTION__SIGNAL);
	}

	def dispatch void format(TUConnector it, extension IFormattableDocument document) {
		formatBlockElement(it, document, regionFor.keyword(if(delegation) 'delegation' else 'connector'), ends, false);
	}

	def dispatch void format(TUConnectorEnd it, extension IFormattableDocument document) {
		regionFor.keyword('->').surround[noSpace];
		regionFor.feature(TU_CONNECTIVE_END__NAME).prepend[oneSpace].append[noSpace];
	}

	def dispatch void format(TUSignalAttribute it, extension IFormattableDocument document) {
		regionFor.feature(TU_SIGNAL_ATTRIBUTE__VISIBILITY).append[oneSpace];
		regionFor.feature(TU_SIGNAL_ATTRIBUTE__NAME).prepend[oneSpace].append[noSpace];
		format(type, document);
	}

	def dispatch void format(TUOperation it, extension IFormattableDocument document) {
		regionFor.feature(TU_OPERATION__NAME).prepend[oneSpace];
		regionFor.keyword('(').surround[noSpace];
		regionFor.keyword(')').prepend[noSpace].append[oneSpace];
		regionFor.keywords(',').forEach[prepend[noSpace].append[oneSpace]];

		format(body, document);
		format(prefix, document);
		for (parameter : parameters) {
			format(parameter, document);
		}
	}

	/**
	 * This formatting rule appends one space after the {@link TUModifiers} element unless it
	 * is empty (no part is defined).
	 */
	def dispatch void format(TUModifiers it, extension IFormattableDocument document) {
		regionFor.feature(TU_MODIFIERS__STATIC).append[oneSpace];
		regionFor.feature(TU_MODIFIERS__EXTERNALITY).append[oneSpace];
		regionFor.feature(TU_MODIFIERS__VISIBILITY).append[oneSpace];
	}

	def dispatch void format(TUConstructor it, extension IFormattableDocument document) {
		format(modifiers, document); // empty or ends with one space
		regionFor.keyword('(').surround[noSpace];
		regionFor.keyword(')').prepend[noSpace].append[oneSpace];
		regionFor.keywords(',').forEach[prepend[noSpace].append[oneSpace]];

		format(body, document);
		for (parameter : parameters) {
			format(parameter, document);
		}
	}

	def dispatch void format(TUAttributeOrOperationDeclarationPrefix it, extension IFormattableDocument document) {
		format(modifiers, document); // empty or ends with one space 
		format(type, document);
	}

	def dispatch void format(TUState it, extension IFormattableDocument document) {
		formatBlockElement(it, document, regionFor.feature(TU_STATE__TYPE), members, false);
	}

	def dispatch void format(TUEntryOrExitActivity it, extension IFormattableDocument document) {
		formatUnnamedBlockElement(it, document, body as XBlockExpression);
	}

	def dispatch void format(TUTransition it, extension IFormattableDocument document) {
		formatBlockElement(it, document, regionFor.keyword('transition'), members, false);
	}

	def dispatch void format(TUTransitionTrigger it, extension IFormattableDocument document) {
		formatSimpleMember(it, document, TU_TRANSITION_TRIGGER__TRIGGER);
	}

	def dispatch void format(TUTransitionVertex it, extension IFormattableDocument document) {
		formatSimpleMember(it, document, TU_TRANSITION_VERTEX__VERTEX);
	}

	def dispatch void format(TUTransitionEffect it, extension IFormattableDocument document) {
		formatUnnamedBlockElement(it, document, body as XBlockExpression);
	}

	def dispatch void format(TUTransitionGuard it, extension IFormattableDocument document) {
		regionFor.keyword('(').surround[oneSpace];
		expression.append[oneSpace];
		regionFor.keyword(';').prepend[noSpace];

		format(expression, document);
	}

	def dispatch void format(TUTransitionPort it, extension IFormattableDocument document) {
		formatSimpleMember(it, document, TU_TRANSITION_PORT__PORT);
	}

	def dispatch void format(TUPort it, extension IFormattableDocument document) {
		regionFor.keyword('behavior').append[oneSpace];
		formatBlockElement(it, document, regionFor.keyword('port'), members, false);
	}

	def dispatch void format(TUPortMember it, extension IFormattableDocument document) {
		formatSimpleMember(it, document, TU_PORT_MEMBER__INTERFACE);
	}

	def dispatch void format(TUAssociationEnd it, extension IFormattableDocument document) {
		regionFor.feature(TU_ASSOCIATION_END__VISIBILITY).append[oneSpace];
		regionFor.keyword('hidden').append[oneSpace];

		collection.multiplicity.append[oneSpace];

		regionFor.keyword('container').append[oneSpace];
		regionFor.feature(TU_CONNECTIVE_END__NAME).prepend[oneSpace].append[noSpace];

		format(collection.multiplicity, document);
	}

	def dispatch void format(TUMultiplicity it, extension IFormattableDocument document) {
		regionFor.keyword('..').surround[noSpace];
	}

	def dispatch void format(TUSendSignalExpression it, extension IFormattableDocument document) {
		signal.surround[oneSpace];
		target.prepend[oneSpace];

		format(signal, document);
		format(target, document);
	}

	def dispatch void format(TUStartObjectExpression it, extension IFormattableDocument document) {
		object.prepend[oneSpace];
		format(object, document);
	}

	def dispatch void format(TUDeleteObjectExpression it, extension IFormattableDocument document) {
		object.prepend[oneSpace];
		format(object, document);
	}

	def dispatch void format(TULogExpression it, extension IFormattableDocument document) {
		message.prepend[oneSpace];
		format(message, document);
	}

	def dispatch void format(TUBindExpression it, extension IFormattableDocument document) {
		regionFor.feature(TU_BIND_EXPRESSION__TYPE).append[oneSpace];
		regionFor.keyword(',').prepend[noSpace].append[oneSpace];
		regionFor.keyword('via').surround[oneSpace];
		regionFor.keywords('as').forEach[surround[oneSpace]];

		format(leftParticipant, document);
		format(rightParticipant, document);
	}

	def dispatch void format(TUCreateObjectExpression it, extension IFormattableDocument document) {
		regionFor.keyword('as').surround[oneSpace];
		super._format(it, document);
	}

	override dispatch void format(XBlockExpression it, extension IFormattableDocument document) {
		val open = regionFor.keyword('{');

		if (expressions.empty && !open.nextHiddenRegion.containsComment) {
			open.append[noSpace];
		} else {
			super._format(it, document); // generated _format is used to prevent infinite recursion
		}
	}

	override dispatch void format(XForLoopExpression it, extension IFormattableDocument document) {
		format(declaredParam, document);
		super._format(it, document); // generated _format is used to prevent infinite recursion
	}

	override dispatch void format(XSwitchExpression it, extension IFormattableDocument document) {
		^switch.surround[noSpace];
		super._format(it, document); // generated _format is used to prevent infinite recursion
	}

	override dispatch void format(XVariableDeclaration it, extension IFormattableDocument document) {
		regionFor.keyword(';').prepend[noSpace];
		super._format(it, document); // generated _format is used to prevent infinite recursion
	}

	def dispatch void format(TUAttribute it, extension IFormattableDocument document) {
		regionFor.feature(TU_ATTRIBUTE__NAME).prepend[oneSpace];
		regionFor.keyword('=').surround[oneSpace];
		regionFor.keyword(';').prepend[noSpace];

		format(prefix, document);
		format(initExpression, document);
	}

	def dispatch void format(TUClassPropertyAccessExpression it, extension IFormattableDocument document) {
		regionFor.feature(TU_CLASS_PROPERTY_ACCESS_EXPRESSION__ARROW).surround[noSpace];
		regionFor.feature(TU_CLASS_PROPERTY_ACCESS_EXPRESSION__RIGHT).surround[noSpace];

		format(left, document);
	}

	/**
	 * Can be used to format a block element according to the following format:
	 * <pre>
	 *     «typeKeyword» «name» {
	 *         «members»
	 *     }
	 * </pre> if <code>members</code> is not empty and
	 * <pre>
	 *     «typeKeyword» «name» {}
	 * </pre> otherwise. The parameter <code>isSpacious</code> indicates whether
	 * empty lines should be placed around each member.
	 */
	def private formatBlockElement(EObject it, extension IFormattableDocument document, ISemanticRegion typeKeyword,
		EList<? extends EObject> members, boolean isSpacious) {

		formatBlockElement(it, document, typeKeyword, members, isSpacious, [
			append[newLines = if(isSpacious) 2 else 1];
			format(it, document); // don't omit 'it'
		])
	}

	/**
	 * Can be used to format a block element according to the following format:
	 * <pre>
	 *     «typeKeyword» «name» {
	 *         «members»
	 *     }
	 * </pre> if <code>members</code> is not empty and
	 * <pre>
	 *     «typeKeyword» «name» {}
	 * </pre> otherwise. The parameter <code>isSpacious</code> indicates whether
	 * empty lines should be placed around each member, while
	 * <code>formatMember</code> is applied to all specified members to format
	 * their interior.
	 */
	def private <T> formatBlockElement(EObject it, extension IFormattableDocument document, ISemanticRegion typeKeyword,
		EList<T> members, boolean isSpacious, (T)=>void formatMember) {
		typeKeyword.append[oneSpace];

		val open = regionFor.keyword('{');
		open.prepend[oneSpace];

		val delimiterLineCount = if(isSpacious) 2 else 1;
		if (open == null || members.empty && !open.nextHiddenRegion.containsComment) {
			open.append[noSpace]; // null is not a problem here
		} else {
			open.append[newLines = delimiterLineCount];
			interior(open, regionFor.keyword('}'), [indent]);
		}

		regionFor.keyword(';').prepend[noSpace];

		members.forEach(formatMember)
	}

	/**
	 * Can be used to format a simple element according to the following format:
	 * <pre>
	 *     «typeKeyword» «reference»;
	 * </pre>
	 */
	def private formatSimpleMember(EObject it, extension IFormattableDocument document,
		EStructuralFeature mainFeature) {
		regionFor.feature(mainFeature).prepend[oneSpace].append[noSpace];
	}

	/**
	 * Can be used to format an unnamed block element according to the following format:
	 * <pre>
	 *     «typeKeyword» {
	 *         «members»
	 *     }
	 * </pre>
	 */
	def private formatUnnamedBlockElement(EObject it, extension IFormattableDocument document, XBlockExpression body) {
		body.regionFor.keyword('{').prepend[oneSpace];
		format(body, document);
	}

}
