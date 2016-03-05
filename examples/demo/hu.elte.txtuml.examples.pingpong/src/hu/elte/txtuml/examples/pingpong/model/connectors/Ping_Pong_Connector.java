package hu.elte.txtuml.examples.pingpong.model.connectors;

import hu.elte.txtuml.api.model.Connector;
import hu.elte.txtuml.examples.pingpong.model.PingClass;
import hu.elte.txtuml.examples.pingpong.model.PongClass;
import hu.elte.txtuml.examples.pingpong.model.associations.TopClass_PingClass;
import hu.elte.txtuml.examples.pingpong.model.associations.TopClass_PongClass;

public class Ping_Pong_Connector extends Connector {
	public class pingEnd extends One<TopClass_PingClass.ping, PingClass.PingPort> {
	}

	public class pongEnd extends One<TopClass_PongClass.pong, PongClass.PongPort> {
	}
}
