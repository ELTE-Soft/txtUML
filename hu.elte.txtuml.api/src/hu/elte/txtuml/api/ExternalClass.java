package hu.elte.txtuml.api;

/**
 * Base class for external classes in the model which are classes which's
 * implementation is not part of the model. They might be used to bring external
 * features in the model.
 * 
 * <p>
 * <b>Represents:</b> external class
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * TODO
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
 * <li><i>Inherit from the defined subtype:</i> allowed</li></li>
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
