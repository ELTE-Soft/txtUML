package hu.elte.txtuml.api.model;

/**
 * An immutable class representing strings in the model. Use of the Java
 * primitive types and <code>String</code>s is completely disallowed in the
 * model, except for parameters of constructors and setter methods of
 * {@link ModelType} and {@link VariableType} classes and their subclasses, and
 * logging methods of {@link Action} ( {@link Action#log(String) log},
 * {@link Action#logError(String) logError} ).
 * <p>
 * <b>Represents:</b> string primitive value
 * <p>
 * <b>Usage:</b>
 * <p>
 * May be used, for example, as a field of a class, signal parameter or local
 * variable. As it is immutable, it can be used in a style very similar to using
 * primitive values.
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> allowed</li>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * ModelString s = new ModelString("sampletext");
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @see ModelType
 * 
 */
public class ModelString extends ModelType<String> {

	/**
	 * A <code>ModelString</code> instance representing an empty string value.
	 */
	public static final ModelString EMPTY = new ModelString("");

	/**
	 * Creates a new <code>ModelString</code> instance representing the
	 * specified string value.
	 * 
	 * @param val
	 *            the string value this new <code>ModelString</code> instance
	 *            will represent
	 */
	public ModelString(String val) {
		super(val);
	}

	/**
	 * Creates a new <code>ModelString</code> instance representing an empty
	 * string value.
	 */
	public ModelString() {
		super("");
	}

}
