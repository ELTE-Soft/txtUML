package hu.elte.txtuml.api.primitives;


public class VariableString extends VariableType<String, ModelString> {

	public VariableString() {
		super(ModelString.EMPTY);
	}

	public VariableString(ModelString value) {
		super(value);
	}

	public VariableString(String rawValue) {
		super(new ModelString(rawValue));
	}

	@Override
	public void set(String rawValue) {
		set(new ModelString(rawValue));
	}

}
