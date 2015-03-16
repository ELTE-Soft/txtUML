package hu.elte.txtuml.layout.lang.elements;

/**
 * An abstraction to an element of a diagram layout description which is not a
 * group.
 * <p>
 * Is either a {@link LayoutLink} or a {@link LayoutNode}.
 * <p>
 * Should not be extended or implemented directly from outside this package.
 * 
 * @author Gábor Ferenc Kovács
 * @see LayoutGroup
 */
public interface LayoutNonGroupElement extends LayoutElement {
}
