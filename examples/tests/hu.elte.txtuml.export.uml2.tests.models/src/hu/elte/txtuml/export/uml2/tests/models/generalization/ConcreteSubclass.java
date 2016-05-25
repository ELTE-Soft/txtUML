package hu.elte.txtuml.export.uml2.tests.models.generalization;

public class ConcreteSubclass extends ConcreteBaseClass {
	
	public ConcreteSubclass() {
		super(3);
	}
	
	@Override
	void baseMethod() {
		super.baseMethod();
	}
	
	void newMethod() {
		baseMethod();
		baseField = 3;
	}
	
}