package hu.elte.txtuml.examples.pingpong.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.pingpong.model.PingClass.PingPort;
import hu.elte.txtuml.examples.pingpong.model.PongClass.PongPort;
import hu.elte.txtuml.examples.pingpong.model.associations.TopClass_PingClass;
import hu.elte.txtuml.examples.pingpong.model.associations.TopClass_PongClass;
import hu.elte.txtuml.examples.pingpong.model.connectors.Ping_Pong_Connector;
import hu.elte.txtuml.examples.pingpong.model.signals.PingInterface;
import hu.elte.txtuml.examples.pingpong.model.signals.PongInterface;
import hu.elte.txtuml.examples.pingpong.model.signals.PongSignal;

public class TopClass extends ModelClass {

	/**
	 * Creates an instance of ping and pong and connects them. If gets a pong
	 * signal, sends it to the ping instance.s
	 */
	public TopClass() {
		PingClass ping = Action.create(PingClass.class);
		PongClass pong = Action.create(PongClass.class);

		Action.link(TopClass_PingClass.top.class, this, TopClass_PingClass.ping.class, ping);
		Action.link(TopClass_PongClass.top.class, this, TopClass_PongClass.pong.class, pong);

		Action.connect(Ping_Pong_Connector.pingEnd.class, ping.port(PingPort.class), Ping_Pong_Connector.pongEnd.class,
				pong.port(PongPort.class));

		Action.start(ping);
		Action.start(pong);
	}

	@BehaviorPort
	public class TopPort extends Port<PongInterface, PingInterface> {
	}

	@From(Waiting.class)
	@To(Waiting.class)
	@Trigger(PongSignal.class)
	class AcceptPong extends Transition {

		@Override
		public void effect() {
			PingClass ping = assoc(TopClass_PingClass.ping.class).selectAny();
			Action.send(ping, getSignal(PongSignal.class));
		}

	}

	class Init extends Initial {
	}

	class Waiting extends State {
	}

	@From(Init.class)
	@To(Waiting.class)
	class Initialize extends Transition {
	}

}
