package hu.elte.txtuml.examples.validation.model;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.validation.model.helpers.Sig;

public class TransitionsWithoutSourceTargetOrTrigger extends ModelClass {

	public class Init extends Initial {
	}

	public class St extends State {
	}

	@From(Init.class)
	@To(St.class)
	@Trigger(Sig.class)
	public class Tr extends Transition {
	}
	
	public class St2 extends State {
	}

	@From(St.class)
	@To(St2.class)
	public class Tr2 extends Transition {
	}
	
	@From(Init.class)
	public class Tr3 extends Transition {
	}
	
	@To(Init.class)
	public class Tr4 extends Transition {
	}
	
}
