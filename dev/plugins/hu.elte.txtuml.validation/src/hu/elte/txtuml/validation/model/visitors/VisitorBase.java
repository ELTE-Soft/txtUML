package hu.elte.txtuml.validation.model.visitors;

import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_CHILDREN_ELEMENT;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.model.Messages;

/**
 * Base class for visitors checking validity of java AST as txtUML model.
 */
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
						collector.report(
								INVALID_CHILDREN_ELEMENT.create(collector.getSourceInfo(), (ASTNode) child, nodeStr));
					}
				}
			} else if (spd.isChildProperty()) {
				Object child = node.getStructuralProperty(spd);
				if (child != null && childForbidden(child, allowedChildrenTypes)) {
					collector.report(
							INVALID_CHILDREN_ELEMENT.create(collector.getSourceInfo(), (ASTNode) child, nodeStr));
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

	/**
	 * Checks if the node is valid after visiting its children. Default
	 * implementation never reports a problem.
	 */
	public void check() {
	}

	protected void acceptChildren(TypeDeclaration elem, VisitorBase visitor) {
		for (Object decl : elem.bodyDeclarations()) {
			((BodyDeclaration) decl).accept(visitor);
		}
		visitor.check();
	}

	/**
	 * Method extracted to apply the applicable visitor to a type declaration
	 * that is a state machine element.
	 */
	public void handleStateMachineElements(TypeDeclaration elem) {
		if (ElementTypeTeller.isCompositeState(elem)) {
			checkChildren(elem, Messages.VisitorBase_composite_state_label,
					CompositeStateVisitor.ALLOWED_COMPOSITE_STATE_DECLARATIONS);
			acceptChildren(elem, new CompositeStateVisitor(collector));
		} else if (ElementTypeTeller.isState(elem)) {
			checkChildren(elem, Messages.VisitorBase_state_label, StateVisitor.ALLOWED_STATE_DECLARATIONS);
			acceptChildren(elem, new StateVisitor(collector));
		} else if (ElementTypeTeller.isInitialPseudoState(elem)) {
			checkChildren(elem, Messages.VisitorBase_initial_state_label,
					StateVisitor.ALLOWED_INITIAL_STATE_DECLARATIONS);
		} else if (ElementTypeTeller.isTransition(elem)) {
			checkChildren(elem, Messages.VisitorBase_transition_label,
					TransitionVisitor.ALLOWED_TRANSITION_DECLARATIONS);
			acceptChildren(elem, new TransitionVisitor(elem, collector));
		}
	}

}
