package hu.elte.txtuml.export.plantuml.generator;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramStructuralException;

public class PlantUmlGenerator {

	private IFile targetFile;
	private CompilationUnit sourceCU;

	private MethodStatementWalker walker;
	private PlantUmlCompiler compiler;

	public PlantUmlGenerator(IFile targetFile, CompilationUnit source) {

		this.targetFile = targetFile;
		this.sourceCU = source;
	}

	public void generate() throws SequenceDiagramStructuralException {

		walker = new MethodStatementWalker();
		sourceCU.accept(walker);

		compiler = new PlantUmlCompiler(walker.fragments);
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
}
