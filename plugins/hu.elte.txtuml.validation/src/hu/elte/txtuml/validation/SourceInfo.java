package hu.elte.txtuml.validation;

import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Class for calculating some informations like line numbers or source files for
 * error markings. Only needed to be able to use the java problem handling
 * utilities.
 */
public class SourceInfo {

	private CompilationUnit compUnit;

	public SourceInfo(CompilationUnit compilationUnit) {
		this.compUnit = compilationUnit;
	}

	public int getSourceLineNumber(int charIndex) {
		return compUnit.getLineNumber(charIndex);
	}

	public String getOriginatingFileName() {
		return compUnit.getJavaElement().getPath().toOSString();
	}

}
