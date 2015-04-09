package hu.elte.txtuml.api;

/**
 * Base class for immutable classes representing primitive types in the model.
 * Use of the Java primitive types is completely disallowed in the model, except
 * for constructor parameters of <code>ModelType</code> objects.
 * <p>
 * <b>Represents:</b> subclasses represent primitive values
 * <p>
 * <b>Usage:</b>
 * <p>
 * Should not be used directly. Instead, use its subclasses: {@link ModelBool},
 * {@link ModelInt}, {@link ModelString}
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @param <T>
 *            the Java type of the primitive type this object represents
 * @see ModelBool
 * @see ModelInt
 * @see ModelString
 *
 */
public class ModelType<T> implements ModelElement, ModelIdentifiedElement {

	/**
	 * The primitive value this immutable instance represents.
	 */
	private final T value;

	/**
	 * Create a new <code>ModelType</code> instance with the specified value.
	 * 
	 * @param val
	 *            the value the new instance will represent
	 */
	ModelType(T val) {
		super();
		value = val;
	}

	/**
	 * @return the value this instance represents
	 */
	final T getValue() {
		return value;
	}

	/**
	 * As <code>ModelType</code> is the model representation of a certain value,
	 * its string representation is the same as of the represented value's.
	 */
	@Override
	public String toString() {
		return value.toString();
	}

}
