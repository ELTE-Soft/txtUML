package machine3.j.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertSend;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import machine3.j.model.Machine;
import machine3.j.model.Machine.Off;
import machine3.j.model.Machine.On;
import machine3.j.model.User;
import machine3.j.model.User.NotWorking;
import machine3.j.model.associations.Usage;
import machine3.j.model.signals.ButtonPress;
import machine3.j.model.signals.DoTasks;
import machine3.j.model.signals.DoYourWork;

public class Machine3SequenceDiagram extends SequenceDiagram {

	@Position(2)
	Machine m;

	@Position(1)
	User u1;

	User u2;

	@Override
	public void initialize() {
		m = Action.create(Machine.class, 3);
		u1 = Action.create(User.class);
		u2 = Action.create(User.class);

		u1.name = "user1";
		u2.name = "user2";
		u1.id = 1;
		u2.id = 2;

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);

		Action.log("One of the users is starting to do his or her work.");
	}

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		fromActor(new DoYourWork(), u1);
		assertState(u1, NotWorking.class);
		assertState(m, Off.class);

		assertSend(u1, new ButtonPress(), m);
		assertSend(u1, new DoTasks(4), m);
		assertState(m, On.Active.class);
	}

}
