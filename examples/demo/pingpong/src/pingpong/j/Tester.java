package pingpong.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import pingpong.j.model.TopClass;
import pingpong.j.model.signals.PongSignal;

public class Tester {

	void test() {
		ModelExecutor.Settings.setExecutorLog(true);

		TopClass top = Action.create(TopClass.class);
		Action.start(top);
		
		Action.send(new PongSignal(4), top);
		
		ModelExecutor.shutdown();
	}

	public static void main(String[] args) {
		new Tester().test();
	}

}