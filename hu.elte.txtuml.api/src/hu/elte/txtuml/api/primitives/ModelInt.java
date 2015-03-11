package hu.elte.txtuml.api.primitives;

import hu.elte.txtuml.api.ModelType;

public class ModelInt extends ModelType<Integer> {

	public static final ModelInt ONE = new ModelInt(1);
	public static final ModelInt ZERO = new ModelInt(0);

	public ModelInt(int val) {
		super(val);
	}

	public ModelInt(long val) {
		super((int) val);
	}

	public ModelInt() {
		super(0);
	}

	public ModelInt abs() {
		if (getValue() >= 0) {
			return this;
		} else {
			return negate();
		}
	}

	public ModelInt add(ModelInt val) {

		return new ModelInt(getValue() + val.getValue());
	}

	public ModelInt divide(ModelInt val) {

		return new ModelInt(getValue() / val.getValue());
	}

	public ModelInt remainder(ModelInt val) {

		return new ModelInt(getValue() % val.getValue());
	}

	public ModelInt multiply(ModelInt val) {
		return new ModelInt(getValue() * val.getValue());
	}

	public ModelInt subtract(ModelInt val) {

		return new ModelInt(getValue() - val.getValue());
	}

	public ModelBool isEqual(ModelInt val) {
		return new ModelBool(getValue() == val.getValue());
	}

	public ModelBool isLess(ModelInt val) {
		return new ModelBool(getValue() < val.getValue());
	}

	public ModelBool isLessEqual(ModelInt val) {
		return new ModelBool(getValue() <= val.getValue());
	}

	public ModelBool isMore(ModelInt val) {
		return new ModelBool(getValue() > val.getValue());
	}

	public ModelBool isMoreEqual(ModelInt val) {
		return new ModelBool(getValue() >= val.getValue());
	}

	public ModelInt negate() {

		return new ModelInt(-getValue());
	}

	public ModelInt signum() {
		return new ModelInt(Integer.signum(getValue()));
	}

}
