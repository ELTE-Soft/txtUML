package train.x;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import train.x.model.Backward;
import train.x.model.Engine;
import train.x.model.Forward;
import train.x.model.GE;
import train.x.model.GL;
import train.x.model.Gearbox;
import train.x.model.LE;
import train.x.model.Lamp;
import train.x.model.SwitchLight;

public class Tester {
	Tester() {
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
				Action.send(new SwitchLight(), l);
			}

			Thread.sleep(2 * time);

			for (int i = 0; i < 3; i++) {
				Thread.sleep(3 * time);
				Action.log("");
				Action.send(new Forward(), g);

				Thread.sleep(time);
				Action.log("");
				Action.send(new Backward(), g);
			}

			ModelExecutor.shutdown();
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) throws Exception {
		new Tester();
	}
}
