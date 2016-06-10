package pingpong.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import pingpong.j.model.Game;
import pingpong.j.model.signals.Ball;

public class Tester {

	static void init() {
		Game game = Action.create(Game.class);
		Action.start(game);
		Action.send(new Ball(4), game);
	}

	public static void main(String[] args) {
		ModelExecutor.create().setTraceLogging(true).run(Tester::init);
	}

}

