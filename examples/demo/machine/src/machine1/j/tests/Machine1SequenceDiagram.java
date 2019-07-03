package machine1.j.tests;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import machine1.j.model.Machine;
import machine1.j.model.User;
import machine1.j.model.associations.Usage;
import machine1.j.model.signals.ButtonPress;
import machine1.j.model.signals.DoYourWork;

public class Machine1SequenceDiagram extends SequenceDiagram {

	Lifeline<Machine> machine;
	Lifeline<User> user1;
	Lifeline<User> user2;

	@Override
	public void initialize() {
		Machine m = Action.create(Machine.class);
		User u1 = Action.create(User.class);
		User u2 = Action.create(User.class);

		machine = Sequence.createLifeline(m);
		user1 = Sequence.createLifeline(u1);
		user2 = Sequence.createLifeline(u2);

		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
		Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);

		Action.log("Machine and users are starting.");
		Action.start(m);
		Action.start(u1);
		Action.start(u2);
	}

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		Sequence.fromActor(new DoYourWork(), user1);

		Sequence.assertSend(user1, new ButtonPress(), machine);
		Sequence.assertSend(user1, new ButtonPress(), machine);
		Sequence.assertSend(user1, new ButtonPress(), machine);
	}

}
