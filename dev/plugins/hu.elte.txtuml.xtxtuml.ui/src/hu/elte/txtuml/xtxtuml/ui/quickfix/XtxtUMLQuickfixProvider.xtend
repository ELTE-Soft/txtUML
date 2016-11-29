package hu.elte.txtuml.xtxtuml.ui.quickfix;

import com.google.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.model.IXtextDocument
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.xtext.validation.Issue
import org.eclipse.xtext.xbase.ui.quickfix.XbaseQuickfixProvider

class XtxtUMLQuickfixProvider extends XbaseQuickfixProvider {

	@Inject XtxtUMLLinkingIssueQuickfixProvider xtxtUMLLinkingIssueQuickfixProvider;

	/**
	 * Overrides the super behavior to add only XtxtUML import proposals.
	 */
	override protected createLinkingIssueQuickfixes(Issue issue, IssueResolutionAcceptor issueResolutionAcceptor,
		IXtextDocument xtextDocument, XtextResource state, EObject target, EReference reference) {
		xtxtUMLLinkingIssueQuickfixProvider.addQuickfixes(issue, issueResolutionAcceptor, xtextDocument, state, target,
			reference);
	}

}
