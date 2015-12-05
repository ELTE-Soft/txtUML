package hu.elte.txtuml.export.uml2.transform.exporters;

import hu.elte.txtuml.export.uml2.transform.backend.ParameterMap;
import hu.elte.txtuml.export.uml2.utils.ActivityEditor;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.SequenceNode;

public class MethodBodyExporter extends ActivityEditor {

	private final ModelExporter modelExporter;
	private final ParameterMap params;

	private MethodBodyExporter(Activity activity, ModelExporter modelExporter) {
		super(activity);
		this.modelExporter = modelExporter;
		this.params = ParameterMap.create(this);
	}

	public static void export(Activity activity, ModelExporter modelExporter,
			MethodDeclaration methodDeclaration) {
		export(activity, modelExporter, methodDeclaration, null);
	}

	public static void export(Activity activity, ModelExporter modelExporter,
			MethodDeclaration methodDeclaration,
			Iterable<Parameter> parameterList) {

		new MethodBodyExporter(activity, modelExporter).exportMethodBody(
				methodDeclaration, parameterList);
	}

	private void exportMethodBody(MethodDeclaration methodDeclaration,
			Iterable<Parameter> parameterList) {
		
		if (parameterList != null) {
			parameterList.forEach(params::copyParameter);
		}

		ActivityNode initialNode = createInitialNode("initial_node");
		ActivityNode finalNode = createFinalNode("final_node");

		SequenceNode bodyNode = BlockExporter.exportBody(this, methodDeclaration.getBody());
		createControlFlowBetweenActivityNodes(initialNode, bodyNode);
		createControlFlowBetweenActivityNodes(bodyNode, finalNode);
	}
	
	public ParameterMap getParameters() {
		return params;
	}
	
	public TypeExporter getTypeExporter() {
		return modelExporter.getTypeExporter();
	}
	
}
