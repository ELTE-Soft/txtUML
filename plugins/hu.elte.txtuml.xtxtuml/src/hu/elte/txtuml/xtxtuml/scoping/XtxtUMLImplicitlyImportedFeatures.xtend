package hu.elte.txtuml.xtxtuml.scoping

import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures;

/*
 * ImplicitlyImportedFeatures is provisional
 */ 
class XtxtUMLImplicitlyImportedFeatures extends ImplicitlyImportedFeatures {
	
	override protected getStaticImportClasses() {
		(super.getStaticImportClasses() + #[
			hu.elte.txtuml.api.model.Action
		]).toList
	}
	
}