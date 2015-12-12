package hu.elte.txtuml.validation.visitors;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.general.InvalidChildrenElement;

public class VisitorBase extends ASTVisitor {

	protected ProblemCollector collector;

	public VisitorBase(ProblemCollector collector) {
		this.collector = collector;
	}

	/**
	 * Checks all the structural properties of a node if they are from the given
	 * classes.
	 */
	protected void checkChildren(ASTNode node, String nodeStr, Class<?>[] allowedChildrenTypes) {
		List<?> strProp = node.structuralPropertiesForType();
		for (Object prop : strProp) {
			StructuralPropertyDescriptor spd = (StructuralPropertyDescriptor) prop;
			if (spd.isChildListProperty()) {
				for (Object child : ((List<?>) node.getStructuralProperty(spd))) {
					if (childForbidden(child, allowedChildrenTypes)) {
						collector.setProblemStatus(
								new InvalidChildrenElement(collector.getSourceInfo(), nodeStr, (ASTNode) child));
					}
				}
			} else if (spd.isChildProperty()) {
				Object child = node.getStructuralProperty(spd);
				if (child != null && childForbidden(child, allowedChildrenTypes)) {
					collector.setProblemStatus(new InvalidChildrenElement(collector.getSourceInfo(), nodeStr, (ASTNode) child));
				}
			}

		}
	}

	private boolean childForbidden(Object child, Class<?>... allowedChildrenTypes) {
		for (Class<?> allowedType : allowedChildrenTypes) {
			if (allowedType.isInstance(child)) {
				return false;
			}
		}
		return true;
	}

	public void check() {
	}

	protected void acceptChildren(TypeDeclaration elem, VisitorBase visitor) {
		for (Object decl : elem.bodyDeclarations()) {
			((BodyDeclaration) decl).accept(visitor);
		}
		visitor.check();
	}

	public void handleStateMachineElements(TypeDeclaration elem) {
		if (ElementTypeTeller.isCompositeState(elem)) {
			checkChildren(elem, Messages.VisitorBase_composite_state_label, CompositeStateVisitor.ALLOWED_COMPOSITE_STATE_DECLARATIONS);
			acceptChildren(elem, new CompositeStateVisitor(collector));
		} else if (ElementTypeTeller.isState(elem)) {
			checkChildren(elem, Messages.VisitorBase_state_label, StateVisitor.ALLOWED_STATE_DECLARATIONS);
			acceptChildren(elem, new StateVisitor(collector));
		} else if (ElementTypeTeller.isInitialPseudoState(elem)) {
			checkChildren(elem, Messages.VisitorBase_initial_state_label, StateVisitor.ALLOWED_INITIAL_STATE_DECLARATIONS);
		} else if (ElementTypeTeller.isTransition(elem)) {
			checkChildren(elem, Messages.VisitorBase_transition_label, TransitionVisitor.ALLOWED_TRANSITION_DECLARATIONS);
			acceptChildren(elem, new TransitionVisitor(elem, collector));
		}
	}

}
