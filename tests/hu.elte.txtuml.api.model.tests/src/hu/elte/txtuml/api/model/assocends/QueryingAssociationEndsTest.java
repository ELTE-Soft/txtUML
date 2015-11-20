package hu.elte.txtuml.api.model.assocends;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.base.TestsBase;
import hu.elte.txtuml.api.model.models.AssociationsModel.A;
import hu.elte.txtuml.api.model.models.AssociationsModel.Assoc1;
import hu.elte.txtuml.api.model.models.AssociationsModel.B;
import hu.elte.txtuml.api.model.models.AssociationsModel.Refl;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class QueryingAssociationEndsTest extends TestsBase {

	@Test
	public void test() {
		A a1 = new A(), a2 = new A(), a3 = new A();
		B b1 = new B();
		
		assertCollection(new A[] {}, b1.assoc(Assoc1.a.class));
		
		Action.link(Assoc1.a.class, a1, Assoc1.b.class, b1);
		assertCollection(new A[] {a1}, b1.assoc(Assoc1.a.class));
		
		Action.link(Assoc1.a.class, a2, Assoc1.b.class, b1);
		assertCollection(new A[] {a1, a2}, b1.assoc(Assoc1.a.class));
		
		Action.link(Assoc1.a.class, a3, Assoc1.b.class, b1);
		assertCollection(new A[] {a1, a2, a3}, b1.assoc(Assoc1.a.class));
		
		Action.link(Assoc1.a.class, a1, Assoc1.b.class, b1);
		assertCollection(new A[] {a1, a2, a3}, b1.assoc(Assoc1.a.class));

		Action.unlink(Assoc1.a.class, a1, Assoc1.b.class, b1);
		assertCollection(new A[] {a2, a3}, b1.assoc(Assoc1.a.class));

		Action.unlink(Assoc1.a.class, a3, Assoc1.b.class, b1);
		assertCollection(new A[] {a2}, b1.assoc(Assoc1.a.class));

		Action.unlink(Assoc1.a.class, a2, Assoc1.b.class, b1);
		assertCollection(new A[] {}, b1.assoc(Assoc1.a.class));

		
		
		Action.link(Refl.a1.class, a1, Refl.a2.class, a2);
		assertCollection(new A[] {}, a1.assoc(Refl.a1.class));
		assertCollection(new A[] {a2}, a1.assoc(Refl.a2.class));
		assertCollection(new A[] {a1}, a2.assoc(Refl.a1.class));
		assertCollection(new A[] {}, a2.assoc(Refl.a2.class));

		Action.link(Refl.a1.class, a2, Refl.a2.class, a1);
		assertCollection(new A[] {a2}, a1.assoc(Refl.a1.class));
		assertCollection(new A[] {a2}, a1.assoc(Refl.a2.class));
		assertCollection(new A[] {a1}, a2.assoc(Refl.a1.class));
		assertCollection(new A[] {a1}, a2.assoc(Refl.a2.class));
		
		
		Action.link(Refl.a1.class, a1, Refl.a2.class, a1);
		assertCollection(new A[] {a1, a2}, a1.assoc(Refl.a1.class));
		assertCollection(new A[] {a1, a2}, a1.assoc(Refl.a2.class));
		assertCollection(new A[] {a1}, a2.assoc(Refl.a1.class));
		assertCollection(new A[] {a1}, a2.assoc(Refl.a2.class));

	}

	private static <T extends ModelClass> void assertCollection(T[] expecteds, Collection<T> collection) {
		Set<T> expected = new HashSet<>();		
		for (T e : expecteds) {
			expected.add(e);
		}
		
		Set<T> actual = new HashSet<>();
		collection.forEach(e -> actual.add(e));
		
		Assert.assertEquals(expected, actual);
	}
	
}
