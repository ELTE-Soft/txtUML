package machine3.x.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertSend;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import machine3.x.model.ButtonPress;
import machine3.x.model.DoTasks;
import machine3.x.model.DoYourWork;
import machine3.x.model.Machine;
import machine3.x.model.Machine.Off;
import machine3.x.model.Machine.On;
import machine3.x.model.Usage;
import machine3.x.model.User;
import machine3.x.model.User.NotWorking;

public class XMachine3SequenceDiagram extends SequenceDiagram {

	@Position(2)
	Lifeline<Machine> machine;

	@Position(1)
	Lifeline<User> user1;

	Lifeline<User> user2;

	@Override
	public void initialize() {
		Machine m = Action.create(Machine.class, 3);
		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

		u1.name = "user1";
		u2.name = "user2";
		u1.id = 1;
		u2.id = 2;

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		machine = Sequence.createLifeline(m);
		user1 = Sequence.createLifeline(u1);
		user2 = Sequence.createLifeline(u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);

		Action.log("One of the users is starting to do his or her work.");
	}

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		fromActor(new DoYourWork(), user1);
		assertState(user1, NotWorking.class);
		assertState(machine, Off.class);

		assertSend(user1, new ButtonPress(), machine);
		assertSend(user1, new DoTasks(4), machine);
		assertState(machine, On.Active.class);
	}

}
