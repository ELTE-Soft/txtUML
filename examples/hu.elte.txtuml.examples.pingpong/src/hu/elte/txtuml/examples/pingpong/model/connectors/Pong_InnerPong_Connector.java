package hu.elte.txtuml.examples.pingpong.model.connectors;

import hu.elte.txtuml.api.model.Connector;
import hu.elte.txtuml.examples.pingpong.model.InnerPongClass;
import hu.elte.txtuml.examples.pingpong.model.PongClass;
import hu.elte.txtuml.examples.pingpong.model.associations.PongClass_InnerPongClass;

public class Pong_InnerPong_Connector extends Connector {
	public class pongEnd extends One<IN_CONTAINER, PongClass.PongPort> {
	}

	public class innerEnd extends One<PongClass_InnerPongClass.inner, InnerPongClass.PongPort> {
	}

}
