package hu.elte.txtuml.xtxtuml.ui.highlighting;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.documentation.impl.MultiLineCommentDocumentationProvider
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor
import org.eclipse.xtext.xbase.XAbstractFeatureCall
import org.eclipse.xtext.xbase.ui.highlighting.XbaseHighlightingCalculator

import static hu.elte.txtuml.xtxtuml.ui.highlighting.XtxtUMLHighlightingConfiguration.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*
import static org.eclipse.xtext.common.types.TypesPackage.Literals.*
import static org.eclipse.xtext.xbase.XbasePackage.Literals.*
import static org.eclipse.xtext.xbase.ui.highlighting.XbaseHighlightingConfiguration.*

class XtxtUMLHighlightingCalculator extends XbaseHighlightingCalculator {

	@Inject extension MultiLineCommentDocumentationProvider;

	override highlightElement(EObject object, IHighlightedPositionAcceptor acceptor) {
		highlightDocumentationComment(object, acceptor);

		switch (object) {
			TUAttribute:
				highlightFeature(acceptor, object, TU_ATTRIBUTE__NAME, FIELD)
			TUSignalAttribute:
				highlightFeature(acceptor, object, TU_SIGNAL_ATTRIBUTE__NAME, FIELD)
			TUConstructor:
				object.parameters.forEach [
					highlightFeature(acceptor, it, JVM_FORMAL_PARAMETER__NAME, FORMAL_PARAMETER)
				]
			TUOperation:
				object.parameters.forEach [
					highlightFeature(acceptor, it, JVM_FORMAL_PARAMETER__NAME, FORMAL_PARAMETER)
				]
			TUMultiplicity: {
				val textRegion = NodeModelUtils.findActualNodeFor(object).textRegion;
				acceptor.addPosition(textRegion.offset, textRegion.length, MULTIPLICITY);
			}
			default:
				super.highlightElement(object, acceptor)
		}

		return false;
	}

	def highlightDocumentationComment(EObject object, IHighlightedPositionAcceptor acceptor) {
		for (docNode : object.documentationNodes) {
			if (docNode.text.startsWith("/**")) {
				val textRegion = docNode.textRegion;
				acceptor.addPosition(textRegion.offset, textRegion.length, DOCUMENTATION_COMMENT);
			}
		}
	}

	override computeFeatureCallHighlighting(XAbstractFeatureCall featureCall, IHighlightedPositionAcceptor acceptor) {
		if (featureCall.isExtension) {
			return;
		} else if (featureCall.feature instanceof JvmFormalParameter) {
			highlightFeature(acceptor, featureCall, XABSTRACT_FEATURE_CALL__FEATURE, FORMAL_PARAMETER);
		}

		super.computeFeatureCallHighlighting(featureCall, acceptor);
	}

}
