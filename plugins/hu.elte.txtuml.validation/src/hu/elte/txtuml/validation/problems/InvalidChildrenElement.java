package hu.elte.txtuml.validation.problems;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.SourceInfo;

public class InvalidChildrenElement extends ValidationErrorBase {

	private ASTNode node;
	private ASTNode child;
	private Class<?>[] allowedChildrenTypes;

	public InvalidChildrenElement(SourceInfo sourceInfo, ASTNode node, ASTNode child, Class<?>[] allowedChildrenTypes) {
		super(sourceInfo, child);
		this.node = node;
		this.child = child;
		this.allowedChildrenTypes = allowedChildrenTypes;
	}

	@Override
	public int getID() {
		return ValidationErrorCatalog.INVALID_CHILDREN_ELEMENT.ordinal();
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Invalid element: ");
		sb.append(child.getClass().getSimpleName());
		sb.append(" in ");
		sb.append(node.getClass().getSimpleName());
		sb.append(". ");
		if (allowedChildrenTypes.length > 1) {
			sb.append("Only");
			for (int i = 0; i < allowedChildrenTypes.length - 2; i++) {
				sb.append(allowedChildrenTypes[i].getSimpleName());
				sb.append(", ");
			}
			sb.append(allowedChildrenTypes[allowedChildrenTypes.length - 2].getSimpleName());
			sb.append(" and ");
			sb.append(allowedChildrenTypes[allowedChildrenTypes.length - 1].getSimpleName());
			sb.append(" are allowed.");
		} else if (allowedChildrenTypes.length == 1) {
			sb.append("Only");
			sb.append(allowedChildrenTypes[0].getSimpleName());
			sb.append(" is allowed");
		} else {
			sb.append("No children is allowed.");
		}
		return sb.toString();
	}

}
