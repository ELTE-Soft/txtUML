package hu.elte.txtuml.api;

/**
 * A mutable container of a <code>ModelString</code> instance.
 * 
 * <p>
 * <b>Represents:</b> no model element directly
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * See the documentation of {@link VariableType}.
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
 *
 */
public class VariableString extends VariableType<String, ModelString> {

	/**
	 * Creates a new <code>VariableString</code> instance containing a
	 * <code>ModelString</code> that represents an empty string value.
	 */
	public VariableString() {
		super(ModelString.EMPTY);
	}

	/**
	 * Creates a new <code>VariableString</code> instance containing the
	 * specified value.
	 * 
	 * @param value
	 *            the value of the created instance
	 */
	public VariableString(ModelString value) {
		super(value);
	}

	/**
	 * Creates a new <code>VariableString</code> instance containing a
	 * <code>ModelString</code> that represents the specified string value.
	 * 
	 * @param rawValue
	 *            the raw value this variable's new <code>ModelString</code>
	 *            value will represent
	 */
	public VariableString(String rawValue) {
		super(new ModelString(rawValue));
	}

	/**
	 * Changes this variable to contain a <code>ModelString</code> that
	 * represents the specified string value.
	 * 
	 * @param rawValue
	 *            the raw value this variable's new <code>ModelString</code>
	 *            value will represent
	 */
	@Override
	public void set(String rawValue) {
		set(new ModelString(rawValue));
	}

}
