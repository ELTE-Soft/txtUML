package hu.elte.txtuml.api.layout;

/**
 * Base class for diagram layout descriptions.
 * <p>
 * <b>Example:</b>
 * <p>
 * 
 * <pre>
 * <code>
 * public class SampleDiagram extends Diagram {
 *  
 *   {@literal @North(val = A.class, from = B.class)}
 *   public class SampleLayout extends Layout {}
 * }
 * </code>
 * </pre>
 */
public abstract class Diagram {

	
	Diagram(){}
	
	/**
	 * Base class for phantom nodes.
	 */
	public abstract class Phantom {
	}

	/**
	 * Base class for node groups.
	 */
	public abstract class NodeGroup {
	}

	/**
	 * Base class for link groups.
	 */
	public abstract class LinkGroup {
	}

	/**
	 * Apply statements to a subclass of this class inside a subclass of
	 * {@link Diagram}.
	 */
	public abstract class Layout {
	}

}
