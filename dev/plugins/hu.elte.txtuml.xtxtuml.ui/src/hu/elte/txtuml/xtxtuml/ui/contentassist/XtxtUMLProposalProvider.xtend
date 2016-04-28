package hu.elte.txtuml.xtxtuml.ui.contentassist

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.jface.viewers.StyledString
import org.eclipse.xtext.Assignment
import org.eclipse.xtext.common.types.xtext.ui.ITypesProposalProvider
import org.eclipse.xtext.conversion.IValueConverter
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor

class XtxtUMLProposalProvider extends AbstractXtxtUMLProposalProvider {

	static val allowedPrimitives = #["boolean", "double", "int"]

	override completeJvmParameterizedTypeReference_Type(EObject model, Assignment assignment,
		ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		allowedPrimitives.forEach [
			acceptor.accept(createCompletionProposal(it, new StyledString(it), getImage(null), context));
		]

		if (model instanceof TUClass || model instanceof TUAttributeOrOperationDeclarationPrefix) {
			acceptor.accept(createCompletionProposal("void", new StyledString("void"), getImage(null), context));
		}
	}

	override protected completeJavaTypes(ContentAssistContext context, EReference reference, boolean forced,
		IValueConverter<String> valueConverter, ITypesProposalProvider.Filter filter,
		ICompletionProposalAcceptor acceptor) {
		// do nothing, the reference proposal creator will handle the case
	}

	override protected StyledString getStyledDisplayString(EObject element, String qualifiedName,
		String shortName) {
		val plainDisplayStringSegments = getDisplayString(element, qualifiedName, shortName).replace("$", ".").
			split("-");
		return if (plainDisplayStringSegments.size != 2) {
			if(!plainDisplayStringSegments.empty) new StyledString(plainDisplayStringSegments.get(0))
		} else {
			createStyledProposalText(plainDisplayStringSegments.get(0), plainDisplayStringSegments.get(1));
		}
	}

	def private createStyledProposalText(String name, String details) {
		new StyledString(name).append(new StyledString(
			"-" + details,
			StyledString::QUALIFIER_STYLER
		))
	}

}
