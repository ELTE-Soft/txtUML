package hu.elte.txtuml.examples.pingpong.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.pingpong.model.signals.PingInterface;
import hu.elte.txtuml.examples.pingpong.model.signals.PingSignal;
import hu.elte.txtuml.examples.pingpong.model.signals.PongInterface;
import hu.elte.txtuml.examples.pingpong.model.signals.PongSignal;

public class InnerPongClass extends ModelClass {

	@BehaviorPort
	public class PongPort extends Port<PongInterface, PingInterface> {
	}

	@From(Waiting.class)
	@To(Waiting.class)
	@Trigger(PingSignal.class)
	class AcceptPing extends Transition {

		@Override
		public void effect() {
			PingSignal sig = getSignal(PingSignal.class);
			if (sig.count > 0) {
				Action.send(port(PongPort.class).provided::reception, new PongSignal(sig.count - 1));
				Action.log("pong");
			}
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
