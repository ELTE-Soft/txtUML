package hu.elte.txtuml.export.uml2.tests.models.compound_ops;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestClass extends ModelClass {
	class Init extends Initial {
	}

	public int fld;
	
	class CompoundOps extends State {
		@Override
		public void entry() {
			fld += 10;
		}
	}

	@From(Init.class)
	@To(CompoundOps.class)
	class InitCompoundOps extends Transition {
	}
}
