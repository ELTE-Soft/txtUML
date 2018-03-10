package hu.elte.txtuml.validation.sequencediagram;

import hu.elte.txtuml.validation.common.MessageLoader;

public final class Messages {
	public static final MessageLoader LOADER;

	static {
		// Create loader and load messages into fields.
		LOADER = new MessageLoader(Messages.class.getCanonicalName().toLowerCase());
		LOADER.fillFields(Messages.class);
	}
}
