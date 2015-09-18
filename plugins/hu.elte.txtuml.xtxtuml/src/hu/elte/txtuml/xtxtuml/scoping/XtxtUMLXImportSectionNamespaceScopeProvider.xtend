package hu.elte.txtuml.xtxtuml.scoping

import org.eclipse.xtext.xbase.scoping.XImportSectionNamespaceScopeProvider;
import org.eclipse.xtext.naming.QualifiedName;

class XtxtUMLXImportSectionNamespaceScopeProvider extends XImportSectionNamespaceScopeProvider {
	
	protected override getImplicitImports(boolean ignoreCase) {
		(super.getImplicitImports(ignoreCase) + #[
			doCreateImportNormalizer(QualifiedName::create("hu", "elte", "txtuml", "xtxtuml", "lib"), true, false)
		]).toList
	}
	
}