package pingpong.j.model.connectors;

import hu.elte.txtuml.api.model.Delegation;
import pingpong.j.model.InnerPongClass;
import pingpong.j.model.PongClass;
import pingpong.j.model.associations.PongClass_InnerPongClass;

public class Pong_InnerPong_Connector extends Delegation {
	public class pongEnd extends One<PongClass_InnerPongClass.pong, PongClass.PongPort> {
	}

	public class innerEnd extends One<PongClass_InnerPongClass.inner, InnerPongClass.PongPort> {
	}

}
