package hu.elte.txtuml.layout.lang.elements;

/**
 * Implementing classes represent a group of links in a diagram layout
 * description.
 * <p>
 * See {@link LayoutGroup} for more information on groups.
 * <p>
 * Should not be extended or implemented directly from outside this package,
 * with the exception of the
 * {@link hu.elte.txtuml.layout.lang.Diagram.LinkGroup Diagram.LinkGroup} class.
 * 
 * @author Gábor Ferenc Kovács
 * @see LayoutLink
 * @see LayoutNode
 * @see LayoutNodeGroup
 *
 */
public interface LayoutLinkGroup extends LayoutGroup, LayoutAbstractLink {
}
