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

	public ModelBool(boolean val, boolean literal) {
		super(val, literal);
	}

	public ModelBool(boolean val, boolean literal, String expression) {
		super(val, literal, expression);
	}

	public ModelBool(boolean val) {
		this(val, true);
	}

	public ModelBool() {
		this(false, false);
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

	public ModelBool isNull(Object o) {
		return o == null ? TRUE : FALSE;
	}

	public ModelBool isNotNull(Object o) {
		return o != null ? TRUE : FALSE;
	}
}
