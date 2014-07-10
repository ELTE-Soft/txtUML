package txtuml.api;

public class ModelBool extends ModelType<Boolean> {
	public ModelBool(boolean val) {
		super(val);
	}
	public ModelBool() {
		this(false);
	}
}
