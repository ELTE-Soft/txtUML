package hu.elte.txtuml.validation.visitors;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.InvalidChildrenElement;

public class VisitorBase extends ASTVisitor {

	protected ProblemCollector collector;

	public VisitorBase(ProblemCollector collector) {
		this.collector = collector;
	}

	protected void checkChildren(ASTNode node, Class<?>... allowedChildrenTypes) {
		List<?> strProp = node.structuralPropertiesForType();
		for (Object prop : strProp) {
			StructuralPropertyDescriptor spd = (StructuralPropertyDescriptor) prop;
			if (spd.isChildListProperty()) {
				for (Object child : ((List<?>) node.getStructuralProperty(spd))) {
					collector.setProblemStatus(childAllowed(child, allowedChildrenTypes), new InvalidChildrenElement(
							collector.getSourceInfo(), node, (ASTNode) child, allowedChildrenTypes));
				}
			} else if (spd.isChildProperty()) {
				Object child = node.getStructuralProperty(spd);
				collector.setProblemStatus(childAllowed(child, allowedChildrenTypes), new InvalidChildrenElement(
						collector.getSourceInfo(), node, (ASTNode) child, allowedChildrenTypes));
			}

		}
	}

	private boolean childAllowed(Object child, Class<?>... allowedChildrenTypes) {
		for (Class<?> allowedType : allowedChildrenTypes) {
			if (allowedType.isInstance(child)) {
				return false;
			}
		}
		return true;
	}

	public void check() {
	}

}
