package hu.elte.txtuml.stdlib;

import hu.elte.txtuml.api.primitives.ModelBool;

public class VariableBool extends VariableType<Boolean, ModelBool> {

	public VariableBool() {
		super(ModelBool.FALSE);
	}

	public VariableBool(ModelBool value) {
		super(value);
	}

	public VariableBool(boolean rawValue) {
		super(new ModelBool(rawValue));
	}

	@Override
	public void set(Boolean rawValue) {
		set(new ModelBool(rawValue));
	}

}
