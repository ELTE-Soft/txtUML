package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.FragmentListener;

public class FragmentCreationListener extends AbstractSequenceDiagramModelListener implements FragmentListener {

	public FragmentCreationListener(SequenceDiagramExecutor executor) {
		super(executor);
	}

	@Override
	public void FragmentStarted(CombinedFragmentType type) {

	};

	@Override
	public void FragmentEnded(CombinedFragmentType type) {

	};
}
