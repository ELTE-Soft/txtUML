package hu.elte.txtuml.export.plantuml.tests.sequences;

import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.FragmentType;

public class SeqFragment extends BaseSequence {

	@Override
	public void run() {
		seqFragment();
	}

	@FragmentType(CombinedFragmentType.SEQ)
	public void seqFragment() {

	}

}
