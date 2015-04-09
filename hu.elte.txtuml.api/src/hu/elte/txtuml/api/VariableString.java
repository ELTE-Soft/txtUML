package hu.elte.txtuml.api;

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
