package train.j.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertSend;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import train.j.model.Engine;
import train.j.model.Gearbox;
import train.j.model.Lamp;
import train.j.model.associations.GE;
import train.j.model.associations.GL;
import train.j.model.associations.LE;
import train.j.model.signals.Backward;
import train.j.model.signals.EngineOff;
import train.j.model.signals.EngineOn;
import train.j.model.signals.Forward;
import train.j.model.signals.LightOff;
import train.j.model.signals.SwitchLight;

public class TrainSequenceDiagram extends SequenceDiagram {

	@Position(1)
	Lamp lamp;

	@Position(2)
	Gearbox gearbox;

	@Position(3)
	Engine engine;

	@Override
	public void initialize() {
		gearbox = Action.create(Gearbox.class);
		engine = Action.create(Engine.class);
		lamp = Action.create(Lamp.class);

		Action.link(GE.g.class, gearbox, GE.e.class, engine);
		Action.link(GL.g.class, gearbox, GL.l.class, lamp);
		Action.link(LE.l.class, lamp, LE.e.class, engine);

		Action.start(gearbox);
		Action.start(engine);
		Action.start(lamp);
	}

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		fromActor(new SwitchLight(), lamp);
		assertState(lamp, Lamp.Light.class);
		fromActor(new SwitchLight(), lamp);
		assertState(lamp, Lamp.Dark.class);
		fromActor(new SwitchLight(), lamp);

		fromActor(new Forward(), gearbox);
		assertState(gearbox, Gearbox.Forwards.F1.class);
		assertSend(gearbox, new EngineOn(), engine);
		assertState(engine, Engine.Working.class);

		fromActor(new Forward(), gearbox);
		assertState(gearbox, Gearbox.Forwards.F2.class);
		fromActor(new Forward(), gearbox);
		assertState(gearbox, Gearbox.Forwards.F1.class);

		fromActor(new Backward(), gearbox);
		assertState(gearbox, Gearbox.Neutral.class);
		assertSend(gearbox, new EngineOff(), engine);
		assertState(engine, Engine.Stopped.class);
		assertSend(gearbox, new LightOff(), lamp);
		assertState(lamp, Lamp.Dark.class);

		fromActor(new Backward(), gearbox);
		assertState(gearbox, Gearbox.Backwards.B1.class);
		assertSend(gearbox, new EngineOn(), engine);
		assertState(engine, Engine.Working.class);

		fromActor(new Backward(), gearbox);
		assertState(gearbox, Gearbox.Backwards.B2.class);
		fromActor(new Backward(), gearbox);
		assertState(gearbox, Gearbox.Backwards.B1.class);
	}

}
