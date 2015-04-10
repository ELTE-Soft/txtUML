package hu.elte.txtuml.api;

/**
 * An immutable class representing booleans in the model. Use of the Java
 * primitive types is completely disallowed in the model, except for constructor
 * parameters of {@link ModelType} objects.
 * <p>
 * <b>Represents:</b> a boolean primitive value
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
 * <p>
 * 
 * <pre>
 * <code>
 * ModelBool b = new ModelBool(true);
 * ModelBool b2 = b.xor(ModelBool.TRUE);
 * </code>
 * </pre>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @see ModelType
 *
 */
public class ModelBool extends ModelType<Boolean> {

	/**
	 * A <code>ModelBool</code> instance representing a <code>true</code> value.
	 */
	public static final ModelBool TRUE = new ModelBool(true);

	/**
	 * A <code>ModelBool</code> instance representing a <code>false</code>
	 * value.
	 */
	public static final ModelBool FALSE = new ModelBool(false);

	/**
	 * An <code>Else</code> instance.
	 */
	public static final ModelBool ELSE = new Else();

	/**
	 * <code>Else</code> is a special {@link ModelBool} which always represents
	 * <code>true</code>. It might be used as the return value of a transition's
	 * {@link StateMachine.Transition#guard() guard} to represent an else
	 * condition in the case of a transition from a choice pseudostate.
	 * 
	 * <p>
	 * <b>Represents:</b> the value of an else condition as a guard on a
	 * transition
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * 
	 * <pre>
	 * <code>
	 * {@literal @}From(ExampleChoice.class) {@literal @}To(TargetVertex.class)
	 * {@literal /}{@literal /} no {@literal @}Trigger annotation in case of a transition from a choice pseudostate 
	 * class MyTransition extends Transition {
	 * 	{@literal @}Override
	 * 	public ModelBool guard() {
	 * 		return new ModelBool.Else();
	 * 	}
	 * }
	 * </code>
	 * </pre>
	 * 
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public static final class Else extends ModelBool {

		/**
		 * Sole constructor of <code>Else</code>.
		 * <p>
		 * As a <code>ModelBool</code> instance, this will represent
		 * <code>true</code>.
		 */
		public Else() {
			super(true);
		}
	}

	/**
	 * Creates a new <code>ModelBool</code> instance representing the specified
	 * boolean value.
	 * 
	 * @param val
	 *            the boolean value this new <code>ModelBool</code> instance
	 *            will represent
	 */
	public ModelBool(boolean val) {
		super(val);
	}

	/**
	 * Creates a new <code>ModelBool</code> instance representing a
	 * <code>false</code> value.
	 */
	public ModelBool() {
		super(false);
	}

	/**
	 * Calculates <code>!</code><i>t</i> where <i>t</i> is the boolean value of
	 * this object. Returns a new <code>ModelBool</code> instance representing
	 * the result.
	 * 
	 * @return new <code>ModelBool</code> instance representing the result
	 */
	public ModelBool not() {
		return !getValue() ? TRUE : FALSE;
	}

	/**
	 * Calculates <i>t</i> <code>||</code> <i>v</i> where <i>t</i> is the
	 * boolean value of this object and <i>v</i> is the boolean value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelBool</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool or(ModelBool val) {
		return getValue() || val.getValue() ? TRUE : FALSE;
	}

	/**
	 * Calculates <i>t</i> <code>&&</code> <i>v</i> where <i>t</i> is the
	 * boolean value of this object and <i>v</i> is the boolean value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelBool</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool and(ModelBool val) {
		return getValue() && val.getValue() ? TRUE : FALSE;
	}

	/**
	 * Calculates <i>t</i> <code>^</code> <i>v</i> where <i>t</i> is the boolean
	 * value of this object and <i>v</i> is the boolean value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelBool</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool xor(ModelBool val) {
		return getValue() ^ val.getValue() ? TRUE : FALSE;
	}

	/**
	 * Calculates <i>t</i> <code>==</code> <i>v</i> where <i>t</i> is the
	 * boolean value of this object and <i>v</i> is the boolean value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelBool</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool equ(ModelBool val) {
		return getValue() == val.getValue() ? TRUE : FALSE;
	}

	/**
	 * Calculates <i>t</i> <code>!=</code> <i>v</i> where <i>t</i> is the
	 * boolean value of this object and <i>v</i> is the boolean value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelBool</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool notEqu(ModelBool val) {
		return getValue() != val.getValue() ? TRUE : FALSE;
	}

}
