package hu.elte.txtuml.export.plantuml.generator;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Type;

import hu.elte.txtuml.export.plantuml.exceptions.ExportRuntimeException;
import hu.elte.txtuml.export.plantuml.exceptions.PreCompilationError;
import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramStructuralException;

/**
 * 
 * This Class combines the compiler and the preCompiler and runs them. Provides
 * the required utility functions for them
 * 
 * @author Zoli
 *
 */
public class PlantUmlGenerator {

	private IFile targetFile;
	private CompilationUnit sourceCU;

	private PlantUmlPreCompiler preCompiler;
	private PlantUmlCompiler compiler;

	public PlantUmlGenerator(IFile targetFile, CompilationUnit source) {

		this.targetFile = targetFile;
		this.sourceCU = source;
	}

	public void generate() throws SequenceDiagramStructuralException, PreCompilationError {

		preCompiler = new PlantUmlPreCompiler();
		sourceCU.accept(preCompiler);
		if (!preCompiler.getErrors().isEmpty()) {

			String messages = "";

			for (Exception ex : preCompiler.getErrors()) {
				messages += "\n" + ex.getMessage();
			}
			throw new PreCompilationError(messages);
		}

		Type superClass = preCompiler.getSuperClass();
		while (superClass != null) {
			CompilationUnit cu = null;

			if (superClass != null) {
				cu = getSuperClassCU(superClass);
			}
			cu.accept(preCompiler);

			superClass = preCompiler.getSuperClass();
		}

		compiler = new PlantUmlCompiler(preCompiler.lifelines, preCompiler.fragments, false);
		sourceCU.accept(compiler);

		String compiledOutput = compiler.getCompiledOutput();

		ByteArrayInputStream stream = new ByteArrayInputStream(compiledOutput.getBytes());

		try {
			targetFile.create(stream, false, null);
		} catch (Exception e) {
			throw new ExportRuntimeException(
					"couldn't create targetFile:" + targetFile.getName() + "\n Reason:" + e.getMessage());
		}

		if (compiler.getErrors().size() > 0) {
			String errString = "";

			for (String error : compiler.getErrors()) {
				errString += error;
			}

			throw new SequenceDiagramStructuralException(errString);
		}
	}

	private CompilationUnit getSuperClassCU(Type superClass) {

		ICompilationUnit element = (ICompilationUnit) superClass.resolveBinding().getTypeDeclaration().getJavaElement()
				.getParent();

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setSource(element);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		return cu;
	}
}
