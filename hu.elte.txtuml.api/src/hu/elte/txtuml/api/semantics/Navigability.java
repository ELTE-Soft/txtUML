package hu.elte.txtuml.api.semantics;


@SuppressWarnings("javadoc")
public interface Navigability {

	public interface Navigable extends Navigability {
	}

	public interface NonNavigable extends Navigability {
	}

}
