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
 * Variables in the model might be used at almost any place where
 * {@link ModelType} instances are. The difference comes from a restriction that
 * a variable type cannot be the type of an attribute, neither be a return or
 * parameter type of a model class constructor or operation (
 * <code>VariableType</code> does not implement {@link ModelValue}).
 * <p>
 * The practical difference between instances of this class and
 * <code>ModelType</code> objects is that the former are mutable whereas the
 * latter are not. Therefore, <code>VariableType</code> instances may be used as
 * variables inside the code blocks and condition evaluations given as
 * parameters to
 * {@link Action#If(hu.elte.txtuml.api.blocks.Condition, hu.elte.txtuml.api.blocks.BlockBody, hu.elte.txtuml.api.blocks.BlockBody)
 * If},
 * {@link Action#While(hu.elte.txtuml.api.blocks.Condition, hu.elte.txtuml.api.blocks.BlockBody)
 * While} and
 * {@link Action#For(ModelInt, ModelInt, hu.elte.txtuml.api.blocks.ParameterizedBlockBody)
 * For} methods where Java lets the use of local variables of enclosing methods
 * only if they are final or effectively final.
 * 
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
 * VariableInt i = new VariableInt(10);
 * Action.While( () {@literal ->} i.get().isMore(ModelInt.ZERO), () {@literal ->} { 
 * 	Action.log("i is decreased by one");
 * 	i.set(i.get().subtract(ModelInt.ONE));	
 * });
 * </code>
 * </pre>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @param <T>
 *            the raw primitive type represented by this variable's value
 * @param <MT>
 *            the type of this variable (the type of its value)
 * @see VariableBool
 * @see VariableInt
 * @see VariableString
 * @see ModelType
 * 
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
	 * Returns the <code>ModelType</code> value of this variable instance.
	 * 
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
