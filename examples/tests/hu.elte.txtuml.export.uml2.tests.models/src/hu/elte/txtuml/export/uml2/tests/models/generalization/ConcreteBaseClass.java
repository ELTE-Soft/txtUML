package hu.elte.txtuml.export.uml2.tests.models.generalization;

public class ConcreteBaseClass extends AbstractBaseClass {
	
	public ConcreteBaseClass(int i) {
		baseField = i;
	}
	
	int baseField;
	
	@Override
	void baseMethod() {}
	
}