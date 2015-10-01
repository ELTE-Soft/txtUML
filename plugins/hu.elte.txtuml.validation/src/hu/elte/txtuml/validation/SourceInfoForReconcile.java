package hu.elte.txtuml.validation;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;

public class SourceInfoForReconcile extends SourceInfo {

	private ICompilationUnit compUnit;

	public SourceInfoForReconcile(ICompilationUnit compUnit) {
		this.compUnit = compUnit;
	}

	@Override
	public int getSourceLineNumber(int charIndex) {
		try {
			return compUnit.getSource().substring(0, charIndex).split("\r\n|\r|\n").length;
		} catch (JavaModelException e) {
			return 0;
		}
	}

	@Override
	public String getOriginatingFileName() {
		try {
			return compUnit.getCorrespondingResource().getName();
		} catch (JavaModelException e) {
			return null;
		}
	}

}
