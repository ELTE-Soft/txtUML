package pingpong.j.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import pingpong.j.model.Player.BallAtPlayerPort;
import pingpong.j.model.associations.LeftPlayer;
import pingpong.j.model.associations.RightPlayer;
import pingpong.j.model.connectors.Table;
import pingpong.j.model.signals.Ball;

public class Game extends ModelClass {

	public Game() {
		Player left = Action.create(Player.class, "Left ");
		Player right = Action.create(Player.class, "Right");

		Action.link(LeftPlayer.inGame.class, this, LeftPlayer.player.class, left);
		Action.link(RightPlayer.inGame.class, this, RightPlayer.player.class, right);

		Action.connect(Table.left.class, left.port(BallAtPlayerPort.class), Table.right.class,
				right.port(BallAtPlayerPort.class));

		Action.start(left);
		Action.start(right);
	}

	public class Init extends Initial {
	}

	public class Waiting extends State {
	}

	@From(Init.class)
	@To(Waiting.class)
	class Initialize extends Transition {
	}

	@From(Waiting.class)
	@To(Waiting.class)
	@Trigger(Ball.class)
	class StartGame extends Transition {

		@Override
		public void effect() {
			Player left = assoc(LeftPlayer.player.class).selectAny();
			Action.send(getTrigger(Ball.class), left);
		}

	}
}
