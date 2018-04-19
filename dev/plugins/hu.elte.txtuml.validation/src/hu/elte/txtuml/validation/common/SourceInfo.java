package hu.elte.txtuml.validation.common;

import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Class for calculating some informations like line numbers or source files for
 * error markings. Only needed to be able to use the java problem handling
 * utilities.
 */
public class SourceInfo {

	private CompilationUnit compilationUnit;

	public SourceInfo(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	public int getSourceLineNumber(int charIndex) {
		return compilationUnit.getLineNumber(charIndex);
	}

	public String getOriginatingFileName() {
		return compilationUnit.getJavaElement().getPath().toOSString();
	}

}
