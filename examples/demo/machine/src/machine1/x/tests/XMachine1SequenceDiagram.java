package machine1.x.tests;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import machine1.x.model.ButtonPress;
import machine1.x.model.DoYourWork;
import machine1.x.model.Machine;
import machine1.x.model.Usage;
import machine1.x.model.User;

public class XMachine1SequenceDiagram extends SequenceDiagram {

	Machine m;
	User u1;
	User u2;

	@Override
	public void initialize() {
		m = Action.create(Machine.class);
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
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		Sequence.fromActor(new DoYourWork(), u1);
		for (int i = 0; i < 3; ++i) {
			Sequence.send(u1, new ButtonPress(), m);
		}
	}

}
