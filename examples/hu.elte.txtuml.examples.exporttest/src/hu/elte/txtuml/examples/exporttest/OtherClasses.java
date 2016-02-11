package hu.elte.txtuml.examples.exporttest;

import hu.elte.txtuml.api.model.*;

class OtherClass extends ModelClass {

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

class OtherClassWithCtor extends OtherClass {

	public OtherClassWithCtor() {
		this(0);
	}

	public OtherClassWithCtor(int i) {
		super(i);
	}

	public void superFieldAccess() {
		super.fld = 3;
	}

	public void superMethodCall() {
		super.superMethodCall();
	}

}
