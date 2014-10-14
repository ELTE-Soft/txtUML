package txtuml.api;

import txtuml.importer.InstructionImporter;
import txtuml.importer.MethodImporter;

public class ModelInt extends ModelType<Integer> {
	public ModelInt(int val) {
		super(val);
	}
	public ModelInt() {
		this(0);
	}
	public ModelInt abs() {
		if(getValue() >= 0) {
			return this;
		} else {
			return negate();
		}
	}
	public ModelInt add(ModelInt val) {
		
		if(MethodImporter.isImporting())
		{
			return InstructionImporter.add(this, val);
		}
		return new ModelInt(getValue() + val.getValue());
	}
	public ModelInt	compareTo(ModelInt val) {
		return new ModelInt(getValue().compareTo(val.getValue()));
	}
	public ModelInt divide(ModelInt val) {
		if(MethodImporter.isImporting())
		{
			return InstructionImporter.divide(this, val);
		}
		return new ModelInt(getValue() / val.getValue());
	}
	public ModelInt remainder(ModelInt val) {
		if(MethodImporter.isImporting())
		{
			return InstructionImporter.remainder(this, val);
		}
		return new ModelInt(getValue() % val.getValue());
	}
	public ModelInt multiply(ModelInt val) {
		if(MethodImporter.isImporting())
		{
			return InstructionImporter.multiply(this, val);
		}
		return new ModelInt(getValue() * val.getValue());
	}
	public ModelInt subtract(ModelInt val) {
		if(MethodImporter.isImporting())
		{
			return InstructionImporter.subtract(this, val);
		}
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
		if(MethodImporter.isImporting())
		{
			return InstructionImporter.negate(this);
		}
		return new ModelInt(-getValue());
	}
	public ModelInt signum() {
		if(MethodImporter.isImporting())
		{
			return InstructionImporter.signum(this);
		}
		return new ModelInt(Integer.signum(getValue()));
	}
	
	public static final ModelInt ONE = new ModelInt(1);
	public static final ModelInt ZERO = new ModelInt(0);
}

