package hu.elte.txtuml.validation;

import org.eclipse.jdt.core.dom.CompilationUnit;

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
