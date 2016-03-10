package pingpong.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import pingpong.j.model.signals.PingInterface;
import pingpong.j.model.signals.PingSignal;
import pingpong.j.model.signals.PongInterface;
import pingpong.j.model.signals.PongSignal;

public class InnerPongClass extends ModelClass {

	@BehaviorPort
	public class PongPort extends Port<PingInterface, PongInterface> {
	}

	@From(Waiting.class)
	@To(Waiting.class)
	@Trigger(PingSignal.class)
	class AcceptPing extends Transition {

		@Override
		public void effect() {
			PingSignal sig = getSignal();
			if (sig.count > 0) {
				Action.send(new PongSignal(sig.count - 1), port(PongPort.class).provided::reception);
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
