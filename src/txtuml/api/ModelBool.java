package txtuml.api;

public class ModelBool extends ModelType<Boolean> {
	public ModelBool(boolean val) {
		super(val);
	}
	public ModelBool() {
		this(false);
	}
	
	public ModelBool not() {
		return new ModelBool(!getValue());
	}
	public ModelBool or(ModelBool val) {
		return new ModelBool(getValue() || val.getValue());
	}
	public ModelBool and(ModelBool val) {
		return new ModelBool(getValue() && val.getValue());
	}
	public ModelBool xor(ModelBool val) {
		return new ModelBool(getValue() ^ val.getValue());
	}
	public ModelBool equ(ModelBool val) {
		return new ModelBool(getValue() == val.getValue());
	}
	public ModelBool notEqu(ModelBool val) {
		return new ModelBool(getValue() != val.getValue());
	}	
}