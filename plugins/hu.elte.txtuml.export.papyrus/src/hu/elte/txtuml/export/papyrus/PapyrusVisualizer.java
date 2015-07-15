package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLLayoutDescriptor;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.AbstractPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.PapyrusDefaultModelManager;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.PapyrusTxtUMLModelManager;
import hu.elte.txtuml.export.utils.Dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelMultiException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

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
	private PapyrusModelCreator papyrusModelCreator;
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
		this.papyrusModelCreator = new PapyrusModelCreator();
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
	 */
	private void createAndOpenPapyrusModel(IProgressMonitor monitor) throws ModelMultiException{
		monitor.beginTask("Generating Papyrus Model", 100);
		papyrusModelCreator.init(Projectname+"/"+Modelname);
		papyrusModelCreator.setUpUML(SourceUMLPath);
		if(!papyrusModelCreator.diExists()){
			
			monitor.subTask("Generating Papyrus model...");
			papyrusModelCreator.createPapyrusModel();
			IMultiDiagramEditor editor = (IMultiDiagramEditor) openEditor(papyrusModelCreator.getDi());
			if(this.layoutDescriptor instanceof TxtUMLLayoutDescriptor){
				papyrusModelManager = new PapyrusTxtUMLModelManager(editor, (TxtUMLLayoutDescriptor) this.layoutDescriptor);
			}else{
				papyrusModelManager = new PapyrusDefaultModelManager(editor);
			}
			monitor.worked(10);
			
			papyrusModelManager.createAndFillDiagrams(new SubProgressMonitor(monitor, 90));
		}else{
			Dialogs.MessageBox("Loading Model", "A Papyrus model with this name already exists in this Project. It'll be loaded");
			papyrusModelCreator.loadPapyrusModel();
			openEditor(papyrusModelCreator.getDi());
			monitor.worked(100);
		}
	}


	/**
	 * Opens an editor for the file
	 * @param file A file in the project
	 * @return The EditorPart of the editor
	 * @throws PartInitException
	 */
	public static final IEditorPart openEditor(final IFile file){
			IEditorPart ed = null;
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if(page != null) {
				try {
					IEditorInput editorInput = new FileEditorInput(file);
					ed = IDE.openEditor(page, editorInput, "org.eclipse.papyrus.infra.core.papyrusEditor", true);
				} catch (PartInitException e) {
					Dialogs.errorMsgb(null, null, e);
				}
			}
			return ed;
	}
}