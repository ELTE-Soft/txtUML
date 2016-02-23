package hu.elte.txtuml.export.uml2.tests.models.send;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class A extends ModelClass {
	protected A connnectionVal;

	class Init extends Initial {
	}

	class SendAction extends State {

		@Override
		public void entry() {
			A inst1 = Action.create(A.class);
			B inst2 = Action.create(B.class);

			Action.link(A_B.ThisEnd.class, inst1, A_B.OtherEnd.class, inst2);
			
			Action.send(inst1.assoc(A_B.OtherEnd.class).selectAny(), new Sig());

			Action.unlink(A_B.ThisEnd.class, inst1, A_B.OtherEnd.class, inst2);
		}
	}

	@From(Init.class)
	@To(SendAction.class)
	class InitToSend extends Transition {
	}
}