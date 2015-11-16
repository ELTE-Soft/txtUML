package hu.elte.txtuml.layout.export.source;

public interface ModelId {

	String getName();

	@Override
	boolean equals(Object obj);

}

class ModelIdImpl implements ModelId {

	private final Package topPackage;

	public ModelIdImpl(Package topPackage) {
		this.topPackage = topPackage;
	}

	@Override
	public String getName() {
		return topPackage.getName();
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

	Package getPackage() {
		return topPackage;
	}

}