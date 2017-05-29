package hu.elte.txtuml.export.papyrus.wizardz;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;

import hu.elte.txtuml.export.diagrams.common.arrange.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.diagrams.common.wizards.AbstractVisualizeWizard;
import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;

public class PapyrusVisualizeWizard extends AbstractVisualizeWizard {

	@Override
	protected void visualize(TxtUMLLayoutDescriptor layoutDescriptor, String genFolder, IProgressMonitor monitor)
			throws Exception {
		PapyrusVisualizer pv = createVisualizer(layoutDescriptor, genFolder);
		pv.run(SubMonitor.convert(monitor, 85));

	}

	/**
	 * Creates a Visualizer with the correct settings
	 * 
	 * @param layoutDescriptor
	 * @return The Visualizer
	 */
	public PapyrusVisualizer createVisualizer(TxtUMLLayoutDescriptor descriptor, String outputFolder) {
		String projectName = descriptor.projectName;
		String modelName = descriptor.modelName;
		URI umlFileURI = URI
				.createFileURI(projectName + File.separator + outputFolder + File.separator + modelName + ".uml");
		URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
		IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(UmlFileResURI.toFileString()));

		PapyrusVisualizer pv = new PapyrusVisualizer(projectName, outputFolder + File.separator + modelName,
				UmlFile.getRawLocationURI().toString(), descriptor);
		return pv;
	}

}
