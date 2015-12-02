package hu.elte.txtuml.api.model;

import static org.junit.Assert.*;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.Container;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.backend.MultipleContainerException;
import hu.elte.txtuml.api.model.CompositionTest.MyComposition.MyContainer;
import hu.elte.txtuml.api.model.CompositionTest.MyComposition.MyPart;
import hu.elte.txtuml.api.model.CompositionTest.MyComposition2.MyContainer2;
import hu.elte.txtuml.api.model.CompositionTest.MyComposition2.MyPart2;

public class CompositionTest {

	class A extends ModelClass {
	}

	class B extends ModelClass {
	}

	class MyComposition extends Composition {
		class MyContainer extends Container<A> {
		}

		class MyPart extends Many<B> {
		}
	}
	
	@Test
	public void tryComposition() throws Exception {
		A a = new A();
		Action.link(MyContainer.class, a, MyPart.class, new B());
		Action.link(MyContainer.class, a, MyPart.class, new B());
		Action.link(MyContainer.class, a, MyPart.class, new B());
		assertEquals(3, a.assoc(MyPart.class).count());
	}
	
	class MyComposition2 extends Composition {
		class MyContainer2 extends Container<A> {
		}

		class MyPart2 extends Many<B> {
		}
	}
	
	@Test(expected = MultipleContainerException.class)
	public void testCompositionValidate() throws Exception {
		A a1 = new A();
		A a2 = new A();
		B b = new B();
		a1.addToAssoc(MyPart.class, b);
		b.addToAssoc(MyContainer.class, a1);
		a2.addToAssoc(MyPart2.class, b);
		b.addToAssoc(MyContainer2.class, a2);
	}

}
