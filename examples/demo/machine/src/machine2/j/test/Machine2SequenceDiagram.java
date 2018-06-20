package machine2.j.test;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import machine2.j.model.Machine;
import machine2.j.model.Machine.Off;
import machine2.j.model.Machine.On;
import machine2.j.model.User;
import machine2.j.model.associations.Usage;
import machine2.j.model.signals.ButtonPress;
import machine2.j.model.signals.DoTasks;
import machine2.j.model.signals.DoYourWork;

public class Machine2SequenceDiagram extends SequenceDiagram {

	@Position(2)
	Machine m;

	@Position(1)
	User u1;

	@Position(3)
	User u2;

	@Override
	public void initialize() {
		m = Action.create(Machine.class, 3);
		u1 = Action.create(User.class);
		u2 = Action.create(User.class);

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);
	}

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		fromActor(new DoYourWork(), u1);
		assertState(m, Off.class);

		Sequence.send(u1, new ButtonPress(), m);
		assertState(m, On.Active.class);

		for (int i = 0; i < 3; ++i) {
			Sequence.send(u1, new DoTasks(1), m);
			assertState(m, On.Active.class);
		}
	}

}
