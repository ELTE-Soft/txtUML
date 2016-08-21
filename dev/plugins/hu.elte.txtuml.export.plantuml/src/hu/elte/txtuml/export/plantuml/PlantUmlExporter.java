package hu.elte.txtuml.export.plantuml;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramStructuralException;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;
import hu.elte.txtuml.utils.eclipse.ClassLoaderProvider;

public class PlantUmlExporter {

	private String projectName;
	private String genFolderName;
	private List<String> diagrams;
	private List<Class<Interaction>> seqDiagrams;

	protected int exportedCount = 0;
	protected int nonExportedCount = 0;

	protected boolean hadErrors = false;
	protected String errorMessage = null;

	public PlantUmlExporter(String txtUMLProjectName, String generatedFolderName, List<String> SeqDiagramNames) {
		projectName = txtUMLProjectName;
		genFolderName = generatedFolderName;
		diagrams = SeqDiagramNames;
		nonExportedCount = diagrams.size();
		filterDiagramsByType();
	}

	@SuppressWarnings("unchecked")
	private void filterDiagramsByType() {
		seqDiagrams = new ArrayList<Class<Interaction>>();
		for (String diagram : diagrams) {
			try {
				URLClassLoader loader = ClassLoaderProvider.getClassLoaderForProject(projectName,
						Interaction.class.getClassLoader());
				Class<?> diagramClass = loader.loadClass(diagram);

				if (Interaction.class.isAssignableFrom(diagramClass)) {
					seqDiagrams.add((Class<Interaction>) diagramClass);
					diagrams.remove(diagram);
					nonExportedCount--;
					exportedCount++;
				} else {
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void generatePlantUmlOutput() {
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
			try {
				if (targetFile.exists()) {

					targetFile.delete(true, null);

				}
				PlantUmlGenerator generator = new PlantUmlGenerator(targetFile, cu);
				generator.generate();
				project.refreshLocal(IProject.DEPTH_INFINITE, null);
			} catch (SequenceDiagramStructuralException | CoreException ex) {
				hadErrors = true;
				errorMessage = ex.getMessage();
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

}
