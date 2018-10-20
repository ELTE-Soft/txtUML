package control;

import control.model.ControlUnit;
import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;

public class Demo implements Execution{
	
	ControlUnit unit;

	@Override
	public void initialization() {
		unit = Action.create(ControlUnit.class);
		Action.start(unit);
	}
	
	@Override
	public void during() {
	}

	public static void main(String[] args) throws Exception {
		new Demo().run();
	}

}
