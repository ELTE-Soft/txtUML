package hu.elte.txtuml.stdlib;

import hu.elte.txtuml.api.primitives.ModelString;

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
