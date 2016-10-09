package hu.elte.txtuml.export.plantuml;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.export.plantuml.exceptions.ExportRuntimeException;
import hu.elte.txtuml.export.plantuml.exceptions.PreCompilationError;
import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramStructuralException;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;
import hu.elte.txtuml.utils.eclipse.ClassLoaderProvider;

/**
 * 
 * @author Zoli
 *
 *         PlantUml exporter class
 *         <p>
 *         Exports sequence diagrams to plantuml textual output into the gen
 *         folder filenames are the same as the sequence diagram class names
 *         </p>
 *
 */
public class PlantUmlExporter {

	private String projectName;
	private String genFolderName;
	private List<String> diagrams;
	private List<Class<Interaction>> seqDiagrams;
	private URLClassLoader loader;

	protected int exportedCount = 0;
	protected int nonExportedCount = 0;

	protected boolean hadErrors = false;
	protected String errorMessage = null;

	public PlantUmlExporter(String txtUMLProjectName, String generatedFolderName, List<String> SeqDiagramNames) {
		projectName = txtUMLProjectName;
		genFolderName = generatedFolderName;
		diagrams = SeqDiagramNames;
		nonExportedCount = diagrams.size();
		loader = ClassLoaderProvider.getClassLoaderForProject(projectName, Interaction.class.getClassLoader());
		filterDiagramsByType();
	}

	public PlantUmlExporter(IProject txtUMLProject, String generatedFolderName, List<String> SeqDiagramNames) {
		projectName = txtUMLProject.getName();
		genFolderName = generatedFolderName;
		diagrams = SeqDiagramNames;
		nonExportedCount = diagrams.size();
		loader = ClassLoaderProvider.getClassLoaderForProject(projectName, Interaction.class.getClassLoader());
		System.out.println("Project location");
		System.out.println(txtUMLProject.getLocationURI().toString());
		filterDiagramsByType();
	}

	@SuppressWarnings("unchecked")
	/**
	 * Filter method to separate sequenceDiagrams from normal Layouts, leave
	 * normal layouts in remove SequenceDiagrams
	 * 
	 */
	private void filterDiagramsByType() {
		seqDiagrams = new ArrayList<Class<Interaction>>();
		String diagram = null;
		for (Iterator<String> iterator = diagrams.iterator(); iterator.hasNext();) {
			diagram = iterator.next();
			try {
				System.out.println(loader.getURLs().toString());
				Class<?> diagramClass = loader.loadClass(diagram);

				if (Interaction.class.isAssignableFrom(diagramClass)) {
					seqDiagrams.add((Class<Interaction>) diagramClass);
					iterator.remove();
					nonExportedCount--;
				}

			} catch (ClassNotFoundException e) {
				throw new ExportRuntimeException("There was an error while trying to load Class " + diagram
						+ ", the error was the following:" + e.getMessage());
			}
		}
	}

	/**
	 * Generate the output into the gen folder using JDT. At the end refresh the
	 * project so the freshly created resource shows up
	 * 
	 * @throws CoreException
	 * @throws SequenceDiagramStructuralException
	 * @throws PreCompilationError
	 */
	public void generatePlantUmlOutput(IProgressMonitor monitor)
			throws CoreException, SequenceDiagramStructuralException, PreCompilationError {
		for (Class<Interaction> sequenceDiagram : seqDiagrams) {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			IFile resource = project.getFile("src/" + sequenceDiagram.getName().replace('.', '/') + ".java");

			ICompilationUnit element = (ICompilationUnit) JavaCore.create(resource);

			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			parser.setSource(element);

			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			String fileName = sequenceDiagram.getSimpleName();

			URI targetURI = CommonPlugin
					.resolve(URI.createFileURI(projectName + "/" + genFolderName + "/" + fileName + ".txt"));

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

			PlantUmlGenerator generator = new PlantUmlGenerator(loader, targetFile, cu);
			generator.generate();
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
			exportedCount++;
		}
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

	public boolean exportedAll() {
		return nonExportedCount == 0;
	}

	public boolean didExport() {
		return exportedCount > 0;
	}

	public boolean wasSeqDiagExport() {
		return exportedAll() && didExport();
	}

	public int expotedCount() {
		return exportedCount;
	}

	public int nonExportedCount() {
		return nonExportedCount;
	}

	public boolean hasSequenceDiagram() {
		return seqDiagrams.size() > 0;
	}

	public boolean noDiagramLayout() {
		return diagrams.size() == 0;
	}

}
