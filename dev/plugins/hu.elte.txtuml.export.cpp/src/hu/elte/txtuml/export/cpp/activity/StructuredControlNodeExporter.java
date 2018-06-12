package hu.elte.txtuml.export.cpp.activity;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Clause;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.ExpansionKind;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates.VariableType;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ObjectDeclDefTemplates;

class StructuredControlNodeExporter {

	private ActivityExporter activityExporter;
	private UserVariableExporter userVariableExporter;
	private ActivityNodeResolver activityExportResolver;
	private ReturnNodeExporter returnNodeExporter;

	public StructuredControlNodeExporter(ActivityExporter activityExporter, ActivityNodeResolver activityExportResolver,
			UserVariableExporter userVariableExporter, ReturnNodeExporter returnNodeExporter) {
		this.activityExporter = activityExporter;
		this.activityExportResolver = activityExportResolver;
		this.userVariableExporter = userVariableExporter;
		this.returnNodeExporter = returnNodeExporter;
	}

	String createSequenceNodeCode(SequenceNode seqNode) {
		StringBuilder source = new StringBuilder("");
		returnNodeExporter.searchReturnNode(seqNode.getContainedEdges());
		source.append(createStructuredActivityNodeVariables(seqNode.getVariables()));
		for (ActivityNode aNode : seqNode.getNodes()) {
			source.append(activityExporter.createActivityNodeCode(aNode));
		}

		return source.toString();
	}

	String createExpansionRegionCode(ExpansionRegion node) {
		String source = "UNKNOWN_EXPANSION_REAGION";

		if (node.getMode().equals(ExpansionKind.ITERATIVE_LITERAL)) {

			Variable iterativeVar = node.getVariables().get(0);
			EList<ActivityNode> nodes = node.getNodes();
			String body = activityExporter.createActivityNodeCode(nodes.get(nodes.size() - 1));
			StringBuilder inits = new StringBuilder("");
			for (int i = 0; i < nodes.size() - 1; i++) {
				inits.append(activityExporter.createActivityNodeCode(nodes.get(i)));
			}

			String collection = activityExportResolver
					.getTargetFromActivityNode(node.getInputElements().get(0).getIncomings().get(0).getSource());
			source = ActivityTemplates.foreachCycle(iterativeVar.getType().getName(), iterativeVar.getName(),
					collection, body.toString(), inits.toString());
		}

		return source;

	}

	String createLoopNodeCode(LoopNode loopNode) {
		StringBuilder source = new StringBuilder("");
		source.append(createStructuredActivityNodeVariables(loopNode.getVariables()));

		for (ExecutableNode initNode : loopNode.getSetupParts()) {
			source.append(activityExporter.createActivityNodeCode(initNode));
		}

		StringBuilder condition = new StringBuilder("");
		for (ExecutableNode condNode : loopNode.getTests()) {
			condition.append(activityExporter.createActivityNodeCode(condNode));
		}
		source.append(condition);

		StringBuilder body = new StringBuilder("");
		for (ExecutableNode bodyNode : loopNode.getBodyParts()) {
			body.append(activityExporter.createActivityNodeCode(bodyNode));
		}

		StringBuilder recalulcateCondition = new StringBuilder("");
		for (ExecutableNode condNode : loopNode.getTests()) {
			recalulcateCondition.append(activityExporter.createActivityNodeCode(condNode));
		}

		source.append(
				ActivityTemplates.whileCycle(activityExportResolver.getTargetFromActivityNode(loopNode.getDecider()),
						body.toString() + "\n" + recalulcateCondition.toString()));

		return source.toString();
	}

	String createConditionalCode(ConditionalNode conditionalNode) {
		StringBuilder source = new StringBuilder("");
		StringBuilder tests = new StringBuilder("");
		StringBuilder bodies = new StringBuilder("");

		for (Clause clause : conditionalNode.getClauses()) {
			for (ExecutableNode test : clause.getTests()) {
				tests.append(activityExporter.createActivityNodeCode(test));
			}

			String cond = activityExportResolver.getTargetFromActivityNode(clause.getDecider());
			StringBuilder body = new StringBuilder("");
			for (ExecutableNode node : clause.getBodies()) {
				body.append(activityExporter.createActivityNodeCode(node));
			}

			bodies.append(ActivityTemplates.simpleIf(cond, body.toString()));

		}

		source.append(tests);
		source.append(bodies);
		return source.toString();
	}

	String createStructuredActivityNodeVariables(EList<Variable> variables) {
		StringBuilder source = new StringBuilder("");
		for (Variable variable : variables) {
			source.append(createVariable(variable));
		}

		return source.toString();
	}

	String createVariable(Variable variable) {
		String type = "!!!UNKNOWNTYPE!!!";
		if (variable.getType() != null) {
			type = variable.getType().getName();
		}
		userVariableExporter.exportNewVariable(variable);
		
		GenerationTemplates.VariableType varType = variable.getType().eClass().equals(UMLPackage.Literals.SIGNAL) ?
				GenerationTemplates.VariableType.EventPtr : VariableType.getUMLMultpliedElementType(variable.getLower(), variable.getUpper());
		return ObjectDeclDefTemplates.variableDecl(type, userVariableExporter.getRealVariableName(variable),varType);
	}
}
