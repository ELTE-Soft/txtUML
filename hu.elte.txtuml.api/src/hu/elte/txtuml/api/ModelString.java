package hu.elte.txtuml.api;

public class ModelString extends ModelType<String> {

	public static final ModelString EMPTY = new ModelString("");

	public ModelString(String val) {
		super(val);
	}

	public ModelString() {
		super("");
	}

}
