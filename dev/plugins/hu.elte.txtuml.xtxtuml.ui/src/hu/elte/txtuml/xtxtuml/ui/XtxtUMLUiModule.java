package hu.elte.txtuml.xtxtuml.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.builder.IXtextBuilderParticipant;
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.service.SingletonBinding;
import org.eclipse.xtext.ui.editor.contentassist.AbstractJavaBasedContentProposalProvider;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.xbase.ui.hover.XbaseDeclarativeHoverSignatureProvider;
import org.eclipse.xtext.xbase.ui.hover.XbaseHoverDocumentationProvider;
import org.eclipse.xtext.xbase.ui.quickfix.TypeNameGuesser;
import org.eclipse.xtext.xbase.ui.validation.XbaseUIValidator;

import hu.elte.txtuml.xtxtuml.ui.contentassist.XtxtUMLReferenceProposalCreator;
import hu.elte.txtuml.xtxtuml.ui.highlighting.XtxtUMLHighlightingCalculator;
import hu.elte.txtuml.xtxtuml.ui.highlighting.XtxtUMLHighlightingConfiguration;
import hu.elte.txtuml.xtxtuml.ui.hover.XtxtUMLDeclarativeHoverSignatureProvider;
import hu.elte.txtuml.xtxtuml.ui.hover.XtxtUMLHoverDocumentationProvider;
import hu.elte.txtuml.xtxtuml.ui.quickfix.XtxtUMLTypeNameGuesser;
import hu.elte.txtuml.xtxtuml.ui.validation.XtxtUMLBuilderParticipant;
import hu.elte.txtuml.xtxtuml.ui.validation.XtxtUMLUIValidator;

public class XtxtUMLUiModule extends hu.elte.txtuml.xtxtuml.ui.AbstractXtxtUMLUiModule {

	public XtxtUMLUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	@Override
	public Class<? extends ISemanticHighlightingCalculator> bindIdeSemanticHighlightingCalculator() {
		return XtxtUMLHighlightingCalculator.class;
	}

	public Class<? extends XbaseDeclarativeHoverSignatureProvider> bindXbaseDeclarativeHoverSignatureProvider() {
		return XtxtUMLDeclarativeHoverSignatureProvider.class;
	}

	public Class<? extends XbaseHoverDocumentationProvider> bindXbaseHoverDocumentationProvider() {
		return XtxtUMLHoverDocumentationProvider.class;
	}

	@Override
	public Class<? extends AbstractJavaBasedContentProposalProvider.ReferenceProposalCreator> bindAbstractJavaBasedContentProposalProvider$ReferenceProposalCreator() {
		return XtxtUMLReferenceProposalCreator.class;
	}

	public Class<? extends TypeNameGuesser> bindTypeNameGuesser() {
		return XtxtUMLTypeNameGuesser.class;
	}

	@Override
	public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
		return XtxtUMLHighlightingConfiguration.class;
	}

	@Override
	@SingletonBinding(eager = true)
	public Class<? extends XbaseUIValidator> bindXbaseUIValidator() {
		return XtxtUMLUIValidator.class;
	}

	@Override
	public Class<? extends IXtextBuilderParticipant> bindIXtextBuilderParticipant() {
		return XtxtUMLBuilderParticipant.class;
	}

}
