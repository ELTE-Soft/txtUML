package hu.elte.txtuml.layout.export.problems;

import java.lang.annotation.Annotation;

import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.lang.LinkEnd;
import hu.elte.txtuml.layout.lang.elements.LayoutAbstractLink;
import hu.elte.txtuml.layout.lang.elements.LayoutAbstractNode;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

public class ProblemReporter {
    
    private DiagramExportationReport report;
    
    public ProblemReporter(DiagramExportationReport report) {
        this.report = report;
    }
    
    // Errors
    
    public void moreThanOneLayoutClass(Class<?> layoutCls, Class<?> otherLayoutCls) {
        report.error("Layout classes " + Utils.classAsString(layoutCls) + " and " + Utils.classAsString(otherLayoutCls) +
            " are subclasses of the same diagram class. Only one layout class per diagram is allowed.");
    }
    
    public void noLayoutClass() {
        report.error("The diagram class has no inner class which is a subclass of Diagram.Layout.");
    }
    
    public void lineStatementExportationFailed(String name, Class <? extends LayoutAbstractNode>[] nodes) {
        statementExportationFailed(Utils.lineStatementAsString(name, nodes));
    }
    
    public void diamondStatementExportationFailed(Class<? extends LayoutNode> top, Class<? extends LayoutNode> right,
        Class<? extends LayoutNode> bottom, Class<? extends LayoutNode> left)
    {
        statementExportationFailed(Utils.diamondStatementAsString(top, right, bottom, left));
    }
    
    public void adjacencyStatementExportationFailed(StatementType type, Class<? extends LayoutNode> val,
        Class<? extends LayoutNode> from)
    {
        statementExportationFailed(Utils.adjacencyStatementAsString(type, val, from));
    }
    
    public void cardinalStatementExportationFailed(StatementType type,
        Class<? extends LayoutElement>[] val,
        Class<? extends LayoutElement>[] from,
        LinkEnd end)
    {
        statementExportationFailed(Utils.cardinalStatementAsString(type, val, from, end));
    }
    
    public void mostStatementExportationFailed(Class<? extends Annotation> type, Class<? extends LayoutAbstractNode>[] val) {
        statementExportationFailed(Utils.mostStatementAsString(type, val));
    }
    
    public void multipleMostStatement(Class<? extends Annotation> type, Class<? extends LayoutAbstractNode>[] val) {
        report.error("Statement " + Utils.mostStatementAsString(type, val) + " is illegal. Only one *most statement is allowed per type.");
    }
    
    public void invalidGroup(Class<? extends LayoutElement> groupClass, Class<? extends LayoutElement> elementClass) {
        report.error("Exportation of group " + Utils.classAsString(groupClass) + " failed, as at least one of its elements (" +
            Utils.classAsString(elementClass) + ") is invalid.");
    }
    
    public void invalidAnonGroup(Class<? extends LayoutElement>[] elementClasses, Class<? extends LayoutElement> elementClass) {
        report.error("Exportation of anonymous group {" + Utils.classArrayAsString(elementClasses) +
            "} failed, as at least one of its elements (" + Utils.classAsString(elementClass) + ") is invalid.");       
    }
    
    public void selfContainment(Class<? extends LayoutElement> groupClass) {
        report.error("Group " + Utils.classAsString(groupClass) + " contains itself.");
    }
    
    public void hasInvalidDeclaringClass(Class<? extends LayoutElement> elementClass) {
        report.error("Model element " + Utils.classAsString(elementClass)
            + " is invalid due to its declaring class. Model elements present in a diagram description should be declared in the same subclass of Model.");
    }
    
    // Warnings
    
    public void unknownInnerClassOfDiagram(Class<?> cls) {
        report.warning("Class " + Utils.classAsString(cls) +
            " should not be present in a diagram description, therefore it is ignored. Only subclasses of Diagram.Layout, Diagram.Phantom, Diagram.NodeGroup and Diagram.LinkGroup are allowed as inner classes of a diagram class.");       
    }
    
    public void unknownAnnotationOnClass(Annotation annot, Class<?> cls) {
        report.warning("Annotation " + annot.toString() + " does not represent a valid description element on class " +
            Utils.classAsString(cls) + ", therefore it is ignored.");
    }
    
    public void sugarStatementWithEmptyArguments(String name) {
        emptyStatement(name + "({})");
    }
    
    public void cardinalStatementWithEmptyArguments(StatementType type,
        Class<? extends LayoutElement>[] val,
        Class<? extends LayoutElement>[] from,
        LinkEnd end)
    {
        emptyStatement(Utils.cardinalStatementAsString(type, val, from, end));
    }
    
    public void mostStatementWithEmptyArguments(Class<? extends Annotation> type, Class<? extends LayoutAbstractNode>[] val) {
        emptyStatement(Utils.mostStatementAsString(type, val));
    }
    
    public void priorityStatementWithEmptyArguments(Class<? extends LayoutAbstractLink>[] val, int prior) {
        emptyStatement(Utils.priorityStatementAsString(val, prior));
    }
    
    public void emptyGroup(Class<? extends LayoutElement> groupClass) {
        report.warning("Group " + Utils.classAsString(groupClass) + " is empty, therefore it has no effect on the arrangement.");
    }
    
    public void groupWithoutContainsAnnotation(Class<? extends LayoutElement> groupClass) {
        report.warning("Group class " + Utils.classAsString(groupClass) +
            " has no Contains annotation, therefore it acts as an empty group, which has no effect on the arrangement.");
    }
    
    // Private helpers
    
    private void statementExportationFailed(String statement) {
        report.error("Exportation of statement " + statement + " failed.");
    }
    
    private void emptyStatement(String statement) {
        report.warning("Statement " + statement + " has no effect on the arrangement, as at least one of its arguments is empty.");
    }
    
}
