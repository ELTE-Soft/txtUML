package hu.elte.txtuml.api.stdlib.world;

import hu.elte.txtuml.api.model.External;

/**
 * A {@link WorldObjectListener} that invokes the appropriate signal handler
 * immediately when receiving a signal. That is, the handlers are called on
 * the <b><i>model executor thread</i></b> which manages the world object this
 * listener is registered for. Thread safety is the user's responsibility.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api.stdlib.world}
 * package or further details and examples about the services provided by
 * the <i>txtUML World</i> model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@External
public class ImmediateWorldObjectListener extends WorldObjectListener {

	@Override
	void acceptAny(SignalToWorld signal) {
		invokeHandler(signal);
	}

}