package hu.elte.txtuml.export.plantuml.tests.models.sequences;

public class LoopFragment extends BaseSequence {

	@Override
	public void run() {
		int i = 1;
		for (i = 1; i < 1; ++i) {

		}

		while (i == 2) {

		}

		do {

		} while (i == 2);
	}

}
