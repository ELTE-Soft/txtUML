package hu.elte.txtuml.export.plantuml.generator;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Type;

import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramStructuralException;

public class PlantUmlGenerator {

	private IFile targetFile;
	private CompilationUnit sourceCU;

	private PlantUmlPreCompiler preCompiler;
	private PlantUmlCompiler compiler;

	public PlantUmlGenerator(IFile targetFile, CompilationUnit source) {

		this.targetFile = targetFile;
		this.sourceCU = source;
	}

	public void generate() throws SequenceDiagramStructuralException {

		preCompiler = new PlantUmlPreCompiler();
		sourceCU.accept(preCompiler);

		Type superClass = preCompiler.getSuperClass();

		CompilationUnit cu = null;

		if (superClass != null) {
			cu = getSuperClassCU(superClass);
		}
		preCompiler.setIsSuper(true);
		cu.accept(preCompiler);

		compiler = new PlantUmlCompiler(preCompiler.superFields, preCompiler.fragments, false);
		sourceCU.accept(compiler);

		String compiledOutput = compiler.getCompiledOutput();

		ByteArrayInputStream stream = new ByteArrayInputStream(compiledOutput.getBytes());

		try {
			targetFile.create(stream, false, null);
		} catch (Exception e) {
			e.printStackTrace();
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
