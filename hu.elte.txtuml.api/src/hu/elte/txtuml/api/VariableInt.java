package hu.elte.txtuml.api;

/**
 * A mutable container of a <code>ModelInt</code> instance.
 * 
 * <p>
 * <b>Represents:</b> no model element directly
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * TODO
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> allowed
 * <li><i>Define subtype:</i> disallowed
 * </li>
 * </ul>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get
 * an overview on modeling in txtUML.
 *
 * @author Gábor Ferenc Kovács
 *
 */
public class VariableInt extends VariableType<Integer, ModelInt> {

	/**
	 * Creates a new <code>VariableInt</code> instance containing a zero value.
	 */
	public VariableInt() {
		super(ModelInt.ZERO);
	}

	/**
	 * Creates a new <code>VariableBool</code> instance containing the specified
	 * value.
	 * 
	 * @param value
	 *            the value of the created <code>VariableBool</code> instance
	 */
	public VariableInt(ModelInt value) {
		super(value);
	}

	/**
	 * Creates a new <code>VariableInt</code> instance containing a
	 * <code>ModelInt</code> that represents the specified integer value.
	 * 
	 * @param rawValue
	 *            the raw value this variable's value will represent
	 */
	public VariableInt(int rawValue) {
		super(new ModelInt(rawValue));
	}

	/**
	 * Changes this variable to contain a <code>ModelInt</code> that represents
	 * the specified integer value.
	 * 
	 * @param rawValue
	 *            the raw value this variable's new value will represent
	 */
	@Override
	public void set(Integer rawValue) {
		set(new ModelInt(rawValue));
	}

}
