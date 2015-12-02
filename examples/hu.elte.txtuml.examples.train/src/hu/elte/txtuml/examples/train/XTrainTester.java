package hu.elte.txtuml.examples.train;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.train.xmodel.Engine;
import hu.elte.txtuml.examples.train.xmodel.Gearbox;
import hu.elte.txtuml.examples.train.xmodel.Lamp;
import hu.elte.txtuml.examples.train.xmodel.GE;
import hu.elte.txtuml.examples.train.xmodel.GL;
import hu.elte.txtuml.examples.train.xmodel.LE;
import hu.elte.txtuml.examples.train.xmodel.Backward;
import hu.elte.txtuml.examples.train.xmodel.Forward;
import hu.elte.txtuml.examples.train.xmodel.SwitchLight;

public class XTrainTester {
	XTrainTester() {
		createInstances();
		test();
	}

	Gearbox g;
	Engine e;
	Lamp l;

	public void createInstances() {
		ModelExecutor.Settings.setExecutorLog(true);
		g = Action.create(Gearbox.class);
		e = Action.create(Engine.class);
		l = Action.create(Lamp.class);
		Action.link(GE.g.class, g, GE.e.class, e);
		Action.link(GL.g.class, g, GL.l.class, l);
		Action.link(LE.l.class, l, LE.e.class, e);
		Action.start(g);
		Action.start(e);
		Action.start(l);
	}

	public void test() {
		try {
			int time = 50;
			for (int i = 0; i < 3; i++) {
				Thread.sleep(time);
				Action.log("");
				Action.send(l, new SwitchLight());
			}

			Thread.sleep(2 * time);

			for (int i = 0; i < 3; i++) {
				Thread.sleep(3 * time);
				Action.log("");
				Action.send(g, new Forward());

				Thread.sleep(time);
				Action.log("");
				Action.send(g, new Backward());
			}

			ModelExecutor.shutdown();
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) throws Exception {
		new XTrainTester();
	}
}
