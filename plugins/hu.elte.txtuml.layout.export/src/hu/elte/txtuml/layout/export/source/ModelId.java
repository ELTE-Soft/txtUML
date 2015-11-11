package hu.elte.txtuml.layout.export.source;

public interface ModelId {

	String getName();

	@Override
	boolean equals(Object obj);

}

class ModelIdImpl implements ModelId {

	private final Class<?> cls;

	public ModelIdImpl(Class<?> cls) {
		this.cls = cls;
	}

	@Override
	public String getName() {
		return cls.getCanonicalName();
	}

	@Override
	public int hashCode() {
		return cls.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModelIdImpl) {
			return cls == ((ModelIdImpl) obj).cls;
		}
		return false;
	}

	Class<?> getCls() {
		return cls;
	}

}