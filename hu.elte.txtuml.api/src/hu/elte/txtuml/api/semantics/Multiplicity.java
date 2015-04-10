package hu.elte.txtuml.api.semantics;

@SuppressWarnings("javadoc")
public interface Multiplicity {

	public interface One extends Multiplicity {
	}

	public interface ZeroToOne extends Multiplicity {
	}

	public interface ZeroToUnlimited extends Multiplicity {
	}

	public interface OneToUnlimited extends Multiplicity {
	}

}
