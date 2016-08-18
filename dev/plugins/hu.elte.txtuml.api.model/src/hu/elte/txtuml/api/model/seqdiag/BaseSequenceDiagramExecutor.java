package hu.elte.txtuml.api.model.seqdiag;

public interface BaseSequenceDiagramExecutor {

	public void addFragmentListener(FragmentListener listener);

	public void removeFragmentListener(FragmentListener listener);
}
