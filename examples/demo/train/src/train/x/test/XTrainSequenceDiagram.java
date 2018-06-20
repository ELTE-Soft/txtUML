package train.x.test;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.send;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import train.x.model.Backward;
import train.x.model.Engine;
import train.x.model.EngineOff;
import train.x.model.EngineOn;
import train.x.model.Forward;
import train.x.model.GE;
import train.x.model.GL;
import train.x.model.Gearbox;
import train.x.model.LE;
import train.x.model.Lamp;
import train.x.model.LightOff;
import train.x.model.SwitchLight;

public class XTrainSequenceDiagram extends SequenceDiagram {

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
		send(gearbox, new EngineOn(), engine);
		assertState(engine, Engine.Working.class);

		fromActor(new Forward(), gearbox);
		assertState(gearbox, Gearbox.Forwards.F2.class);
		fromActor(new Forward(), gearbox);
		assertState(gearbox, Gearbox.Forwards.F1.class);

		fromActor(new Backward(), gearbox);
		assertState(gearbox, Gearbox.Neutral.class);
		send(gearbox, new EngineOff(), engine);
		assertState(engine, Engine.Stopped.class);
		send(gearbox, new LightOff(), lamp);
		assertState(lamp, Lamp.Dark.class);

		fromActor(new Backward(), gearbox);
		assertState(gearbox, Gearbox.Backwards.B1.class);
		send(gearbox, new EngineOn(), engine);
		assertState(engine, Engine.Working.class);

		fromActor(new Backward(), gearbox);
		assertState(gearbox, Gearbox.Backwards.B2.class);
		fromActor(new Backward(), gearbox);
		assertState(gearbox, Gearbox.Backwards.B1.class);
	}

}
