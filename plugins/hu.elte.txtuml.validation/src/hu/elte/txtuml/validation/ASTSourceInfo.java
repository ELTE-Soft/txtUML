package hu.elte.txtuml.validation;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTSourceInfo extends SourceInfo {

	private CompilationUnit compUnit;

	public ASTSourceInfo(CompilationUnit compilationUnit) {
		this.compUnit = compilationUnit;
	}

	@Override
	public int getSourceLineNumber(int charIndex) {
		return compUnit.getLineNumber(charIndex);
	}

	@Override
	public String getOriginatingFileName() {
		return compUnit.getJavaElement().getPath().toOSString();
	}

}
