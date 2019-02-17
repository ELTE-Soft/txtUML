package hu.elte.txtuml.validation.common;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Base class for all txtUML validation problems.
 * <p>
 * By default, {@link #isError}, {@link #isWarning} and {@link isInfo} all
 * return false.
 */
public abstract class AbstractValidationProblem extends CategorizedProblem implements IValidationProblem {

	private final SourceInfo sourceInfo;
	protected int sourceStart;
	protected int sourceEnd;
	private int lineNumber;

	public AbstractValidationProblem(SourceInfo sourceInfo, ASTNode node) {
		this.sourceInfo = sourceInfo;

		ASTNode nodeToMark = getNodeToMark(node);
		this.sourceStart = nodeToMark.getStartPosition();
		this.sourceEnd = nodeToMark.getStartPosition() + nodeToMark.getLength();
		this.lineNumber = sourceInfo.getSourceLineNumber(getSourceEnd());
	}

	protected ASTNode getNodeToMark(ASTNode source) {
		return source;
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
	public int getCategoryID() {
		return 0;
	}

	@Override
	public boolean isError() {
		return false;
	}

	@Override
	public boolean isWarning() {
		return false;
	}

	@Override
	public boolean isInfo() {
		return super.isInfo();
	}

	@Override
	public int getSourceStart() {
		return sourceStart;
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
	public void setSourceStart(int sourceStart) {
		this.sourceStart = sourceStart;
	}

	@Override
	public void setSourceEnd(int sourceEnd) {
		this.sourceEnd = sourceEnd;
	}

	@Override
	public void setSourceLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
