package hu.elte.txtuml.api;

public class VariableBool extends VariableType<Boolean, ModelBool> {

	/**
	 * Creates a new <code>VariableBool</code> instance containing a
	 * <code>ModelBool</code> that represents a <code>false</code> value.
	 */
	public VariableBool() {
		super(ModelBool.FALSE);
	}

	/**
	 * Creates a new <code>VariableBool</code> instance containing the specified
	 * value.
	 * 
	 * @param value
	 *            the value of the created <code>VariableBool</code> instance
	 */
	public VariableBool(ModelBool value) {
		super(value);
	}

	/**
	 * Creates a new <code>VariableBool</code> instance containing a
	 * <code>ModelBool</code> that represents the specified boolean value.
	 * 
	 * @param rawValue
	 *            the raw value this variable's new <code>ModelBool</code> value
	 *            will represent
	 */
	public VariableBool(boolean rawValue) {
		super(new ModelBool(rawValue));
	}

	/**
	 * Changes this variable to contain a <code>ModelBool</code> that represents
	 * the specified boolean value.
	 * 
	 * @param rawValue
	 *            the raw value this variable's new <code>ModelBool</code> value
	 *            will represent
	 */
	@Override
	public void set(Boolean rawValue) {
		set(new ModelBool(rawValue));
	}

}
