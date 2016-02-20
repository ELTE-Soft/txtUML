package hu.elte.txtuml.export.uml2.utils;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

public abstract class AbstractActivityEditor {

	public abstract ActivityEdge createEdge(String name, EClass type);
	public abstract ExecutableNode createExecutableNode(String name, EClass type);
	
	public abstract Variable createVariable(String name, Type type);

	/**
	 * Creates an activity edge from the source activity node to the target
	 * activity node. If one of the nodes is an object node, the edge will be an
	 * object flow. Otherwise, it will be a control flow.
	 * 
	 * @param source
	 *            The source activity node.
	 * @param target
	 *            The target activity node.
	 * @return The created activity edge.
	 */
	public ActivityEdge createEdgeBetweenActivityNodes(ActivityNode source,
			ActivityNode target) {
		if (source instanceof ObjectNode || target instanceof ObjectNode) {
			return createObjectFlowBetweenActivityNodes(source, target);
		} else {
			return createControlFlowBetweenActivityNodes(source, target);
		}
	}

	/**
	 * Creates a control flow from the source activity node to the target
	 * activity node.
	 * 
	 * @param source
	 *            The source activity node.
	 * @param target
	 *            The target activity node.
	 * @return The created control flow.
	 */
	public ActivityEdge createControlFlowBetweenActivityNodes(
			ActivityNode source, ActivityNode target) {
		ActivityEdge edge = createEdge(
				"controlflow_from_" + source.getName() + "_to_"
						+ target.getName(), UMLPackage.Literals.CONTROL_FLOW);
		edge.setSource(source);
		edge.setTarget(target);
		return edge;
	}

	/**
	 * Creates an object flow from the source activity node to the target
	 * activity node.
	 * 
	 * @param source
	 *            The source activity node.
	 * @param target
	 *            The target activity node.
	 * @return The created object flow.
	 */
	public ActivityEdge createObjectFlowBetweenActivityNodes(
			ActivityNode source, ActivityNode target) {
		ActivityEdge edge = createEdge(
				"objectflow_from_" + source.getName() + "_to_"
						+ target.getName(), UMLPackage.Literals.OBJECT_FLOW);
		edge.setSource(source);
		edge.setTarget(target);

		return edge;
	}

	public SequenceNode createSequenceNode(String name) {
		return (SequenceNode) createExecutableNode(name,
				UMLPackage.Literals.SEQUENCE_NODE);
	}
	
}
