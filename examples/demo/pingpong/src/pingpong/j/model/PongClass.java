package pingpong.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import pingpong.j.model.associations.PongClass_InnerPongClass;
import pingpong.j.model.connectors.Pong_InnerPong_Connector;
import pingpong.j.model.signals.PingInterface;
import pingpong.j.model.signals.PongInterface;

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

	public class PongPort extends Port<PingInterface, PongInterface> {
	}

}
