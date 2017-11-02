package hu.elte.txtuml.api.stdlib.world;

import javax.swing.SwingUtilities;

import hu.elte.txtuml.api.model.External;

/**
 * A special {@link WorldObjectListener} that calls the signal handler methods
 * on the AWT event dispatching thread. Intended to be used in Swing
 * applications when the listener has to update the GUI.
 * <p>
 * <i>Implementation note:</i> calls {@link SwingUtilities#invokeLater}.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api.stdlib.world} package
 * or further details and examples about the services provided by the <i>txtUML
 * World</i> model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@External
public class SwingWorldObjectListener extends WorldObjectListener {

	@Override
	void acceptAny(SignalToWorld signal) {
		SwingUtilities.invokeLater(() -> {
			invokeHandler(signal);
		});
	}

}