package hu.elte.txtuml.export.uml2.tests.models.ctors;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void testParameterlessCtorCreate() {
		Action.create(ClassWithCtors.class);
	}
	
	public void testParameterlessCtor() {
		new ClassWithCtors();
	}
	
	public void testParameteredCtorCreate() {
		Action.create(ClassWithCtors.class, 1);
	}
	
	public void testParameteredCtor() {
		new ClassWithCtors(1);
	}
	
	public void testDefaultCtorCreate() {
		Action.create(DefaultConstructible.class);
	}
	
	public void testDefaultCtor() {
		new DefaultConstructible();
	}
	
}

class ClassWithCtors extends ModelClass {

	public ClassWithCtors() {
		this(4);
	}

	public ClassWithCtors(int i) {
	}
}

class DefaultConstructible extends ModelClass {
}
