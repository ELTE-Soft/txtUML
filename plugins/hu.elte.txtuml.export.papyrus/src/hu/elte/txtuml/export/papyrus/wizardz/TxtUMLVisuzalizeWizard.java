package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.ProjectUtils;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.uml2.UML2;
import hu.elte.txtuml.export.utils.ClassLoaderProvider;
import hu.elte.txtuml.export.utils.Dialogs;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.lang.Diagram;

import java.net.URLClassLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Wizard for visualization of txtUML models
 *
 * @author András Dobreff
 */
public class TxtUMLVisuzalizeWizard extends Wizard {

	private VisualizeTxtUMLPage selectTxtUmlPage;

	/**
	 * The Constructor
	 */
	public TxtUMLVisuzalizeWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getWindowTitle()
	 */
	@Override
	public String getWindowTitle() {
		return "Create Papyrus Model from txtUML Model";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		selectTxtUmlPage = new VisualizeTxtUMLPage();
		addPage(selectTxtUmlPage);
	}

	/**
	 * Calls the {@link hu.elte.txtuml.export.uml2.UML2 txtUML UML2 Export} and
	 * then starts the visualization.
	 */
	@Override
	public boolean performFinish() {
		PreferencesManager preferncesManager = new PreferencesManager();
		String txtUMLModelName = selectTxtUmlPage.getTxtUmlModelClass();
		String txtUMLLayout = selectTxtUmlPage.getTxtUmlLayout();
		String txtUMLProjectName = selectTxtUmlPage.getTxtUmlProject();
		String folder = preferncesManager
				.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);

		String[] modellnamesplit = txtUMLModelName.split("\\.");
		String[] layoutnamesplit = txtUMLLayout.split("\\.");
		String projectName = modellnamesplit[modellnamesplit.length-1]+"_"+
										layoutnamesplit[layoutnamesplit.length-1];
		
		
		TxtUMLLayoutDescriptor layoutDesriptor;
		
		
		ClassLoader parentClassLoader = hu.elte.txtuml.export.uml2.UML2.class.getClassLoader();
		preferncesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT,
				txtUMLProjectName);
		preferncesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL,
				txtUMLModelName);
		preferncesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT,
				txtUMLLayout);

		try (URLClassLoader loader = ClassLoaderProvider
					.getClassLoaderForProject(txtUMLProjectName, parentClassLoader)){
			Class<?> txtUMLModelClass = loader.loadClass(txtUMLModelName);
			String uri = URI.createPlatformResourceURI(
					txtUMLProjectName + "/" + folder, false).toString();
			UML2.exportModel(txtUMLModelClass, uri);
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML export Error",
					e.getClass() + ":\n" + e.getMessage(), e);
			return false;
		}

		try (URLClassLoader loader = ClassLoaderProvider
					.getClassLoaderForProject(txtUMLProjectName, parentClassLoader)){
			Class<?> txtUMLLayoutClass = loader.loadClass(txtUMLLayout);  
            @SuppressWarnings("unchecked")
			DiagramExporter exporter= DiagramExporter.create((Class<? extends Diagram>) txtUMLLayoutClass); 
            DiagramExportationReport report = exporter.export(); 
            layoutDesriptor = new TxtUMLLayoutDescriptor(txtUMLModelName, report);
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML export Error",
					e.getClass() + ":\n" + e.getMessage(), e);
			return false;
		}
		
		try {
			URI umlFileURI = URI.createFileURI(txtUMLProjectName + "/" + folder
					+ "/" + txtUMLModelName + ".uml");
			URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);
			IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(new Path(UmlFileResURI.toFileString()));

			URI diFileURI = URI.createFileURI(projectName + "/"
					+ txtUMLModelName + ".di");
			URI diFileResURI = CommonPlugin.resolve(diFileURI);
			IFile diFile = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(new Path(diFileResURI.toFileString()));

			IEditorInput input = new FileEditorInput(diFile);

			IEditorPart editor = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.findEditor(input);
			if (editor != null) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().closeEditor(editor, false);
				
			}

			ProjectUtils.deleteProject(projectName);
			PapyrusVisualizer ma = new PapyrusVisualizer(projectName, txtUMLModelName,
					UmlFile.getRawLocationURI().toString(), layoutDesriptor);
			ma.run();
		} catch (Exception e) {
			Dialogs.errorMsgb("txtUML visualization Error", e.getClass()
					+ ":\n" + e.getMessage(), e);
			return false;
		}
		return true;
	}

}
