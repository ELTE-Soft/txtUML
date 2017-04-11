package hu.elte.txtuml.validation.model.problems;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.model.JtxtUMLCompilationParticipant;
import hu.elte.txtuml.validation.model.SourceInfo;

/**
 * Base class for all jtxtuml problems.
 */
public abstract class ValidationErrorBase extends CategorizedProblem {

	private SourceInfo sourceInfo;
	private int sourceStart;
	private int sourceEnd;
	private int lineNumber;

	public ValidationErrorBase(SourceInfo sourceInfo, ASTNode node) {
		this.sourceInfo = sourceInfo;
		this.sourceStart = node.getStartPosition();
		this.sourceEnd = node.getStartPosition() + node.getLength() - 1;
		this.lineNumber = sourceInfo.getSourceLineNumber(getSourceEnd());
	}

	@Override
	public String[] getArguments() {
		return new String[0];
	}

	@Override
	public char[] getOriginatingFileName() {
		return sourceInfo.getOriginatingFileName().toCharArray();
	}

	@Override
	public int getSourceEnd() {
		return sourceEnd;
	}

	@Override
	public int getSourceLineNumber() {
		return lineNumber;
	}

	@Override
	public int getSourceStart() {
		return sourceStart;
	}

	@Override
	public boolean isError() {
		return true;
	}

	@Override
	public boolean isWarning() {
		return false;
	}

	@Override
	public void setSourceEnd(int sourceEnd) {
		this.sourceEnd = sourceEnd;
	}

	@Override
	public void setSourceLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public void setSourceStart(int sourceStart) {
		this.sourceStart = sourceStart;
	}

	@Override
	public int getCategoryID() {
		return 0;
	}

	@Override
	public String getMarkerType() {
		return JtxtUMLCompilationParticipant.JTXTUML_MARKER_TYPE;
	}

}
