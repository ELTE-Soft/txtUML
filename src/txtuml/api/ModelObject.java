package txtuml.api;

public class ModelObject<T extends ModelClass> {	
	ModelObject(T obj) {
		this.object = obj;
		object.setSelf(this);
	}

	public final String getIdentifier() {
		if (object != null) {
			return object.getIdentifier();
		} else {
			return null;
		}
	}
	
	void deleteObject() {
		object = null;
	}
	
	T getObject() {
		return object;
	}
	
	private T object;
}