package hu.elte.txtuml.layout.lang.elements;

/**
 * Implementing classes represent groups of nodes in a diagram layout
 * description.
 * <p>
 * See {@link LayoutGroup} for more information on groups.
 * <p>
 * Should not be extended or implemented directly from outside this package with
 * the exception of the {@link hu.elte.txtuml.layout.lang.Diagram.NodeGroup
 * Diagram.NodeGroup} class.
 * 
 * @author Gabor Ferenc Kovacs
 * @see LayoutLink
 * @see LayoutLinkGroup
 * @see LayoutNode
 *
 */
public interface LayoutNodeGroup extends LayoutGroup, LayoutAbstractNode {
}
