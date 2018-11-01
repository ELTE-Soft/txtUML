package hu.elte.txtuml.layout.visualizer.model;

/**
 * Enumeration for the special property of a {@link RectangleObject}.
 *
 */
public enum SpecialBox {
	/**
	 * No special property.
	 */
	None,
	/**
	 * Initial state.
	 */
	Initial,
	/**
	 * Final state.
	 */
	Final,
	/**
	 * Choice state.
	 */
	Choice,
	/**
	 * Join state.
	 */
	Join
}
