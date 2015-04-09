package hu.elte.txtuml.api;

@ModelAnnotatedElement
public class ExternalClass extends Action {

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
	 * gained from the model in an external component. If any data is sent back
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
