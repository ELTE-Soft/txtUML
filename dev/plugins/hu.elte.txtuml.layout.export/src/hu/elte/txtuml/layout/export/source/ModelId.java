package hu.elte.txtuml.layout.export.source;

public interface ModelId {

	String getName();

	@Override
	boolean equals(Object obj);

}

class ModelIdImpl implements ModelId {

	private final String topPackage;

	public ModelIdImpl(String topPackage) {
		this.topPackage = topPackage.intern();
	}

	@Override
	public String getName() {
		return topPackage;
	}

	@Override
	public int hashCode() {
		return topPackage.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModelIdImpl) {
			return topPackage == ((ModelIdImpl) obj).topPackage;
		}
		return false;
	}

	String getPackage() {
		return topPackage;
	}

}