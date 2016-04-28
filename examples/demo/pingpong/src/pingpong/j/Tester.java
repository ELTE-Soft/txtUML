package pingpong.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import pingpong.j.model.Game;
import pingpong.j.model.signals.Ball;

public class Tester {

	void test() {
		ModelExecutor.Settings.setExecutorLog(true);

		Game game = Action.create(Game.class);
		Action.start(game);
		
		Action.send(new Ball(4), game);
		
		ModelExecutor.shutdown();
	}

	public static void main(String[] args) {
		new Tester().test();
	}

}