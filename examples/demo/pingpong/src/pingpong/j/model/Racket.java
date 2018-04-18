package pingpong.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import pingpong.j.model.signals.Ball;
import pingpong.j.model.signals.BallIfc;
import pingpong.j.model.signals.HitOrMissIfc;
import pingpong.j.model.signals.HitTheBall;
import pingpong.j.model.signals.MissedTheBall;

public class Racket extends ModelClass {

	@BehaviorPort
	public class BallAtRacketPort extends Port<BallIfc, BallIfc> {
	}
	
	@BehaviorPort
	public class HitOrMissPort extends OutPort<HitOrMissIfc> {
	}
	
	public class Init extends Initial {
	}

	public class Waiting extends State {
	}

	@From(Init.class)
	@To(Waiting.class)
	class Initialize extends Transition {
	}

	public class Check extends Choice {
	}

	@From(Waiting.class)
	@To(Check.class)
	@Trigger(Ball.class)
	class ReceiveBall extends Transition {
	}

	@From(Check.class)
	@To(Waiting.class)
	class CanHit extends Transition {

		@Override
		public boolean guard() {
			return getTrigger(Ball.class).countdown > 0;
		}

		@Override
		public void effect() {
			Ball ball = getTrigger(Ball.class);
			Action.send(new Ball(ball.countdown - 1), port(BallAtRacketPort.class).required::reception);
			Action.send(new HitTheBall(), port(HitOrMissPort.class).required::reception);
		}

	}

	@From(Check.class)
	@To(Waiting.class)
	class CannotHit extends Transition {

		@Override
		public boolean guard() {
			//return Else(); not supported yet in export
			return getTrigger(Ball.class).countdown <= 0;
		}

		@Override
		public void effect() {
			Action.send(new MissedTheBall(), port(HitOrMissPort.class).required::reception);
		}

	}

}
