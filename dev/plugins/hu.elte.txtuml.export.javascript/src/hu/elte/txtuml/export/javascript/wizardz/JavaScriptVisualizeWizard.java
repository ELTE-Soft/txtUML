package hu.elte.txtuml.export.javascript.wizardz;

import org.eclipse.core.runtime.IProgressMonitor;

import hu.elte.txtuml.export.diagrams.common.arrange.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.diagrams.common.wizards.AbstractVisualizeWizard;
import hu.elte.txtuml.export.javascript.Exporter;

/**
 * Wizard for visualization of txtUML models
 */
public class JavaScriptVisualizeWizard extends AbstractVisualizeWizard {

	@Override
	protected void visualize(TxtUMLLayoutDescriptor layoutDescriptor, String genFolder, IProgressMonitor monitor)
			throws Exception {
		Exporter ex = new Exporter(layoutDescriptor, layoutDescriptor.modelName);
		ex.export();
	}
}
