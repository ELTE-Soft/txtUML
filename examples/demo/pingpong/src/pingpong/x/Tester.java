package pingpong.x;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import pingpong.x.model.Game;
import pingpong.x.model.signals.Ball;

public class Tester {

	static void init() {
		Game game = Action.create(Game.class);
		Action.start(game);
		Action.send(new Ball(4), game);
	}

	public static void main(String[] args) {
		ModelExecutor.create().setLogLevel(LogLevel.TRACE).run(Tester::init);
	}

}
