package hu.elte.txtuml.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;

import hu.elte.txtuml.api.model.error.LowerBoundError;
import hu.elte.txtuml.api.model.error.UpperBoundError;

public class CollectionTests {

	@Test
	public void testActionCollect() {
		Any<Integer> a = API.collect(1, 2, 3, 4);
		Any<String> s = API.collect("A", "B", "C", "C", "B");
		Any<String> s2 = API.collect("A", "B", "C", "B", "C");

		assertThatContains(a, 1, 2, 3, 4);
		assertThatContains(s, "A", "B", "B", "C", "C");
		assertThatContains(s2, "A", "B", "B", "C", "C");
	}

	@Test
	public void testActionCollectIn() {
		Any<Integer> a = API.collectIn(Any.class, 1, 2, 3, 3);
		UniqueAny<String> s = API.collectIn(UniqueAny.class, "A", "B", "C", "C", "B");

		assertThatContains(a, 1, 2, 3, 3);
		assertThatContains(s, "A", "B", "C");
	}

	@Test
	public void testAddAndRemove() {
		Any<Integer> a = API.collect(1);

		assertThatContains(a, 1);

		a = a.add(2).remove(3).remove(1).add(3);

		assertThatContains(a, 2, 3);

		a = a.add(2).add(2).add(3);

		assertThatContains(a, 2, 2, 2, 3, 3);
	}

	@Test
	public void testIsEmptyAndSize() {
		Any<Integer> a = API.collect();

		assertThatContains(a);
		Assert.assertTrue(a.isEmpty());

		a = a.add(1);

		Assert.assertTrue(!a.isEmpty());

		a = a.add(2).remove(3).remove(1).add(3);

		assertThatContains(a, 2, 3);
		Assert.assertEquals(2, a.size());

		a = a.add(2).add(2).add(3);

		Assert.assertEquals(5, a.size());

	}

	@Test
	public void testContains() {
		Any<Integer> a = API.collect(2, 2, 2, 3, 3);
		Assert.assertTrue(!a.contains(1));
		Assert.assertTrue(a.contains(2));
		Assert.assertTrue(a.contains(3));
	}

	@Test
	public void testCountOf() {
		Any<Integer> a = API.collect(2, 2, 2, 3, 3);
		OrderedAny<Integer> a2 = API.collectIn(OrderedAny.class, 2, 2, 2, 3, 3);
		// ordered collections use a different implementation

		Assert.assertEquals(0, a.countOf(1));
		Assert.assertEquals(3, a.countOf(2));
		Assert.assertEquals(2, a.countOf(3));

		Assert.assertEquals(0, a2.countOf(1));
		Assert.assertEquals(3, a2.countOf(2));
		Assert.assertEquals(2, a2.countOf(3));
	}

	@Test
	public void testProperties() {
		Any<Integer> a = API.collect();
		OrderedAny<Integer> oa = API.collectIn(OrderedAny.class);
		UniqueAny<Integer> ua = API.collectIn(UniqueAny.class);
		OrderedUniqueAny<Integer> oua = API.collectIn(OrderedUniqueAny.class);
		One<Integer> o = API.collectIn(One.class, 0);

		Assert.assertTrue(!a.isOrdered());
		Assert.assertTrue(!a.isUnique());
		Assert.assertEquals(0, a.getLowerBound());
		Assert.assertEquals(GeneralCollection.INFINITE_BOUND, a.getUpperBound());

		Assert.assertTrue(oa.isOrdered());
		Assert.assertTrue(!oa.isUnique());

		Assert.assertTrue(!ua.isOrdered());
		Assert.assertTrue(ua.isUnique());

		Assert.assertTrue(oua.isOrdered());
		Assert.assertTrue(oua.isUnique());

		Assert.assertEquals(1, o.getLowerBound());
		Assert.assertEquals(1, o.getUpperBound());
	}

