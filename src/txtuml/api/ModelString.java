package txtuml.api;

public class ModelString extends ModelType<String> {

	public static final ModelString EMPTY = new ModelString("");

	public ModelString(String val) {
		super(val);
	}

	public ModelString() {
		super("");
	}

	@Override
	public ModelString toMString() {
		return this;
	}

	public ModelString concat(ModelString val) {
		return new ModelString(getValue() + val.getValue());
	}

}
