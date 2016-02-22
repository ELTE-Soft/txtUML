package hu.elte.txtuml.examples.exporttest.model;

import hu.elte.txtuml.api.model.*;

public class OtherClass extends ModelClass {

	public int fld;

	public OtherClass(int i) {
		this.fld = i;
	}

	public void superMethodCall() {

	}

	public void fieldAccess() {
		fld = 10;
	}

}
