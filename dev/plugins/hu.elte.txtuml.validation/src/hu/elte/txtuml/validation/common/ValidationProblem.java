package hu.elte.txtuml.validation.common;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Base class for all txtUML validation problems.
 * 
 * @see ValidationProblem.Error
 * @see ValidationProblem.Warning
 * @see ValidationProblem.Info
 */
public abstract class ValidationProblem extends CategorizedProblem {

	private final SourceInfo sourceInfo;
	private int sourceStart;
	private int sourceEnd;
	private int lineNumber;

	public ValidationProblem(SourceInfo sourceInfo, ASTNode node) {
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
	public int getCategoryID() {
		return 0;
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

	public static abstract class Error extends ValidationProblem {

		public Error(SourceInfo sourceInfo, ASTNode node) {
			super(sourceInfo, node);
		}

		@Override
		public boolean isError() {
			return true;
		}

		@Override
		public boolean isWarning() {
			return false;
		}

	}

	public static abstract class Warning extends ValidationProblem {

		public Warning(SourceInfo sourceInfo, ASTNode node) {
			super(sourceInfo, node);
		}

		@Override
		public boolean isError() {
			return false;
		}

		@Override
		public boolean isWarning() {
			return true;
		}

	}

	public static abstract class Info extends ValidationProblem {

		public Info(SourceInfo sourceInfo, ASTNode node) {
			super(sourceInfo, node);
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
			return true;
		}

	}

}
