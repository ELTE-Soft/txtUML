package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLExporter;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.utils.Dialogs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

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
		
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		
		try {
			progressService.runInUI(
					progressService,
				      new IRunnableWithProgress() {
				         public void run(IProgressMonitor monitor) throws InterruptedException {
				        	monitor.beginTask("Visualization", 100);
				        	
				        	TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName, projectName,
				        			folder, txtUMLModelName, txtUMLLayout, parentClassLoader);
				     		
				        	monitor.subTask("Exporting txtUML Model to UML2 model...");
				        	try{
				        		exporter.exportTxtUMLModelToUML2();
				        		monitor.worked(10);
				    		} catch (Exception e) {
				    			Dialogs.errorMsgb("txtUML export Error",
				    					e.getClass() + ":\n" + e.getMessage(), e);
				    			monitor.done();
				    			throw new InterruptedException();
				    		}
				     		
				     		
				     		monitor.subTask("Generating txtUML layout description...");
				     		TxtUMLLayoutDescriptor layoutDesriptor = null;
				     		try{
				     			layoutDesriptor = exporter.exportTxtUMLLayout();
				     			monitor.worked(5);
				     		}catch(Exception e){
				     			Dialogs.errorMsgb("txtUML layout export Error",
				    					e.getClass() + ":\n" + e.getMessage(), e);
				    			monitor.done();
				    			throw new InterruptedException();
				     		}
				     		
				     		try{
					     		PapyrusVisualizer pv = exporter.createVisualizer(layoutDesriptor);
					            pv.run(new SubProgressMonitor(monitor,85));
				     		}catch(Exception e){
				     			Dialogs.errorMsgb("txtUML visualization Error", e.getClass()
				    					+ ":\n" + e.getMessage(), e);
				     			monitor.done();
				     			throw new InterruptedException();
				     		}
				         }
				      },
				      ResourcesPlugin.getWorkspace().getRoot());
			return true;
		} catch (InvocationTargetException | InterruptedException e) {
			return false;
		}
	}

}
