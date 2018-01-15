package hu.elte.txtuml.export.uml2.tests.models.operation;

import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {
	int op1(boolean b, String c) {
		return 0;
	}

	public void op2() {
	}
	
	private void op3() {
		
	}
	
	protected void op4() {
	}

	@External
	void external_op() {
	}

	@ExternalBody
	void external_body_op() {
	}

	static void static_op() {
	}
}
