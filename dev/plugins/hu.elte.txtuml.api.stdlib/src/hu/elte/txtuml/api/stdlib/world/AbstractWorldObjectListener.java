package hu.elte.txtuml.api.stdlib.world;

import hu.elte.txtuml.api.model.External;

/**
 * Abstract {@link WorldObjectListener} to define custom listeners. Override the
 * {@link #acceptAny} method with the use of the {@link #invokeHandler} method.
 * <p>
 * Intended for complex use cases. Use the simpler {@code WorldObjectListener}
 * versions is possible.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api.stdlib.world} package
 * or further details and examples about the services provided by the <i>txtUML
 * World</i> model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@External
public abstract class AbstractWorldObjectListener extends WorldObjectListener {

	@Override
	protected abstract void acceptAny(SignalToWorld signal);

}