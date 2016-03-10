package pingpong.j.model.connectors;

import hu.elte.txtuml.api.model.Connector;
import pingpong.j.model.PingClass;
import pingpong.j.model.PongClass;
import pingpong.j.model.associations.TopClass_PingClass;
import pingpong.j.model.associations.TopClass_PongClass;

public class Ping_Pong_Connector extends Connector {
	public class pingEnd extends One<TopClass_PingClass.ping, PingClass.PingPort> {
	}

	public class pongEnd extends One<TopClass_PongClass.pong, PongClass.PongPort> {
	}
}
