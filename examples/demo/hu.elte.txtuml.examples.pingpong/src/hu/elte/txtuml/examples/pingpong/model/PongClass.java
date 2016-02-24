package hu.elte.txtuml.examples.pingpong.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import hu.elte.txtuml.examples.pingpong.model.associations.PongClass_InnerPongClass;
import hu.elte.txtuml.examples.pingpong.model.connectors.Pong_InnerPong_Connector;
import hu.elte.txtuml.examples.pingpong.model.signals.PingInterface;
import hu.elte.txtuml.examples.pingpong.model.signals.PongInterface;

public class PongClass extends ModelClass {

	/**
	 * Creates an instance of inner pong and through a delegation connector,
	 * connects its port to the port of that instance.
	 */
	public PongClass() {
		InnerPongClass inner = Action.create(InnerPongClass.class);

		Action.link(PongClass_InnerPongClass.pong.class, this, PongClass_InnerPongClass.inner.class, inner);
		Action.connect(port(PongPort.class), Pong_InnerPong_Connector.innerEnd.class, inner.port(InnerPongClass.PongPort.class));
		Action.start(inner);
	}

	public class PongPort extends Port<PongInterface, PingInterface> {
	}

}
