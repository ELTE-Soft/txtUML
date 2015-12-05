package hu.elte.txtuml.xtxtuml.scoping

import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.scoping.NestedTypeAwareImportNormalizerWithDotSeparator

class XtxtUMLImportNormalizer extends NestedTypeAwareImportNormalizerWithDotSeparator {

	new(QualifiedName importedNamespace, boolean wildcard, boolean ignoreCase) {
		super(importedNamespace, wildcard, ignoreCase);
	}

	override protected resolveWildcard(QualifiedName relativeName) {
		getImportedNamespacePrefix().append(relativeName)
	}
}
