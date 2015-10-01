package hu.elte.txtuml.validation;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class SourceInfoForBuild extends SourceInfo {

	private String name;
	private CompilationUnit compUnit;
	
	public SourceInfoForBuild(String name, CompilationUnit compUnit) {
		this.name = name;
		this.compUnit = compUnit;
	}
	
	@Override
	public int getSourceLineNumber(int charIndex) {
		return this.compUnit.getLineNumber(charIndex);
	}

	@Override
	public String getOriginatingFileName() {
		return name;
	}

}
