package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public interface BaseSequenceDiagramExecutor {
	boolean checkThread(Thread thread);

	Signal lastSignal();

	ModelClass lastSender();

	ModelClass lastReceiver();
}
