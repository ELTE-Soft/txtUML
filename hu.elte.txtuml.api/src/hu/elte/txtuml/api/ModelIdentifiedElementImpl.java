package hu.elte.txtuml.api;

import java.util.concurrent.atomic.AtomicLong;

abstract class ModelIdentifiedElementImpl implements ModelIdentifiedElement {

	private static AtomicLong counter = new AtomicLong(0);
	private final String identifier;

	protected ModelIdentifiedElementImpl() {
		this.identifier = "inst_" + counter.addAndGet(1);
	}

	@Override
	public final String getIdentifier() {
		return identifier;
	}
}
