package hu.elte.txtuml.layout.export.problems;

import java.lang.annotation.Annotation;

import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.source.ModelId;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

public class ProblemReporter {

	private final DiagramExportationReport report;

	public ProblemReporter(DiagramExportationReport report) {
		this.report = report;
	}

	// Errors

	public void moreThanOneLayoutClass(Class<?> layoutCls,
			Class<?> otherLayoutCls) {
		report.error("Layout classes "
				+ Utils.classAsString(layoutCls)
				+ " and "
				+ Utils.classAsString(otherLayoutCls)
				+ " are subclasses of the same diagram class. Only one layout class per diagram is allowed.");
	}

	public void noLayoutClass() {
		report.error("The diagram class has no inner class which is a subclass of Diagram.Layout.");
	}

	public void lineStatementExportationFailed(String name, Class<?>[] nodes) {
		statementExportationFailed(Utils.lineStatementAsString(name, nodes), 
				"node");
	}

	public void diamondStatementExportationFailed(Class<?> top, Class<?> right,
			Class<?> bottom, Class<?> left) {
		statementExportationFailed(Utils.diamondStatementAsString(top, right,
				bottom, left), "node");
	}

	public void adjacencyStatementExportationFailed(StatementType type,
			Class<?> val, Class<?> from) {
		statementExportationFailed(Utils.adjacencyStatementAsString(type, val,
				from), "node");
	}

	public void cardinalStatementExportationFailed(StatementType type,
			Class<?>[] val, Class<?>[] from, LinkEnd end) {
		statementExportationFailed(Utils.cardinalStatementAsString(type, val,
				from, end), "(node, node), (link, node)");
	}

	public void mostStatementExportationFailed(
			Class<? extends Annotation> type, Class<?>[] val) {
		statementExportationFailed(Utils.mostStatementAsString(type, val), "node");
	}

	public void multipleMostStatement(Class<? extends Annotation> type,
			Class<?>[] val) {
		report.error("Statement " + Utils.mostStatementAsString(type, val)
				+ " is illegal. Only one *most statement is allowed per type.");
	}

	public void invalidGroup(Class<?> groupClass, Class<?> elementClass) {
		report.error("Exportation of group " + Utils.classAsString(groupClass)
				+ " failed, as at least one of its elements ("
				+ Utils.classAsString(elementClass) + ") is invalid.");
	}

	public void invalidAnonymousGroup(Class<?>[] elementClasses,
			Class<?> elementClass) {
		report.error("Exportation of anonymous group {"
				+ Utils.classArrayAsString(elementClasses)
				+ "} failed, as at least one of its elements ("
				+ Utils.classAsString(elementClass) + ") is invalid.");
	}

	public void selfContainment(Class<?> groupClass) {
		report.error("Group " + Utils.classAsString(groupClass)
				+ " contains itself.");
	}

	public void elementFromAnotherModel(ModelId model,
			ModelId otherModel, Class<?> element) {
		report.error("Model element "
				+ Utils.classAsString(element)
				+ " is invalid because it is part of the model "
				+ otherModel.getName()
				+ ". The current diagram definition belongs to the model "
				+ model.getName()
				+ " (as that is the container of the first found element inside the definition).");
	}

	public void priorityStatementWithInvalidElement(Class<?> cls,
			Class<?>[] val, int prior) {
		invalidElement(cls, Utils.priorityStatementAsString(val, prior),
				"link or link group");
	}

	public void showStatementWithInvalidElement(Class<?> cls, Class<?>[] value) {
		invalidElement(cls, Utils.showStatementAsString(value), "node or link");
	}

	public void unknownContainingModel(Class<?> cls) {
		report.error("Model element "
				+ Utils.classAsString(cls)
				+ " is invalid because it is part of an unknown model (its container could not be retrieved).");
	}

	// Warnings

	public void unknownInnerClassOfDiagram(Class<?> cls) {
		report.warning("Class "
				+ Utils.classAsString(cls)
				+ " should not be present in a diagram description, therefore it is ignored. Only subclasses of Diagram.Layout, Diagram.Phantom, Diagram.NodeGroup and Diagram.LinkGroup are allowed as inner classes of a diagram class.");
	}

	public void unknownAnnotationOnClass(Annotation annot, Class<?> cls) {
		report.warning("Annotation " + annot.toString()
				+ " does not represent a valid description element on class "
				+ Utils.classAsString(cls) + ", therefore it is ignored.");
	}

	public void sugarStatementWithEmptyArguments(String name) {
		emptyStatement(name + "({})");
	}

	public void cardinalStatementWithEmptyArguments(StatementType type,
			Class<?>[] val, Class<?>[] from, LinkEnd end) {
		emptyStatement(Utils.cardinalStatementAsString(type, val, from, end));
	}

	public void mostStatementWithEmptyArguments(
			Class<? extends Annotation> type, Class<?>[] val) {
		emptyStatement(Utils.mostStatementAsString(type, val));
	}

	public void priorityStatementWithEmptyArguments(Class<?>[] val, int prior) {
		emptyStatement(Utils.priorityStatementAsString(val, prior));
	}

	public void emptyGroup(Class<?> groupClass) {
		report.warning("Group " + Utils.classAsString(groupClass)
				+ " is empty, therefore it has no effect on the arrangement.");
	}

	public void groupWithoutContainsAnnotation(Class<?> groupClass) {
		report.warning("Group class "
				+ Utils.classAsString(groupClass)
				+ " has no Contains annotation, therefore it acts as an empty group, which has no effect on the arrangement.");
	}

	// Private helpers

	private void invalidElement(Class<?> cls, String statement, String valid) {
		report.error("Class " + Utils.classAsString(cls)
				+ " cannot be the argument of statement " + statement
				+ ". Acceptable model elements here: " + valid + ".");
	}

	private void statementExportationFailed(String statement, String valid) {
		report.error("Exportation of statement " + statement + " failed." +
				" Acceptable model elements here: " + valid + ".");
	}

	private void emptyStatement(String statement) {
		report.warning("Statement "
				+ statement
				+ " has no effect on the arrangement, as at least one of its arguments is empty.");
	}

}
