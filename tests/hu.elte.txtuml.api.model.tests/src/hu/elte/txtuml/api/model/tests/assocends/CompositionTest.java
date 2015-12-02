package hu.elte.txtuml.api.model.tests.assocends;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.tests.assocends.CompositionTest.MyComposition.MyContainer;
import hu.elte.txtuml.api.model.tests.assocends.CompositionTest.MyComposition.MyPart;

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

}
