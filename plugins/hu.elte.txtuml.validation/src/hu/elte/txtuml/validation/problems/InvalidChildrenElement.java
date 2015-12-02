package hu.elte.txtuml.validation.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;

public class InvalidChildrenElement extends ValidationErrorBase {

	private String nodeStr;

	public InvalidChildrenElement(SourceInfo sourceInfo, String nodeStr, ASTNode child) {
		super(sourceInfo, child);
		this.nodeStr = nodeStr;
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_CHILDREN_ELEMENT.ordinal();
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Invalid element in ");
		sb.append(nodeStr);
		sb.append(".");
		return sb.toString();
	}

}
