package hu.elte.txtuml.layout.lang.elements;

/**
 * Implementing classes represent links in a diagram layout description.
 * <p>
 * Links are, for example, associations in a class diagram or transitions in a
 * state machine diagram.
 * <p>
 * Should not be extended or implemented directly from outside this package,
 * with the exception of types in the model API which are super types of classes
 * representing certain kinds of links.
 * 
 * @author Gábor Ferenc Kovács
 * @see LayoutLinkGroup
 * @see LayoutNode
 * @see LayoutNodeGroup
 *
 */
public interface LayoutLink extends LayoutElement {
}
