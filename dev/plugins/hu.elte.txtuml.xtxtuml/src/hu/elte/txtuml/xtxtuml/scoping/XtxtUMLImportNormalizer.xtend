package hu.elte.txtuml.xtxtuml.scoping;

import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.scoping.NestedTypeAwareImportNormalizerWithDotSeparator

class XtxtUMLImportNormalizer extends NestedTypeAwareImportNormalizerWithDotSeparator {

	new(QualifiedName importedNamespace, boolean wildcard, boolean ignoreCase) {
		super(importedNamespace, wildcard, ignoreCase);
	}

	/**
	 * Overrides the default behavior such that it handles references
	 * to nested classes more permissively.
	 * See <a href=https://github.com/ELTE-Soft/txtUML/pull/89>#89</a>.
	 */
	override protected resolveWildcard(QualifiedName relativeName) {
		getImportedNamespacePrefix().append(relativeName)
	}

}
