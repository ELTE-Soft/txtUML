package hu.elte.txtuml.examples.pingpong.model.connectors;

import hu.elte.txtuml.api.model.Delegation;
import hu.elte.txtuml.examples.pingpong.model.InnerPongClass;
import hu.elte.txtuml.examples.pingpong.model.PongClass;
import hu.elte.txtuml.examples.pingpong.model.associations.PongClass_InnerPongClass;

public class Pong_InnerPong_Connector extends Delegation {
	public class pongEnd extends One<PongClass_InnerPongClass.pong, PongClass.PongPort> {
	}

	public class innerEnd extends One<PongClass_InnerPongClass.inner, InnerPongClass.PongPort> {
	}

}
