package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.export.utils.Dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
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
public class MainAction {
	private IWorkbenchWindow window;
	private String Projectname;
	private String Modelname;
	private String SourceUMLPath;
	private PapyrusModelCreator papyrusModelCreator;
	private AbstractPapyrusModelManager papyrusModelManager;
	private ProjectManager projectManager;
	private Object layoutDescriptor;
	
	/**
	 * The constructor.
	 * @param projectName - The name of the project in which the Papyrus Model will be created.
	 * @param modelName - The Name of the Papyrus Model
	 * @param sourceUMLpath - Sourcepath of the Eclipse UML2 model 
	 * @param layoutDescripton  - A descriptor file which adds constraints to the layout and diagram generation
	 */
	public MainAction(String projectName, String modelName,
			String sourceUMLpath, Object layoutDescripton) {
		this.Projectname = projectName;
		this.Modelname = modelName;
		this.SourceUMLPath = sourceUMLpath;
		this.window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		this.projectManager = new ProjectManager();
		this.papyrusModelCreator = new PapyrusModelCreator();
		this.layoutDescriptor = layoutDescripton;
	}
	
	/**
	 * @param projectName
	 * @param modelName
	 * @param sourceUMLpath
	 */
	public MainAction(String projectName, String modelName, String sourceUMLpath){
		this(projectName, modelName, sourceUMLpath, null);
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
			Dialogs.errorMsgb("Error", e.toString(), e);
		}
	}	
	
	/**
	 * Creates the Papyrus Model and fills the diagrams.
	 * If the Model already exists, then loads it.
	 * @throws Exception Any kind of Exception that could be thrown  
	 */
	private void createAndOpenPapyrusModel() throws Exception{
		papyrusModelCreator.init(Projectname+"/"+Modelname);
		papyrusModelCreator.setUpUML(SourceUMLPath);
		if(!papyrusModelCreator.diExists()){
			
			papyrusModelCreator.createPapyrusModel();
			IMultiDiagramEditor editor = (IMultiDiagramEditor) openEditor(papyrusModelCreator.getDi());
			if(this.layoutDescriptor instanceof TxtUMLLayoutDescriptor){
				papyrusModelManager = new PapyrusTxtUMLModelManager(editor, (TxtUMLLayoutDescriptor) this.layoutDescriptor);
			}else{
				papyrusModelManager = new PapyrusDefaultModelManager(editor);
			}
			
			papyrusModelManager.createAndFillDiagrams();
		}else{
			Dialogs.MessageBox("Loading Model", "A Papyrus model with this name already exists in this Project. It'll be loaded");
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
	private final IEditorPart openEditor(final IFile file){
			IEditorPart ed = null;
			IWorkbenchPage page = window.getActivePage();
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