package hu.elte.txtuml.export.uml2.utils;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityFinalNode;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.JoinNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

public class ActivityEditor extends AbstractActivityEditor<ExecutableNode> {

	private final Activity activity;

	public ActivityEditor(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity() {
		return this.activity;
	}

	@Override
	public ExecutableNode createAndAddNode(String name, EClass type) {
		return (ExecutableNode) createNode(name, type);
	}

	@Override
	public ActivityEdge createEdge(String name, EClass type) {
		return activity.createEdge(name, type);
	}

	@Override
	public Variable createVariable(String name, Type type) {
		return activity.createVariable(name, type);
	}

	public void addOwnedParameter(Parameter param) {
		activity.getOwnedParameters().add(param);
	}

	public InitialNode createInitialNode(String name) {
		return (InitialNode) createNode(name, UMLPackage.Literals.INITIAL_NODE);
	}

	public ActivityFinalNode createFinalNode(String name) {
		return (ActivityFinalNode) createNode(name, UMLPackage.Literals.ACTIVITY_FINAL_NODE);
	}

	public ActivityNode createNode(String name, EClass type) {
		return activity.createOwnedNode(name, type);
	}

	/**
	 * Creates a parameter node in the specified activity for a specified
	 * parameter.
	 * 
	 * @param param
	 *            The UML2 parameter.
	 * @return The created activity parameter node.
	 */
	public ActivityParameterNode createParameterNode(Parameter param) {
		String paramName = param.getName();
		Type paramType = param.getType();

		ActivityParameterNode paramNode = (ActivityParameterNode) createNode(paramName + "_paramNode",
				UMLPackage.Literals.ACTIVITY_PARAMETER_NODE);

		paramNode.setParameter(param);
		paramNode.setType(paramType);

		return paramNode;
	}

	/**
	 * Creates a merge node (and the necessary flows) to merge the two given
	 * nodes.
	 * 
	 * @param node1
	 *            The first node to merge.
	 * @param node2
	 *            The second node to merge.
	 * @return The created merge node.
	 */
	public MergeNode createMergeNode(ActivityNode node1, ActivityNode node2) {
		String name = "merge_" + node1.getName() + "_and_" + node2.getName();
		MergeNode result = (MergeNode) createAndAddNode(name, UMLPackage.Literals.MERGE_NODE);
		createControlFlowBetweenActivityNodes(node1, result);
		createControlFlowBetweenActivityNodes(node2, result);
		return result;
	}

	/**
	 * Creates a fork node (and the necessary flows) to the given two nodes.
	 * 
	 * @param name
	 *            The name of the fork node.
	 * @param node1
	 *            The first node to fork to.
	 * @param node2
	 *            The second node to fork tSo.
	 * @return The created fork node.
	 */
	public ForkNode forkToNodes(String name, ActivityNode node1, ActivityNode node2) {
		ForkNode result = (ForkNode) createNode(name, UMLPackage.Literals.FORK_NODE);

		createEdgeBetweenActivityNodes(result, node1);
		createEdgeBetweenActivityNodes(result, node2);

		return result;
	}

	/**
	 * Creates a join node (and the necessary flows) to join the two given
	 * nodes.
	 * 
	 * @param node1
	 *            The first node to join.
	 * @param node2
	 *            The second node to join.
	 * @return The created join node.
	 */
	public JoinNode joinNodes(ActivityNode node1, ActivityNode node2) {
		String name = "join_" + node1.getName() + "_and_" + node2.getName();
		JoinNode result = (JoinNode) createNode(name, UMLPackage.Literals.JOIN_NODE);
		createControlFlowBetweenActivityNodes(node1, result);
		createControlFlowBetweenActivityNodes(node2, result);
		return result;
	}

}
