package hu.elte.txtuml.api;

import hu.elte.txtuml.api.primitives.ModelBool;
import hu.elte.txtuml.api.primitives.ModelInt;
import hu.elte.txtuml.api.primitives.ModelString;

@ModelAnnotatedElement
public class ExternalClass extends Action {

	protected ExternalClass() {
	}

	protected static <T> T convert(ModelType<T> objectToConvert) {
		return objectToConvert.getValue();
	}

	protected static Integer convertModelInt(ModelInt intToConvert) {
		return convert(intToConvert);
	}

	protected static Boolean convertModelBool(ModelBool boolToConvert) {
		return convert(boolToConvert);
	}

	protected static String convertModelString(ModelString stringToConvert) {
		return convert(stringToConvert);
	}

}
