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

public class PingClass extends ModelClass {

	@BehaviorPort
	public class PingPort extends Port<PongInterface, PingInterface> {
	}
	
	@From(Waiting.class)
	@To(Waiting.class)
	@Trigger(PongSignal.class)
	class AcceptPong extends Transition {

		@Override
		public void effect() {
			PongSignal sig = getSignal(PongSignal.class);
			if (sig.count > 0) {
				Action.send(new PingSignal(sig.count - 1), port(PingPort.class).required::reception);				
				Action.log("ping");
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
