package hu.elte.txtuml.api;

/**
 * Base class for external classes in the model. External classes are those
 * which's implementation is not part of the model, and from the scope of the
 * model they function as black boxes. They might be used to bring external
 * features into the model.
 * <p>
 * By the current implementation, external classes <b>are not exported</b> to
 * UML2, with the exception of standard library classes
 * {@link hu.elte.txtuml.stdlib.Timer Timer} and
 * {@link hu.elte.txtuml.stdlib.Timer.Handle Timer.Handle}. The planned design
 * is to include all external classes in the model with an 'external' stereotype
 * and empty operations.
 * 
 * <p>
 * <b>Represents:</b> external class
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * From outside the model, use external classes according to the rules and
 * conventions of the Java language. The only restriction is that an external
 * class may not be abstract because that could cause problems during model
 * execution or exportation. From the model, use them as any passive model class
 * (that is, one that may not react to any asynchronous events). Call its
 * methods as operations or use its fields as attributes. All its fields used
 * from inside the model must be of a type extending {@link ModelClass},
 * {@link ModelType} or {@link Collection}. Also all parameter and return types
 * of methods called from the model must extend one of these three types.
 * <p>
 * As the txtUML API uses its own thread for model execution, external classes
 * probably need synchronization.
 * <p>
 * When a method of an external class is called from the model (on the model's
 * executor thread), it can call back to methods of model classes, get or set
 * fields before it returns. However, if it is called from a different thread,
 * it may only communicate with the model through signals or by creating new
 * model objects, as it might be done from anywhere outside the model. For
 * details about managing the model from outside, see the documentation of the
 * {@link hu.elte.txtuml.api} package.
 * <p>
 * In case of primitive values, this class has specific protected methods to
 * convert <code>ModelType</code> objects back to their raw value (the primitive
 * values represented by them). These methods may only be used to convert data
 * gained from the model for an external component. If any data is sent back to
 * the model, it must be in the form of <code>ModelType</code> objects.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>none</li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> allowed</li>
 * <li><i>Constructors:</i> allowed</li>
 * <li><i>Initialization blocks:</i> allowed</li>
 * <li><i>Fields:</i> allowed</li>
 * <li><i>Methods:</i> allowed</li>
 * <li><i>Nested interfaces:</i> allowed</li>
 * <li><i>Nested classes:</i> allowed</li>
 * <li><i>Nested enums:</i> allowed</li>
 * </ul>
 * <li><i>Inherit from the defined subtype:</i> allowed
 * </ul>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public class ExternalClass {

	/**
	 * Sole constructor of <code>ExternalClass</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Protected because this class is intended to be inherited from but not
	 * instantiated. However, <code>ExternalClass</code> has to be a
	 * non-abstract class to make sure that it is instantiatable when that is
	 * needed for the API or the model exportation.
	 */
	protected ExternalClass() {
	}

	/**
	 * Convert a <code>ModelType</code> object back to its raw value (the
	 * primitive value represented by it). May only be used to convert data
	 * gained from the model for an external component. If any data is sent back
	 * to the model, it must be in the form of <code>ModelType</code> objects.
	 * 
	 * @param <T>
	 *            the Java type of the primitive type this object represents
	 * @param objectToConvert
	 *            the object to be converted
	 * @return the value represented by <code>objectToConvert</code>
	 */
	protected final static <T> T convert(ModelType<T> objectToConvert) {
		return objectToConvert.getValue();
	}

	/**
	 * Convert a <code>ModelBool</code> object back to its raw value (the
	 * boolean value represented by it). May only be used to convert data gained
	 * from the model in an external component. If any data is sent back to the
	 * model, it must be in the form of <code>ModelType</code> objects.
	 * 
	 * @param boolToConvert
	 *            the object to be converted
	 * @return the value represented by <code>boolToConvert</code>
	 */
	protected final static Boolean convertModelBool(ModelBool boolToConvert) {
		return convert(boolToConvert);
	}

	/**
	 * Convert a <code>ModelInt</code> object back to its raw value (the integer
	 * value represented by it). May only be used to convert data gained from
	 * the model in an external component. If any data is sent back to the
	 * model, it must be in the form of <code>ModelType</code> objects.
	 * 
	 * @param intToConvert
	 *            the object to be converted
	 * @return the value represented by <code>intToConvert</code>
	 */
	protected final static Integer convertModelInt(ModelInt intToConvert) {
		return convert(intToConvert);
	}

	/**
	 * Convert a <code>ModelString</code> object back to its raw value (the
	 * string value represented by it). May only be used to convert data gained
	 * from the model in an external component. If any data is sent back to the
	 * model, it must be in the form of <code>ModelType</code> objects.
	 * 
	 * @param stringToConvert
	 *            the object to be converted
	 * @return the value represented by <code>stringToConvert</code>
	 */
	protected final static String convertModelString(ModelString stringToConvert) {
		return convert(stringToConvert);
	}

}
