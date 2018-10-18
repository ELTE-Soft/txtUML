package hu.elte.txtuml.validation.common;

import java.util.List;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import hu.elte.txtuml.validation.model.ModelErrors;
import hu.elte.txtuml.validation.model.ModelValidationError;

/**
 * Base class for all txtUML validation problems.
 * <p>
 * By default, {@link #isError}, {@link #isWarning} and {@link isInfo} all
 * return false.
 */
public abstract class AbstractValidationProblem extends CategorizedProblem implements IValidationProblem {

	private final SourceInfo sourceInfo;
	private int sourceStart;
	private int sourceEnd;
	private int lineNumber;

	public AbstractValidationProblem(SourceInfo sourceInfo, ASTNode node) {
		this.sourceInfo = sourceInfo;
		Class<?> c = ASTNode.nodeClassForType(node.getNodeType());
		if (c == PrefixExpression.class) {
			this.sourceStart = node.getStartPosition();
			this.sourceEnd = node.getStartPosition() + node.getLength() - 1;
		} else if (this instanceof ModelValidationError
				&& ((ModelValidationError) this).getType() == ModelErrors.STATE_METHOD_PARAMETERS
				|| ((ModelValidationError) this).getType() == ModelErrors.TRANSITION_METHOD_PARAMETERS) {
			List<SingleVariableDeclaration> paramlist = ((MethodDeclaration) node).parameters();
			this.sourceStart = paramlist.get(0).getStartPosition();
			this.sourceEnd = paramlist.get(paramlist.size() - 1).getStartPosition()
					+ paramlist.get(paramlist.size() - 1).getLength();
		} else {
			this.sourceStart = node.getStartPosition();
			this.sourceEnd = node.getStartPosition() + node.getLength();
		}
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