	@Test
	public void testCustomCollections() {
		TwoToFour<Integer> t2f = API.collectIn(TwoToFour.class, 2, 3, 4);
		OrderedTwoToFour<Integer> ot2f = API.collectIn(OrderedTwoToFour.class, 2, 3, 4);
		UniqueTwoToFour<Integer> ut2f = API.collectIn(UniqueTwoToFour.class, 2, 3, 4);
		OrderedUniqueTwoToFour<Integer> out2f = API.collectIn(OrderedUniqueTwoToFour.class, 2, 3, 4);
		TwoToAny<Integer> t2a = API.collectIn(TwoToAny.class, 2, 3, 4);

		assertThatContains(t2f, 2, 3, 4);
		assertThatContains(ot2f, 2, 3, 4);
		assertThatContains(ut2f, 2, 3, 4);
		assertThatContains(out2f, 2, 3, 4);
		assertThatContains(t2a, 2, 3, 4);

		Assert.assertEquals(2, t2f.getLowerBound());
		Assert.assertEquals(4, t2f.getUpperBound());
		Assert.assertEquals(2, ot2f.getLowerBound());
		Assert.assertEquals(4, ot2f.getUpperBound());
		Assert.assertEquals(2, ut2f.getLowerBound());
		Assert.assertEquals(4, ut2f.getUpperBound());
		Assert.assertEquals(2, out2f.getLowerBound());
		Assert.assertEquals(4, out2f.getUpperBound());
		Assert.assertEquals(2, t2a.getLowerBound());
		Assert.assertEquals(GeneralCollection.INFINITE_BOUND, t2a.getUpperBound());
	}

	@Test
	public void testOne() {
		Any<Integer> a = API.collect(1, 2, 3);

		Assert.assertTrue(Arrays.asList(1, 2, 3).contains(a.one()));
	}

	@Test
	public void testUnbound() {
		TwoToFour<Integer> t2f = API.collectIn(TwoToFour.class, 2, 3, 4);
		OrderedTwoToFour<Integer> ot2f = API.collectIn(OrderedTwoToFour.class, 2, 3, 4);
		UniqueTwoToFour<Integer> ut2f = API.collectIn(UniqueTwoToFour.class, 2, 3, 4);
		OrderedUniqueTwoToFour<Integer> out2f = API.collectIn(OrderedUniqueTwoToFour.class, 2, 3, 4);
		TwoToAny<Integer> t2a = API.collectIn(TwoToAny.class, 2, 3, 4);

		Any<Integer> a = t2f.unbound();
		OrderedAny<Integer> oa = ot2f.unbound();
		UniqueAny<Integer> ua = ut2f.unbound();
		OrderedUniqueAny<Integer> oua = out2f.unbound();
		Any<Integer> a2 = t2a.unbound();

		assertThatContains(a, 2, 3, 4);
		assertThatContains(oa, 2, 3, 4);
		assertThatContains(ua, 2, 3, 4);
		assertThatContains(oua, 2, 3, 4);
		assertThatContains(a2, 2, 3, 4);
	}

	@Test
	public void testAs() {
		OrderedUniqueAny<Integer> a = API.collectIn(OrderedUniqueAny.class, 1, 2, 3, 4);
		OrderedTwoToFour<Integer> t2a = a.as(OrderedTwoToFour.class);

		assertThatContains(t2a, 1, 2, 3, 4);

		OneToAny<Integer> o2a = t2a.as(OneToAny.class);

		assertThatContains(o2a, 1, 2, 3, 4);
	}

	@Test(expected = LowerBoundError.class)
	public void testLowerBoundError() {
		API.collectIn(TwoToFour.class, 2);
	}

	@Test(expected = UpperBoundError.class)
	public void testUpperBoundError() {
		API.collectIn(TwoToFour.class, 1, 2, 3, 4, 5);
	}

	@Test(expected = LowerBoundError.class)
	public void testLowerBoundErrorWithRemove() {
		TwoToFour<Integer> t2f = API.collectIn(TwoToFour.class, 2, 3);

		t2f.remove(3);
	}

	@Test(expected = UpperBoundError.class)
	public void testUpperBoundErrorWithAdd() {
		TwoToFour<Integer> t2f = API.collectIn(TwoToFour.class, 1, 2, 3, 4);

		t2f.add(5);
	}

	@Test(expected = LowerBoundError.class)
	public void testLowerBoundErrorWithAs() {
		Any<Integer> a = API.collect(2);

		a.as(TwoToFour.class);
	}

	@Test(expected = UpperBoundError.class)
	public void testUpperBoundErrorWithAs() {
		Any<Integer> a = API.collect(1, 2, 3, 4, 5);

		a.as(TwoToFour.class);
	}

	@Test
	public void testUnique() {
		UniqueAny<String> s = API.collectIn(UniqueAny.class, "A", "B", "C", "C", "B");
		s = s.add("A").add("B").add("A").add("D");

		assertThatContains(s, "A", "B", "C", "D");

		UniqueTwoToFour<String> s2 = s.as(UniqueTwoToFour.class);

		assertThatContains(s2, "A", "B", "C", "D");
	}

