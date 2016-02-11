package hu.elte.txtuml.export.uml2.transform.exporters;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.SequenceNode;

import hu.elte.txtuml.export.uml2.transform.backend.ParameterMap;
import hu.elte.txtuml.export.uml2.utils.ActivityEditor;

public class MethodBodyExporter extends ActivityEditor {

	private final ModelExporter modelExporter;
	private final ParameterMap params;
	private ActivityNode initialNode;
	private ActivityNode finalNode;

	private MethodBodyExporter(Activity activity, ModelExporter modelExporter) {
		super(activity);
		this.modelExporter = modelExporter;
		this.params = ParameterMap.create(this);
	}

	public static void export(Activity activity, ModelExporter modelExporter, MethodDeclaration methodDeclaration) {
		export(activity, modelExporter, methodDeclaration, null);
	}

	public static void export(Activity activity, ModelExporter modelExporter, MethodDeclaration methodDeclaration,
			Iterable<Parameter> parameterList) {

		new MethodBodyExporter(activity, modelExporter).exportMethodBody(methodDeclaration, parameterList);
	}

	private void exportMethodBody(MethodDeclaration methodDeclaration, Iterable<Parameter> parameterList) {

		if (parameterList != null) {
			parameterList.forEach(params::copyParameter);
		}

		initialNode = createInitialNode("initial_node");
		finalNode = createFinalNode("final_node");

		Block body = methodDeclaration.getBody();
		if (body != null) {
			SequenceNode bodyNode = BlockExporter.exportBody(this, body);
			createControlFlowBetweenActivityNodes(initialNode, bodyNode);
			createControlFlowBetweenActivityNodes(bodyNode, finalNode);
		} else {
			createControlFlowBetweenActivityNodes(initialNode, finalNode);
		}
	}

	public ParameterMap getParameters() {
		return params;
	}

	public TypeExporter getTypeExporter() {
		return modelExporter.getTypeExporter();
	}

	public ActivityNode getFinalNode() {
		return finalNode;
	}

}
