package hu.elte.txtuml.export.papyrus.wizardz;

import hu.elte.txtuml.eclipseutils.Dialogs;
import hu.elte.txtuml.export.ExportUtils;
import hu.elte.txtuml.export.papyrus.PapyrusVisualizer;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLExporter;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

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
		
		preferncesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_PROJECT,
				txtUMLProjectName);
		preferncesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_MODEL,
				txtUMLModelName);
		preferncesManager.setValue(
				PreferencesManager.TXTUML_VISUALIZE_TXTUML_LAYOUT,
				txtUMLLayout);
		
		try {
			IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
			
			progressService.runInUI(
					progressService,
				      new IRunnableWithProgress() {
				         public void run(IProgressMonitor monitor) throws InterruptedException {
				        	monitor.beginTask("Visualization", 100);
				        	
				        	TxtUMLExporter exporter = new TxtUMLExporter(txtUMLProjectName, projectName,
				        			folder, txtUMLModelName, txtUMLLayout);
				     		
				        	monitor.subTask("Exporting txtUML Model to UML2 model...");
				        	try{
				        		ExportUtils.exportTxtUMLModelToUML2(txtUMLProjectName, txtUMLModelName,
				        				txtUMLProjectName+"/"+folder);
				        		monitor.worked(10);
				    		} catch (Exception e) {
				    			Dialogs.errorMsgb("txtUML export Error",
				    					e.getClass() + ":"+System.lineSeparator() + e.getMessage(), e);
				    			monitor.done();
				    			throw new InterruptedException();
				    		}
				     		
				     		
				     		monitor.subTask("Generating txtUML layout description...");
				     		TxtUMLLayoutDescriptor layoutDesriptor = null;
				     		try{
				     			layoutDesriptor = exporter.exportTxtUMLLayout();
				     			
				     			if(layoutDesriptor.report.getWarningCount() != 0){
				     				StringBuilder warnings = new StringBuilder("Warnings:"+System.lineSeparator());
				     				warnings.append(String.join(System.lineSeparator(), layoutDesriptor.report.getWarnings()));
				     				warnings.append(System.lineSeparator()+System.lineSeparator()+"Do you want to continue?");
				     				
				     				if(!Dialogs.WarningConfirm("Warnings about layout description", warnings.toString())){
					     				throw new InterruptedException();
					     			}
				     			}
				     			
				     			monitor.worked(5);
				     		}catch(Exception e){
				     			if(e instanceof InterruptedException){
				     				throw (InterruptedException) e;
				     			}else{
					     			Dialogs.errorMsgb("txtUML layout export Error",
					    					e.getClass() + ":"+System.lineSeparator() + e.getMessage(), e);
					    			monitor.done();
					    			throw new InterruptedException();
				     			}
				     		}
				     		
				     		try{
					     		PapyrusVisualizer pv = exporter.createVisualizer(layoutDesriptor);
					            pv.run(new SubProgressMonitor(monitor,85));
				     		}catch(Exception e){
				     			Dialogs.errorMsgb("txtUML visualization Error", e.getClass()
				    					+ ":"+System.lineSeparator() + e.getMessage(), e);
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
