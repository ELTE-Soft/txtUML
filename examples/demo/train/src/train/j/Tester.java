package train.j;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.LogLevel;
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
		ModelExecutor executor = ModelExecutor.create().setLogLevel(LogLevel.TRACE).start(this::createInstances);
		test();
		executor.shutdown();
	}

	Gearbox g;
	Engine e;
	Lamp l;

	public void createInstances() {
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
				API.log("");
				API.send(new SwitchLight(), l);
			}

			Thread.sleep(2 * time);

			for (int i = 0; i < 3; i++) {
				Thread.sleep(3 * time);
				API.log("");
				API.send(new Forward(), g);

				Thread.sleep(time);
				API.log("");
				API.send(new Backward(), g);
			}

		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) throws Exception {
		new Tester();
	}
}
