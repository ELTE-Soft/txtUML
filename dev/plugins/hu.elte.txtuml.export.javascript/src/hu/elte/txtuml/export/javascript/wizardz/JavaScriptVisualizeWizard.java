package hu.elte.txtuml.export.javascript.wizardz;

import org.eclipse.core.runtime.IProgressMonitor;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.export.diagrams.common.arrange.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.diagrams.common.wizards.UML2VisualizeWizard;
import hu.elte.txtuml.export.diagrams.common.wizards.VisualizeTxtUMLPage;
import hu.elte.txtuml.export.javascript.Exporter;

/**
 * Wizard for visualization of txtUML models
 */
public class JavaScriptVisualizeWizard extends UML2VisualizeWizard {

	@Override
	protected void visualize(TxtUMLLayoutDescriptor layoutDescriptor, String genFolder, IProgressMonitor monitor)
			throws Exception {
		Exporter ex = new Exporter(layoutDescriptor, layoutDescriptor.modelName);
		ex.export();
	}

	@Override
	public void addPages() {
		selectTxtUmlPage = new VisualizeTxtUMLPage(false, true, StateMachineDiagram.class, ClassDiagram.class, CompositeDiagram.class);
		addPage(selectTxtUmlPage);
	}

	@Override
	public String getWindowTitle() {
		return "Visualize JointJS diagrams from txtUML models";
	}
}
