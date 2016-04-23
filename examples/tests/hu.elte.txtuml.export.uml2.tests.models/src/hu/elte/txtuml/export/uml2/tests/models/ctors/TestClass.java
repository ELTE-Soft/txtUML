package hu.elte.txtuml.export.uml2.tests.models.ctors;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void test() {
		Action.create(ClassWithCtors.class);
		Action.create(ClassWithCtors.class, 1);
		new ClassWithCtors();
		new ClassWithCtors(1);
		Action.create(DefaultConstructible.class);
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
