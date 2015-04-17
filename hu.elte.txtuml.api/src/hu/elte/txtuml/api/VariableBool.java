package hu.elte.txtuml.api;

/**
 * A mutable container of a <code>ModelBool</code> instance.
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
	 *            the raw boolean value this variable's new
	 *            <code>ModelBool</code> value will represent
	 */
	@Override
	public void set(Boolean rawValue) {
		set(new ModelBool(rawValue));
	}

}
