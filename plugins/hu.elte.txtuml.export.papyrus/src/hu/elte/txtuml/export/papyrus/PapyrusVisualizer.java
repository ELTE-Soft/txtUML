package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.export.papyrus.api.EditorOpener;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.AbstractPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.DefaultPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.TxtUMLPapyrusModelManager;
import hu.elte.txtuml.export.utils.Dialogs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelMultiException;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @param <ViewPrototype>
 * @see IWorkbenchWindowActionDelegate
 */
public class PapyrusVisualizer {
	private String Projectname;
	private String Modelname;
	private String SourceUMLPath;
	private AbstractPapyrusModelManager papyrusModelManager;
	private Object layoutDescriptor;
	
	/**
	 * The constructor.
	 * @param projectName - The name of the project in which the Papyrus Model will be created.
	 * @param modelName - The Name of the Papyrus Model
	 * @param sourceUMLpath - Sourcepath of the Eclipse UML2 model 
	 * @param layoutDescripton  - A descriptor file which adds constraints to the layout and diagram generation
	 */
	public PapyrusVisualizer(String projectName, String modelName,
			String sourceUMLpath, Object layoutDescripton) {
		this.Projectname = projectName;
		this.Modelname = modelName;
		this.SourceUMLPath = sourceUMLpath;
		this.layoutDescriptor = layoutDescripton;
	}
	
	/**
	 * @param projectName
	 * @param modelName
	 * @param sourceUMLpath
	 */
	public PapyrusVisualizer(String projectName, String modelName, String sourceUMLpath){
		this(projectName, modelName, sourceUMLpath, null);
	}
	
	/**
	 * Executes the visualization process. 
	 * Creates the project (if not exists) and sets up the Papyrus Model
	 * @param monitor 
	 * @return 
	 */
	public IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Visualization", 100);
		
		monitor.subTask("Creating new Papyrus project...");
		IProject project = ProjectUtils.createProject(Projectname);
		ProjectUtils.openProject(project);
		monitor.worked(20);
		
		createPapyrusProject(new SubProgressMonitor(monitor, 80));
		return Status.OK_STATUS;
	}
	
	/**
	 * Creates a Papyrus Model in the opened project or handles the Exceptions 
	 * with a messagebox.
	 */
	private void createPapyrusProject(IProgressMonitor monitor) {
		try {	
			createAndOpenPapyrusModel(monitor);
		} catch (Exception e) {
			Dialogs.errorMsgb("Error", e.toString(), e);
		}
	}	
	
	/**
	 * Creates the Papyrus Model and fills the diagrams.
	 * If the Model already exists, then loads it.
	 * @throws ModelMultiException - If the loading of existing model fails
	 * @throws ServiceException 
	 */
	private void createAndOpenPapyrusModel(IProgressMonitor monitor) throws ModelMultiException, ServiceException{
		monitor.beginTask("Generating Papyrus Model", 100);
		PapyrusModelCreator papyrusModelCreator = new PapyrusModelCreator(Projectname+"/"+Modelname);
		papyrusModelCreator.setUpUML(SourceUMLPath);
		if(!papyrusModelCreator.diExists()){
			
			monitor.subTask("Generating Papyrus model...");
			papyrusModelCreator.createPapyrusModel();
			IMultiDiagramEditor editor = (IMultiDiagramEditor) EditorOpener.openPapyrusEditor(papyrusModelCreator.getDi());
			
			UmlModel umlModel = (UmlModel) editor.getServicesRegistry().getService(ModelSet.class)
												.getModel(UmlModel.MODEL_ID);
			
			if(this.layoutDescriptor instanceof TxtUMLLayoutDescriptor){
				papyrusModelManager = new TxtUMLPapyrusModelManager(editor, umlModel, (TxtUMLLayoutDescriptor) this.layoutDescriptor);
			}else{
				papyrusModelManager = new DefaultPapyrusModelManager(editor, umlModel);
			}
			monitor.worked(10);
			
			papyrusModelManager.createAndFillDiagrams(new SubProgressMonitor(monitor, 90));
		}else{
			Dialogs.MessageBox("Loading Model", "A Papyrus model with this name already exists in this Project. It'll be loaded");
			EditorOpener.openPapyrusEditor(papyrusModelCreator.getDi());
			monitor.worked(100);
		}
	}
}