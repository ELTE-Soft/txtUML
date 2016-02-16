package hu.elte.txtuml.xtxtuml.ui.highlighting

import org.eclipse.xtext.xbase.ui.highlighting.XbaseHighlightingCalculator
import org.eclipse.xtext.xbase.XAbstractFeatureCall
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor

class XtxtUMLHighlightingCalculator extends XbaseHighlightingCalculator {
	
	override computeFeatureCallHighlighting(XAbstractFeatureCall featureCall, IHighlightedPositionAcceptor acceptor) {
		if (featureCall.isExtension) {
			return;
		}
		
		super.computeFeatureCallHighlighting(featureCall, acceptor);
	}
	
}