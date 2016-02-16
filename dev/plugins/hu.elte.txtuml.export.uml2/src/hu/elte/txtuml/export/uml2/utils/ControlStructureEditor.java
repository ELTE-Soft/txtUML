package hu.elte.txtuml.export.uml2.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Variable;

public class ControlStructureEditor extends AbstractActivityEditor {

	private final StructuredActivityNode controlStructure;
	private final EList<ExecutableNode> nodeList;

	public ControlStructureEditor(StructuredActivityNode controlStructure,
			EList<ExecutableNode> nodeList) {
		this.controlStructure = controlStructure;
		this.nodeList = nodeList;
	}
	
	public ControlStructureEditor(ControlStructureEditor editor) {
		this.controlStructure = editor.controlStructure;
		this.nodeList = editor.nodeList;
	}

	public EList<ExecutableNode> getNodeList() {
		return nodeList;
	}

	@Override
	public ActivityEdge createEdge(String name, EClass type) {
		return controlStructure.createEdge(name, type);
	}

	@Override
	public ExecutableNode createExecutableNode(String name, EClass type) {
		ExecutableNode node = (ExecutableNode) controlStructure.createNode(name, type);
		nodeList.add(node);
		return node;
	}

	@Override
	public Variable createVariable(String name, Type type) {
		return controlStructure.createVariable(name, type);
	}

}
