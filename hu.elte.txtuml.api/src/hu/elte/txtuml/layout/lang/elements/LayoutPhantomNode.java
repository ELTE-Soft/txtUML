package hu.elte.txtuml.layout.lang.elements;

/**
 * Implementing classes represent phantom nodes in a diagram layout description.
 * <p>
 * A phantom node is a node which does not appear on generated the diagram, but
 * helps writing the layout description.
 * <p>
 * Should not be extended or implemented directly from outside this package with
 * the exception of the {@link hu.elte.txtuml.layout.lang.Diagram.Phantom Diagram.Phantom} class.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface LayoutPhantomNode extends LayoutNode {
}
