package txtuml.api;

@ModelAnnotatedElement
public abstract class ExternalClass implements ModelElement {
	protected ExternalClass() {}
		
	protected <T> T convert(ModelType<T> objectToConvert) {
		return objectToConvert.getValue();
	}
	
	protected Integer convertModelInt(ModelInt intToConvert) {
		return convert(intToConvert);
	}

	protected Boolean convertModelBool(ModelBool boolToConvert) {
		return convert(boolToConvert);
	}
	
	protected String convertModelString(ModelString stringToConvert) {
		return convert(stringToConvert);
	}
}
