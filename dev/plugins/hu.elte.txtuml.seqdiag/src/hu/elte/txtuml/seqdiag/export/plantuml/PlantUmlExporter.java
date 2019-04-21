package hu.elte.txtuml.seqdiag.export.plantuml;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.seqdiag.export.plantuml.exceptions.ExportRuntimeException;
import hu.elte.txtuml.seqdiag.export.plantuml.exceptions.SequenceDiagramExportException;
import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlGenerator;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;

/**
 * <b>PlantUML exporter class.</b>
 * <p>
 * Exports sequence diagram descriptions as PlantUML textual output into the gen
 * folder. Generated file names are the same as the sequence diagram class
 * names. See {@link #generatePlantUmlOutput(IProgressMonitor)} method.
 */
public class PlantUmlExporter {

	private PlantUmlGenerator generator;
	private String projectName;
	private IProject project;
	private String genFolderName;
	private List<IType> seqDiagrams;

	protected boolean hadErrors = false;
	protected String errorMessage = null;

	public PlantUmlExporter(final String txtUMLProjectName, final String generatedFolderName,
			final List<IType> seqDiagrams) {
		IProject _project = ResourcesPlugin.getWorkspace().getRoot().getProject(txtUMLProjectName);

		if (_project == null || !_project.exists()) {
			throw new ExportRuntimeException("Project not found with name: " + txtUMLProjectName);
		}

		initialize(_project, generatedFolderName, seqDiagrams);
	}

	public PlantUmlExporter(final IProject txtUMLProject, final String generatedFolderName,
			final List<IType> seqDiagrams) {
		initialize(txtUMLProject, generatedFolderName, seqDiagrams);
	}

	private void initialize(IProject txtUMLProject, String generatedFolderName, List<IType> seqDiagrams) {
		generator = new PlantUmlGenerator();
		project = txtUMLProject;
		projectName = txtUMLProject.getName();
		genFolderName = generatedFolderName;
		this.seqDiagrams = seqDiagrams;
	}

	/**
	 * Generates the output into the gen folder using JDT. Also refreshes the
	 * project so the newly created resource shows up.
	 * 
	 * @throws CoreException
	 * @throws SequenceDiagramExportException
	 */
	public void generatePlantUmlOutput(final IProgressMonitor monitor)
			throws CoreException, SequenceDiagramExportException {
		try {
			for (IType sequenceDiagram : seqDiagrams) {
				String simpleName = sequenceDiagram.getElementName();
				
				ICompilationUnit element = sequenceDiagram.getCompilationUnit();
	
				ASTParser parser = ASTParser.newParser(AST.JLS8);
				parser.setResolveBindings(true);
				parser.setBindingsRecovery(true);
				parser.setSource(element);
				
				CompilationUnit cu = (CompilationUnit) parser.createAST(null);
	
				TypeDeclaration seqeunceDiagramDeclaration = getSequenceDiagramTypeDeclaration(
						sequenceDiagram.getFullyQualifiedName(), cu);
	
				if (!ElementTypeTeller.hasSuperClass(seqeunceDiagramDeclaration.resolveBinding(),
						SequenceDiagram.class.getCanonicalName()))
					throw new SequenceDiagramExportException(
							sequenceDiagram.getElementName() + " is not a sequence diagram.");
	
				URI targetURI = CommonPlugin
						.resolve(URI.createFileURI(projectName + "/" + genFolderName + "/" + simpleName + ".txt"));
	
				IFile targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(targetURI.toFileString()));
	
				if (PlatformUI.isWorkbenchRunning()) {
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
	
					cleanupWorkbench(targetFile, page);
					if (targetFile.exists()) {
						targetFile.delete(true, null);
					}
				}
	
				URI targetDirURI = CommonPlugin.resolve(URI.createFileURI(projectName + "/" + genFolderName));
	
				IFolder targetDir = ResourcesPlugin.getWorkspace().getRoot()
						.getFolder(new Path(targetDirURI.toFileString()));
	
				if (!targetDir.exists()) {
					targetDir.create(false, true, new NullProgressMonitor());
				}
	
				if (monitor != null) {
					monitor.worked(100 / (seqDiagrams.size() * 2));
				}
				
				generator.generate(cu, targetFile, simpleName);
				project.refreshLocal(IProject.DEPTH_INFINITE, null);
	
				if (PlatformUI.isWorkbenchRunning()) {
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
					IEditorDescriptor editor = workbench.getEditorRegistry().getDefaultEditor(targetFile.getName());
					IEditorPart editorPart = page.openEditor(new FileEditorInput(targetFile), editor.getId());
					page.activate(editorPart);
					page.showView("net.sourceforge.plantuml.eclipse.views.PlantUmlView");
				}
	
				if (monitor != null) {
					monitor.worked(100 / (seqDiagrams.size() * 2));
				}
			}
		}
		catch (Exception e){
			throw new SequenceDiagramExportException("");
		}
	}

	@SuppressWarnings("unchecked")
	private TypeDeclaration getSequenceDiagramTypeDeclaration(String sequenceDiagramName, CompilationUnit cu) {
		List<TypeDeclaration> declarations = cu.types();
		return declarations.stream()
				.filter(decl -> decl.resolveBinding().getQualifiedName().equals(sequenceDiagramName)).findFirst().get();
	}

	protected void cleanupWorkbench(IFile targetFile, IWorkbenchPage page) throws CoreException {
		if (targetFile.exists()) {
			IEditorReference[] refs = page.getEditorReferences();
			for (IEditorReference ref : refs) {
				IEditorPart part = ref.getEditor(false);
				if (part != null) {
					IEditorInput inp = part.getEditorInput();
					if (inp instanceof FileEditorInput) {
						if (((FileEditorInput) inp).getFile().equals(targetFile)) {
							page.closeEditor(ref.getEditor(false), true);
						}
					}
				}
			}
		}
	}

	public boolean isSuccessful() {
		return !hadErrors;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
