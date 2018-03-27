package pingpong.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.LogLevel;
import pingpong.j.model.Game;
import pingpong.j.model.signals.Ball;

public class Tester implements Execution {

	@Override
	public void configure(Settings s) {
		s.logLevel = LogLevel.TRACE;
	}
	
	@Override
	public void initialization() {
		Game game = Action.create(Game.class);
		Action.start(game);
		Action.send(new Ball(4), game);
	}

	public static void main(String[] args) {
		new Tester().run();
	}

}

