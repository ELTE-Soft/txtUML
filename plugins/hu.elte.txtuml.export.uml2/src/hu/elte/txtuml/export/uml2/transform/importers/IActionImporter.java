package hu.elte.txtuml.export.uml2.transform.importers;

import org.eclipse.jdt.core.dom.MethodInvocation;

public interface IActionImporter {
	void importFromMethodInvocation(MethodInvocation methodInvocation);
}
