package hu.elte.txtuml.api;

/**
 * An immutable class representing integers in the model. Use of the Java
 * primitive types is completely disallowed in the model, except for constructor
 * parameters of {@link ModelType} objects.
 * <p>
 * <b>Represents:</b> an integer primitive value
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
 * ModelInt i = new ModelInt(100);
 * ModelInt j = i.add(new ModelInt(200));
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
public class ModelInt extends ModelType<Integer> {

	/**
	 * A <code>ModelInt</code> instance representing a one value.
	 */
	public static final ModelInt ONE = new ModelInt(1);

	/**
	 * A <code>ModelInt</code> instance representing a zero value.
	 */
	public static final ModelInt ZERO = new ModelInt(0);

	/**
	 * Creates a new <code>ModelInt</code> instance representing the specified
	 * integer value.
	 * 
	 * @param val
	 *            the integer value this new <code>ModelInt</code> instance will
	 *            represent
	 */
	public ModelInt(int val) {
		super(val);
	}

	/**
	 * Creates a new <code>ModelInt</code> instance representing the specified
	 * long value converted to integer.
	 * 
	 * @param val
	 *            the long value this new <code>ModelInt</code> instance will
	 *            represent after conversion to integer
	 */
	public ModelInt(long val) {
		super((int) val);
	}

	/**
	 * Creates a new <code>ModelInt</code> instance representing a zero value.
	 * TODO
	 */
	public ModelInt() {
		super(0);
	}

	/**
	 * Calculates <code>abs(</code><i>t</i><code>)</code> where <i>t</i> is the
	 * boolean value of this object. Returns a new <code>ModelBool</code>
	 * instance representing the result.
	 * 
	 * @return new <code>ModelBool</code> instance representing the result
	 */
	public ModelInt abs() {
		if (getValue() >= 0) {
			return this;
		} else {
			return negate();
		}
	}

	/**
	 * Calculates <i>t</i> <code>+</code> <i>v</i> where <i>t</i> is the integer
	 * value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelInt</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelInt</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelInt add(ModelInt val) {

		return new ModelInt(getValue() + val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code>/</code> <i>v</i> where <i>t</i> is the integer
	 * value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelInt</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelInt</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelInt divide(ModelInt val) {

		return new ModelInt(getValue() / val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code>%</code> <i>v</i> where <i>t</i> is the integer
	 * value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelInt</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelInt</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */

	public ModelInt remainder(ModelInt val) {

		return new ModelInt(getValue() % val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code>*</code> <i>v</i> where <i>t</i> is the integer
	 * value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelInt</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelInt</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelInt multiply(ModelInt val) {
		return new ModelInt(getValue() * val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code>-</code> <i>v</i> where <i>t</i> is the integer
	 * value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelInt</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelInt</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */

	public ModelInt subtract(ModelInt val) {

		return new ModelInt(getValue() - val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code>==</code> <i>v</i> where <i>t</i> is the
	 * integer value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool isEqual(ModelInt val) {
		return new ModelBool(getValue() == val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code><</code> <i>v</i> where <i>t</i> is the integer
	 * value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool isLess(ModelInt val) {
		return new ModelBool(getValue() < val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code><=</code> <i>v</i> where <i>t</i> is the
	 * integer value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool isLessEqual(ModelInt val) {
		return new ModelBool(getValue() <= val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code>></code> <i>v</i> where <i>t</i> is the integer
	 * value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */

	public ModelBool isMore(ModelInt val) {
		return new ModelBool(getValue() > val.getValue());
	}

	/**
	 * Calculates <i>t</i> <code>>=</code> <i>v</i> where <i>t</i> is the
	 * integer value of this object and <i>v</i> is the integer value of the
	 * <code>val</code> parameter. Returns a new <code>ModelBool</code> instance
	 * representing the result.
	 * 
	 * @param val
	 *            the other <code>ModelInt</code> instance to calculate with
	 * @return new <code>ModelBool</code> instance representing the result
	 * @throws NullPointerException
	 *             if <code>val</code> is <code>null</code>
	 */
	public ModelBool isMoreEqual(ModelInt val) {
		return new ModelBool(getValue() >= val.getValue());
	}

	/**
	 * Calculates <code>!</code><i>t</i> where <i>t</i> is the boolean value of
	 * this object. Returns a new <code>ModelBool</code> instance representing
	 * the result.
	 * 
	 * @return new <code>ModelBool</code> instance representing the result
	 */
	public ModelInt negate() {

		return new ModelInt(-getValue());
	}

	/**
	 * Calculates <code>signum(</code><i>t</i><code>)</code> where <i>t</i> is
	 * the boolean value of this object. Returns a new <code>ModelBool</code>
	 * instance representing the result.
	 * 
	 * @return new <code>ModelBool</code> instance representing the result
	 */
	public ModelInt signum() {
		return new ModelInt(Integer.signum(getValue()));
	}

}
