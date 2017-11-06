package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.seqdiag.RuntimeInfo;

public interface BaseWrapper<T> extends RuntimeInfo {
	T getWrapped();
}
