package hu.elte.txtuml.api.model.error.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public class PatternMismatchError extends InvalidMessageError {

	private static final long serialVersionUID = -2489869288687106259L;

	public PatternMismatchError(ModelClass patternSource, ModelClass modelSource, Signal patternSignal,
			Signal modelSignal, ModelClass patternTarget, ModelClass modelTarget) {
		super(patternSource, "The model diverged from the Sequence-diagram Specified behaviour:\n it sent: \n");
		if (modelSource != null) {
			this.message += "From: " + modelSource;
		}

		this.message += "\n To: " + modelTarget + "\n signal: " + modelSignal + "\n instead of: \n From: "
				+ patternSource + "\n To: " + patternTarget + "\n signal: " + patternSignal + "\n";
	}

}
