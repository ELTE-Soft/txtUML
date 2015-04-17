package hu.elte.txtuml.api;

/**
 * A mutable container of a specified <code>ModelType</code> instance.
 * 
 * <p>
 * <b>Represents:</b> no model element directly
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * TODO using variables
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> allowed</li>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @param <T>
 *            the raw primitive type represented by this variable's value
 * @param <MT>
 *            the type of this variable (the type of its value)
 */
public abstract class VariableType<T, MT extends ModelType<T>> {

	/**
	 * The value of this variable.
	 */
	private MT value;

	/**
	 * Sole constructor of <code>VariableType</code>.
	 *
	 * @param value
	 *            the value of this variable; should not be <code>null</code>
	 */
	protected VariableType(MT value) {
		this.value = value;
	}

	/**
	 * @return the value of this variable
	 */
	public MT get() {
		return value;
	}

	/**
	 * Changes this variable to contain the specified value.
	 * 
	 * @param value
	 *            the new value of this variable
	 */
	public void set(MT value) {
		this.value = value;
	}

	/**
	 * Abstract method that changes this variable to contain an <code>MT</code>
	 * instance ({@code MT extends ModelType<T>}) that represents the specified
	 * primitive value.
	 * 
	 * @param rawValue
	 *            the raw primitive value this variable's new <code>MT</code>
	 *            value will represent
	 */
	public abstract void set(T rawValue);

	@Override
	public String toString() {
		return "var:" + value.toString();
	}
}
