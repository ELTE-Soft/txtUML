package hu.elte.txtuml.examples.pingpong;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.pingpong.xmodel.PongSignal;
import hu.elte.txtuml.examples.pingpong.xmodel.TopClass;

public class XPingPongTester {

	void test() {
		ModelExecutor.Settings.setExecutorLog(true);

		TopClass top = Action.create(TopClass.class);
		Action.start(top);

		Action.send(top, new PongSignal(4));

		ModelExecutor.shutdown();
	}

	public static void main(String[] args) {
		new PingPongTester().test();
	}

}
