package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.seqdiag.RuntimeInfo;

public interface BaseMessageWrapper extends RuntimeInfo {
	Signal getSignal();

	ModelClass getSender();

	ModelClass getReceiver();

	@Override
	boolean equals(Object arg0);
}
