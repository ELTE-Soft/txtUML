package machine1.x.tests;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import machine1.x.model.ButtonPress;
import machine1.x.model.DoYourWork;
import machine1.x.model.Machine;
import machine1.x.model.Usage;
import machine1.x.model.User;

public class XMachine1SequenceDiagram extends SequenceDiagram {

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
		for (int i = 0; i < 3; ++i) {
			Sequence.send(user1, new ButtonPress(), machine);
		}
	}

}
