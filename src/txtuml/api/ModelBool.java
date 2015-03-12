package txtuml.api;

public class ModelBool extends ModelType<Boolean> {

	public static final ModelBool TRUE = new ModelBool(true);
	public static final ModelBool FALSE = new ModelBool(false);
	public static final ModelBool ELSE = new Else();

	public static class Else extends ModelBool { // special ModelBool which
													// value is always false
		public Else() {
			super();
		}
	}

	public ModelBool(boolean val) {
		super(val);
	}

	public ModelBool() {
		super(false);
	}

	public ModelBool not() {
		return !getValue() ? TRUE : FALSE;
	}

	public ModelBool or(ModelBool val) {
		return getValue() || val.getValue() ? TRUE : FALSE;
	}

	public ModelBool and(ModelBool val) {
		return getValue() && val.getValue() ? TRUE : FALSE;
	}

	public ModelBool xor(ModelBool val) {
		return getValue() ^ val.getValue() ? TRUE : FALSE;
	}

	public ModelBool equ(ModelBool val) {
		return getValue() == val.getValue() ? TRUE : FALSE;
	}

	public ModelBool notEqu(ModelBool val) {
		return getValue() != val.getValue() ? TRUE : FALSE;
	}

}
