package hu.elte.txtuml.examples.exporttest.model;

public class OtherClassWithCtor extends OtherClass {

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