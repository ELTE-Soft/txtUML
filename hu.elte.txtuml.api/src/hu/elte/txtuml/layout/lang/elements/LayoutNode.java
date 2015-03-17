package hu.elte.txtuml.layout.lang.elements;

/**
 * Implementing classes represent nodes in a diagram layout description.
 * <p>
 * Nodes are, for example, classes in a class diagram or states and pseudostates
 * in a state machine diagram.
 * <p>
 * Should not be extended or implemented directly from outside this package,
 * with the exception of types in the model API which are super types of classes
 * representing certain kinds of nodes.
 * 
 * @author Gábor Ferenc Kovács
 * @see LayoutLink
 * @see LayoutLinkGroup
 * @see LayoutNodeGroup
 *
 */
public interface LayoutNode extends LayoutAbstractNode, LayoutNonGroupElement {
}
