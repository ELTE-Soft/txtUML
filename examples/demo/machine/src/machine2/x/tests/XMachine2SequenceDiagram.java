package machine2.x.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import machine2.x.model.ButtonPress;
import machine2.x.model.DoTasks;
import machine2.x.model.DoYourWork;
import machine2.x.model.Machine;
import machine2.x.model.Machine.Off;
import machine2.x.model.Machine.On;
import machine2.x.model.Usage;
import machine2.x.model.User;

public class XMachine2SequenceDiagram extends SequenceDiagram {

	@Position(2)
	Lifeline<Machine> machine;

	@Position(1)
	Lifeline<User> user1;

	@Position(3)
	Lifeline<User> user2;

	@Override
	public void initialize() {
		Machine m = Action.create(Machine.class, 3);
		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		machine = Sequence.createLifeline(m);
		user1 = Sequence.createLifeline(u1);
		user2 = Sequence.createLifeline(u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);
	}

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		fromActor(new DoYourWork(), user1);
		assertState(machine, Off.class);

		Sequence.assertSend(user1, new ButtonPress(), machine);
		assertState(machine, On.Active.class);

		for (int i = 0; i < 3; ++i) {
			Sequence.assertSend(user1, new DoTasks(1), machine);
			assertState(machine, On.Active.class);
		}
	}

}
