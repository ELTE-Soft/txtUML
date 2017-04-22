package hu.elte.txtuml.xtxtuml.ui.hover;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.ui.labeling.XtxtUMLLabelProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.xbase.ui.hover.XbaseDeclarativeHoverSignatureProvider

class XtxtUMLDeclarativeHoverSignatureProvider extends XbaseDeclarativeHoverSignatureProvider {

	@Inject XtxtUMLLabelProvider labelProvider;

	/**
	 * Overrides the default implementation to provide Java-like operation signatures.
	 */
	override getSignature(EObject obj) {
		if(obj instanceof TUOperation) labelProvider.text(obj, true).toString else labelProvider.getText(obj)
	}

	/**
	 * Overrides the default implementation to use simple signatures.
	 */
	override getDerivedOrSourceSignature(EObject obj) {
		getSignature(obj);
	}

}
