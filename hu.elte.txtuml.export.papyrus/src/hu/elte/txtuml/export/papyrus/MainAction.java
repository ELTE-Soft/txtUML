package hu.elte.txtuml.export.papyrus;

import static org.eclipse.papyrus.uml.diagram.wizards.Activator.log;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
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
public class MainAction {
	private IWorkbenchWindow window;
	private String Projectname;
	private String Modelname;
	private String SourceUMLPath;
	private PapyrusModelCreator papyrusModelCreator;
	private PapyrusModelManager papyrusModelManager;
	private ProjectManager projectManager;
	
	/**
	 * The constructor.
	 * @param projectName - The name of the project in which the Papyrus Model will be created.
	 * @param modelName - The Name of the Papyrus Model
	 * @param sourceUMLpath - Sourcepath of the ECore uml2 model 
	 */
	public MainAction(String projectName, String modelName, String sourceUMLpath) {
		Projectname = projectName;
		Modelname = modelName;
		SourceUMLPath = sourceUMLpath;
		window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		projectManager = new ProjectManager();
		papyrusModelCreator = new PapyrusModelCreator();
	}
	
	/**
	 * Executes the visualization process. 
	 * Creates the project (if not exists) and sets up the Papyrus Model
	 */
	public void run() {
		IProject project = projectManager.createProject(Projectname);
		projectManager.openProject(project);
		createPapyrusProject();
	}
	
	/**
	 * Creates a Papyrus Model in the opened project or handles the Exceptions 
	 * with a messagebox.
	 */
	private void createPapyrusProject() {
		try {	
			createAndOpenPapyrusModel();
		} catch (Exception e) {
			msgb("Error", e.toString());
			e.printStackTrace();
		}
	}	
	
	/**
	 * Creates the Papyrus Model and fills the diagrams.
	 * If the Model already exists, then loads it.
	 * @throws Exception Any kind of Exception that could be thrown  
	 */
	protected void createAndOpenPapyrusModel() throws Exception{
		papyrusModelCreator.init(Projectname+"/"+Modelname, SourceUMLPath);

		if(!papyrusModelCreator.diExists()){
			
			papyrusModelCreator.createPapyrusModel();
			IMultiDiagramEditor editor = (IMultiDiagramEditor) openEditor(papyrusModelCreator.getDi());
			papyrusModelManager = new PapyrusModelManager(editor);
			papyrusModelManager.createAndFillDiagrams();
		}else{
			msgb("Loading Model", "A Papyrus model with this name already exists in this Project. It'll be loaded");
			papyrusModelCreator.loadPapyrusModel();
			openEditor(papyrusModelCreator.getDi());
		}
	}


	/**
	 * Opens an editor for the file
	 * @param file A file in the project
	 * @return The EditorPart of the editor
	 * @throws PartInitException
	 */
	public final IEditorPart openEditor(final IFile file) throws PartInitException {
			IEditorPart ed = null;
			IWorkbenchPage page = window.getActivePage();
			if(page != null) {
				try {
					IEditorInput editorInput = new FileEditorInput(file);
					ed = IDE.openEditor(page, editorInput, "org.eclipse.papyrus.infra.core.papyrusEditor", true);
				} catch (PartInitException e) {
					log.error(e);
				}
			}
			return ed;
	}
	
	/**
	 * Opens a MessageBox with the given title and message.
	 * @param title The title of the MessageBox window
	 * @param body The Message
	 */
	public void msgb(String title, String body){
		MessageDialog.openInformation(window.getShell(),title,body);
	}
}