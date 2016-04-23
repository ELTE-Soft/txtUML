package pingpong.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import pingpong.j.model.Racket.BallAtRacketPort;
import pingpong.j.model.Racket.HitOrMissPort;
import pingpong.j.model.associations.PlayerOwnsRacket;
import pingpong.j.model.connectors.PlayerHitsOrMisses;
import pingpong.j.model.connectors.PlayerUsesRacket;
import pingpong.j.model.signals.Ball;
import pingpong.j.model.signals.BallIfc;
import pingpong.j.model.signals.HitOrMissIfc;
import pingpong.j.model.signals.HitTheBall;
import pingpong.j.model.signals.MissedTheBall;

public class Player extends ModelClass {

	private String name;

	public class BallAtPlayerPort extends Port<BallIfc, BallIfc> {
	}

	@BehaviorPort
	public class ShoutPort extends OutPort<HitOrMissIfc> {
	}

	public Player(String name) {
		this.name = name;

		Racket racket = Action.create(Racket.class);
		Action.link(PlayerOwnsRacket.player.class, Player.this, PlayerOwnsRacket.racket.class, racket);
		Action.connect(port(BallAtPlayerPort.class), PlayerUsesRacket.racket.class,
				racket.port(BallAtRacketPort.class));
		Action.connect(port(ShoutPort.class), PlayerHitsOrMisses.withRacket.class, racket.port(HitOrMissPort.class));

		Action.start(racket);
	}

	class Init extends Initial {
	}

	class Waiting extends State {
	}

	@From(Init.class)
	@To(Waiting.class)
	class Initialize extends Transition {
	}

	@From(Waiting.class)
	@To(Waiting.class)
	@Trigger(HitTheBall.class)
	class ShoutAfterHittingTheBall extends Transition {

		@Override
		public void effect() {
			Action.log(name + ": \"HIT!\"");
		}

	}

	@From(Waiting.class)
	@To(Waiting.class)
	@Trigger(MissedTheBall.class)
	class ShoutAfterMissingTheBall extends Transition {

		@Override
		public void effect() {
			Action.log(name + ": \"MISS! I lost...\"");
		}

	}

	@From(Waiting.class)
	@To(Waiting.class)
	@Trigger(Ball.class)
	class Serve extends Transition {
		
		@Override
		public void effect() {
			Action.log(name + ": \"I'll serve now...!\"");
			Action.send(getSignal(Ball.class), assoc(PlayerOwnsRacket.racket.class).selectAny());
		}

	}
	
}
