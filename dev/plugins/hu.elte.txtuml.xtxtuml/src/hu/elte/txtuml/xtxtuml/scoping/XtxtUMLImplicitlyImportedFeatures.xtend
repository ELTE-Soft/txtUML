package hu.elte.txtuml.xtxtuml.scoping;

import hu.elte.txtuml.api.model.Action
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures

/**
 * Extends the default behavior to implicitly import the static methods
 * of {@link Action} as well.
 */
class XtxtUMLImplicitlyImportedFeatures extends ImplicitlyImportedFeatures {

	override protected getStaticImportClasses() {
		#[Action]
	}

}
