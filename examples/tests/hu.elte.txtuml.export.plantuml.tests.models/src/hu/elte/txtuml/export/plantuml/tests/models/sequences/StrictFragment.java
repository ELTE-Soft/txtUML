package hu.elte.txtuml.export.plantuml.tests.models.sequences;

import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.FragmentType;

public class StrictFragment extends BaseSequence {

	@Override
	public void run() {
		strictFragment();
	}

	@FragmentType(CombinedFragmentType.STRICT)
	public void strictFragment() {

	}

}
