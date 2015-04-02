package hu.elte.txtuml.api;

/**
 * An immutable class representing strings in the model. Use of the Java
 * primitive types is completely disallowed in the model, except for constructor
 * parameters of {@link ModelType} objects.
 * <p>
 * <b>Represents:</b> a string primitive value
 * <p>
 * <b>Usage:</b>
 * <p>
 * May be used, for example, as a field of a class, signal parameter or local
 * variable. As it is immutable, it can be used in a style very similar to using
 * primitive values.
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> allowed
 * <li><i>Define subtype:</i> disallowed
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * <p>
 * 
 * <pre>
 * <code>
 * ModelString s = new ModelString("mytext");
 * </code>
 * </pre>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gábor Ferenc Kovács
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
