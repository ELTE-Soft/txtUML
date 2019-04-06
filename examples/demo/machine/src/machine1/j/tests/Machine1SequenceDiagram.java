package machine1.j.tests;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import machine1.j.model.Machine;
import machine1.j.model.User;
import machine1.j.model.associations.Usage;
import machine1.j.model.signals.ButtonPress;
import machine1.j.model.signals.DoYourWork;

public class Machine1SequenceDiagram extends SequenceDiagram {

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
			Sequence.assertSend(u1, new ButtonPress(), m);
		}
	}

}
