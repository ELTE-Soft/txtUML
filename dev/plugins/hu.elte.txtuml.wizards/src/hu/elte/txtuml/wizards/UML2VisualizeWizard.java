package hu.elte.txtuml.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import hu.elte.txtuml.export.uml2.ExportMode;
import hu.elte.txtuml.export.uml2.TxtUMLToUML2;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.TxtUMLExporter;
import hu.elte.txtuml.layout.export.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.eclipse.Dialogs;
import hu.elte.txtuml.utils.eclipse.preferences.PreferencesManager;

/**
 * Abstract class for {@link TxtUMLVisualizeWizard} which first exports UML2 then uses that for the visualization.
 */
public abstract class UML2VisualizeWizard extends TxtUMLVisualizeWizard {

	public UML2VisualizeWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	protected boolean exportDiagrams(Map<Pair<String, String>, List<IType>> layoutConfigs) {
		
		final String generatedFolderName = getGeneratedFolderName();
		
		for(Map.Entry<Pair<String, String>, List<IType>> layout : layoutConfigs.entrySet()){
			final String txtUMLModelName = layout.getKey().getFirst();
			final String txtUMLProjectName = layout.getKey().getSecond();
			Map<String, String> layouts = new HashMap<String, String>();
			layout.getValue().forEach(
					type -> layouts.put(type.getFullyQualifiedName(), type.getJavaProject().getElementName()));


			try {
				IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
				progressService.runInUI(progressService, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InterruptedException {
						monitor.beginTask("Visualization", 100);

						monitor.subTask("Exporting txtUML Model to UML2 model...");
						try {
							TxtUMLToUML2.exportModel(txtUMLProjectName, txtUMLModelName,
									txtUMLProjectName + "/" + generatedFolderName, ExportMode.ErrorHandlingNoActions,
									generatedFolderName);
							monitor.worked(10);
						} catch (Exception e) {
							Dialogs.errorMsgb("txtUML export Error", "Error occured during the UML2 exportation.", e);
							monitor.done();
							throw new InterruptedException();
						}						
						
						monitor.subTask("Generating txtUML layout description...");
						TxtUMLLayoutDescriptor layoutDescriptor = null;
						try {
							layoutDescriptor = makeLayoutDescriptor(layout);
							monitor.worked(20);
						} catch (Exception e) {
							if (e instanceof InterruptedException) {
								throw (InterruptedException) e;
							} else {
								Dialogs.errorMsgb("txtUML layout export Error",
										"Error occured during the diagram layout interpretation.", e);
								monitor.done();
								throw new InterruptedException();
							}
						}	
						
						monitor.subTask("Exporting diagrams for visualization...");
						try {
							exportDiagram(layoutDescriptor, monitor);
						} catch (Exception e) {
							Dialogs.errorMsgb("txtUML visualization Error",
									"Error occured during the visualization process.", e);
							monitor.done();
							throw new InterruptedException();
						}
						monitor.done();
					}
				}, ResourcesPlugin.getWorkspace().getRoot());
			} catch (InvocationTargetException | InterruptedException e) {
				Logger.sys.error(e.getMessage());
				return false;
			}
			try {
				refreshGeneratedFolder(txtUMLProjectName);
			} catch (CoreException e) {
				Logger.sys.error(e.getMessage());
				Dialogs.errorMsgb("txtUML visualization Error",
						"Error occured during refreshing generated folder.", e);
			}
		}
		return true;
	}
	
	
	/**
	 * Export a single model's diagrams 
	 * @param layoutDescriptor the model's layout descriptor
	 * @param monitor for filling the progress bar
	 * @throws Exception
	 */
	protected abstract void exportDiagram(TxtUMLLayoutDescriptor layoutDescriptor, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Returns the layout of a single model 
	 * @param layout the model to export
	 * @return the layout description
	 * @throws Exception
	 */
	protected TxtUMLLayoutDescriptor makeLayoutDescriptor(Map.Entry<Pair<String, String>, List<IType>> layout) throws Exception{
		TxtUMLLayoutDescriptor res = null;
		final String txtUMLModelName = layout.getKey().getFirst();
		final String txtUMLProjectName = layout.getKey().getSecond();
		final String generatedFolderName = getGeneratedFolderName();
		Map<String, String> layouts = new HashMap<String, String>();
		layout.getValue().forEach(
				type -> layouts.put(type.getFullyQualifiedName(), type.getJavaProject().getElementName()));

		TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName,
				txtUMLModelName, layouts);
		
		try {
			res = exporter.exportTxtUMLLayout();
		} catch (Exception e) {
			throw e;
		}

		List<String> warnings = new LinkedList<String>();
		for (DiagramExportationReport report : res.getReports()) {
			warnings.addAll(report.getWarnings());
		}

		res.mappingFolder = generatedFolderName;
		res.projectName = txtUMLProjectName;

		if (warnings.size() != 0) {
			StringBuilder warningMessages = new StringBuilder(
					"Warnings:" + System.lineSeparator() + System.lineSeparator() + "- ");
			warningMessages.append(
					String.join(System.lineSeparator() + System.lineSeparator() + "- ", warnings));
			warningMessages.append(
					System.lineSeparator() + System.lineSeparator() + "Do you want to continue?");

			if (!Dialogs.WarningConfirm("Warnings about layout description",
					warningMessages.toString())) {
				throw new InterruptedException();
			}
		}
		
		return res;
	}
	
	protected static String getGeneratedFolderName(){
		return PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);		
	}
	
	protected static void refreshGeneratedFolder(String txtUMLProjectName) throws CoreException{
		ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProjectName).getFolder(getGeneratedFolderName()).refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	
}