	@Test
	public void testOrdered() {
		OrderedAny<String> s = API.collectIn(OrderedAny.class, "A", "B", "C", "C", "B");
		s = s.add("A").add("B").add("A").add("D");

		assertThatContains(s, "A", "A", "A", "B", "B", "B", "C", "C", "D");

		List<String> tmp = new ArrayList<>();
		s.forEach(tmp::add);

		Assert.assertEquals(Arrays.asList("A", "B", "C", "C", "B", "A", "B", "A", "D"), tmp);

		s = API.collectIn(OrderedAny.class, "A", "B", "C");

		OrderedTwoToFour<String> s2 = s.as(OrderedTwoToFour.class);

		assertThatContains(s2, "A", "B", "C");
	}

	@Test
	public void testOrderedUnique() {
		OrderedUniqueAny<String> s = API.collectIn(OrderedUniqueAny.class, "A", "B", "C", "C", "B");
		s = s.add("A").add("B").add("A").add("D");

		assertThatContains(s, "A", "B", "C", "D");

		OrderedUniqueTwoToFour<String> s2 = s.as(OrderedUniqueTwoToFour.class);

		assertThatContains(s2, "A", "B", "C", "D");
	}

	@Test
	public void testGet() {
		OrderedAny<Integer> a = API.collectIn(OrderedAny.class, 1, -5, -3, 2);

		Assert.assertEquals(1, (int) a.get(0));
		Assert.assertEquals(-5, (int) a.get(1));
		Assert.assertEquals(-3, (int) a.get(2));
		Assert.assertEquals(2, (int) a.get(3));
	}

	@Test
	public void testIndexedAdd() {
		OrderedAny<Integer> a = API.collectIn(OrderedAny.class, 1, -5, -3, 2);
		a = a.add(2, 0);

		Assert.assertEquals(1, (int) a.get(0));
		Assert.assertEquals(-5, (int) a.get(1));
		Assert.assertEquals(0, (int) a.get(2));
		Assert.assertEquals(-3, (int) a.get(3));
		Assert.assertEquals(2, (int) a.get(4));
	}

	@Test
	public void testIndexedRemove() {
		OrderedAny<Integer> a = API.collectIn(OrderedAny.class, 1, -5, -3, 2);
		a = a.remove(2);

		Assert.assertEquals(1, (int) a.get(0));
		Assert.assertEquals(-5, (int) a.get(1));
		Assert.assertEquals(2, (int) a.get(2));
	}

	@Test
	public void testToString() {
		Any<Integer> a = API.collect(1, 2, 3);
		OrderedAny<Integer> oa = API.collectIn(OrderedAny.class, 1, 2, 3);
		UniqueAny<Integer> ua = API.collectIn(UniqueAny.class, 1, 2, 3);
		OrderedUniqueAny<Integer> oua = API.collectIn(OrderedUniqueAny.class, 1, 2, 3);
		One<Integer> o = API.collectIn(One.class, 0);

		Assert.assertEquals("[1, 2, 3]", a.toString());
		Assert.assertEquals("(1, 2, 3)", oa.toString());
		Assert.assertEquals("{1, 2, 3}", ua.toString());
		Assert.assertEquals("<1, 2, 3>", oua.toString());
		Assert.assertEquals("[0]", o.toString());
	}

	// custom collections

	@Min(2)
	@Max(4)
	private class TwoToFour<T> extends Collection<T, TwoToFour<T>> {
	}

	@Min(2)
	@Max(4)
	private class OrderedTwoToFour<T> extends OrderedCollection<T, OrderedTwoToFour<T>> {
	}

	@Min(2)
	@Max(4)
	private class UniqueTwoToFour<T> extends UniqueCollection<T, UniqueTwoToFour<T>> {
	}

	@Min(2)
	@Max(4)
	private class OrderedUniqueTwoToFour<T> extends OrderedUniqueCollection<T, OrderedUniqueTwoToFour<T>> {
	}

	@Min(2)
	private class TwoToAny<T> extends Collection<T, TwoToAny<T>> {
	}

	@SafeVarargs
	private final <T> void assertThatContains(GeneralCollection<T> actual, T... expecteds) {
		Multiset<T> actualSet = ImmutableMultiset.copyOf(actual);
		Multiset<T> expectedSet = ImmutableMultiset.copyOf(expecteds);
		Assert.assertEquals(expectedSet, actualSet);
	}

}
