package train.j;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import train.j.model.Engine;
import train.j.model.Gearbox;
import train.j.model.Lamp;
import train.j.model.associations.GE;
import train.j.model.associations.GL;
import train.j.model.associations.LE;
import train.j.model.signals.Backward;
import train.j.model.signals.Forward;
import train.j.model.signals.SwitchLight;

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
