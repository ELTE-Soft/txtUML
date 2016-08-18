package hu.elte.txtuml.api.model.seqdiag;

public interface FragmentListener {

	default void FragmentStarted(CombinedFragmentType type) {
	};

	default void FragmentEnded(CombinedFragmentType type) {
	};
}
