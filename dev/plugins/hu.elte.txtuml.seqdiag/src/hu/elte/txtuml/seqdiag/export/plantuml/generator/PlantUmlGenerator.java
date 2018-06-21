package hu.elte.txtuml.seqdiag.export.plantuml.generator;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Type;

import hu.elte.txtuml.seqdiag.export.plantuml.exceptions.ExportRuntimeException;
import hu.elte.txtuml.seqdiag.export.plantuml.exceptions.PreCompilationError;
import hu.elte.txtuml.seqdiag.export.plantuml.exceptions.SequenceDiagramStructuralException;

/**
 * This class combines the functionality of the compiler and the precompiler,
 * and generates sequence diagram output using PlantUML syntax (see
 * {@link #generate(CompilationUnit, IFile)} method).
 */
public class PlantUmlGenerator {

	private PlantUmlPreCompiler preCompiler;
	private PlantUmlCompiler compiler;
	private String seqDiagramName;
	private CompilationUnit source;

	/**
	 * Processes the source, then generates PlantUML output to the given target
	 * file.
	 */
	public void generate(final CompilationUnit source, final IFile targetFile, String seqDiagramName)
			throws SequenceDiagramStructuralException, PreCompilationError {
		this.source = source;
		this.seqDiagramName = seqDiagramName;
		preCompile();
		compile(targetFile);
	}

	private void preCompile() throws PreCompilationError {
		preCompiler = new PlantUmlPreCompiler(seqDiagramName);
		source.accept(preCompiler);
		if (!preCompiler.getErrors().isEmpty()) {
			StringBuilder messages = new StringBuilder();
			for (Exception ex : preCompiler.getErrors()) {
				messages.append("\n" + ex.getMessage());
			}
			throw new PreCompilationError(messages.toString());
		}

		// parse all superclasses
		Type superClass = preCompiler.getSuperClass();
		while (superClass != null) {
			CompilationUnit cu = getSuperClassCU(superClass);
			preCompiler.setSeqDiagramName(superClass.resolveBinding().getName());
			cu.accept(preCompiler);
			superClass = preCompiler.getSuperClass();
		}
	}

	private void compile(IFile targetFile) throws SequenceDiagramStructuralException {
		compiler = new PlantUmlCompiler(preCompiler.getOrderedLifelines(), seqDiagramName);
		source.accept(compiler);

		createTargetFile(compiler.getCompiledOutput(), targetFile);

		if (compiler.getErrors().size() > 0) {
			StringBuilder errors = new StringBuilder();
			compiler.getErrors().forEach(errors::append);
			throw new SequenceDiagramStructuralException(errors.toString());
		}
	}

	private void createTargetFile(String compiledOutput, IFile targetFile) throws ExportRuntimeException {
		ByteArrayInputStream stream = new ByteArrayInputStream(compiledOutput.getBytes());

		try {
			targetFile.create(stream, false, null);
		} catch (Exception e) {
			throw new ExportRuntimeException(
					"Couldn't create target file: " + targetFile.getName() + "\n Reason: " + e.getMessage());
		}
	}

	private CompilationUnit getSuperClassCU(Type superClass) {
		ICompilationUnit element = (ICompilationUnit) superClass.resolveBinding().getTypeDeclaration().getJavaElement()
				.getParent();

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setSource(element);

		return (CompilationUnit) parser.createAST(null);
	}

}
