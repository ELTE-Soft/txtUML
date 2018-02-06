package hu.elte.txtuml.api.model;

import org.junit.Assert;
import org.junit.Test;

public class DataTypeTests {

	@Test
	public void testNull() {
		Empty e1 = new Empty();
		Empty e2 = null;

		assertUnequals(e1, e2);
	}

	@Test
	public void testEqualsSelf() {
		Empty e1 = new Empty();
		Empty e2 = e1;

		assertEqualsAndHashCode(e1, e2);
	}

	@Test
	public void testTwoEmptyDataTypesAreEqual() {
		Empty e1 = new Empty();
		Empty e2 = new Empty();

		assertEqualsAndHashCode(e1, e2);
	}

	@Test
	public void testInstancesOfDifferentTypesAreUnequal() {
		Empty e1 = new Empty();
		Empty2 e2 = new Empty2();

		assertUnequals(e1, e2);
	}

	@Test
	public void testEqualityWithFields() {
		IntString iS1 = new IntString(1024, "sth");
		IntString iS2 = new IntString(1024, "sth");

		assertEqualsAndHashCode(iS1, iS2);
	}

	@Test
	public void testUnequalityWithFields() {
		IntString iS1 = new IntString(1024, "sth");
		IntString iS2 = new IntString(-1024, "sth");

		assertUnequals(iS1, iS2);
	}

	@Test
	public void testEqualityOfWrappers() {
		Wrapper w1 = new Wrapper(new IntString(2048, "string"));
		Wrapper w2 = new Wrapper(new IntString(2048, "string"));

		assertEqualsAndHashCode(w1, w2);
	}

	@Test
	public void testUnequalityOfWrappers() {
		Wrapper w1 = new Wrapper(new IntString(1426, "string1"));
		Wrapper w2 = new Wrapper(new IntString(1426, "string2"));

		assertUnequals(w1, w2);
	}

	@Test
	public void testTwoWrappersBothContainingNull() {
		Wrapper w1 = new Wrapper(null);
		Wrapper w2 = new Wrapper(null);

		assertEqualsAndHashCode(w1, w2);
	}

	@Test
	public void testTwoWrappersOnlyOneContainingNull() {
		Wrapper w1 = new Wrapper(new IntString(1426, "string1"));
		Wrapper w2 = new Wrapper(null);

		assertUnequals(w1, w2);
	}

	@Test
	public void testEqualityOfSubs() {
		Sub s1 = new Sub(142, "152", (byte) 7);
		Sub s2 = new Sub(142, "152", (byte) 7);

		assertEqualsAndHashCode(s1, s2);
	}

	@Test
	public void testSubsDifferInOwnField() {
		Sub s1 = new Sub(142, "152", 8L);
		Sub s2 = new Sub(142, "152", 7L);

		assertUnequals(s1, s2);
	}

	@Test
	public void testSubsDifferInInheritedField() {
		Sub s1 = new Sub(142, "152", 8L);
		Sub s2 = new Sub(142, "153", 7L);

		assertUnequals(s1, s2);
	}

	@Test
	public void testEqualityOfBigs() {
		IntString wrapped = new IntString(98, "wasd");

		Sub s1 = new Sub(-10, "c", 0L);
		Sub s2 = new Sub(-10, "c", 0L);

		IntString iS1 = new IntString(42, "42");
		IntString iS2 = new IntString(42, "42");

		Wrapper w1 = new Wrapper(wrapped);
		Wrapper w2 = new Wrapper(wrapped);

		Big b1 = new Big(56, "X", 1000L, s1, "name", -1, 127L, iS1, w1);
		Big b2 = new Big(56, "X", 1000L, s2, "name", -1, 127L, iS2, w2);

		assertEqualsAndHashCode(b1, b2);
	}

	@Test
	public void testUnequalityOfBigs() {
		IntString wrapped = new IntString(98, "wasd");

		Sub s1 = new Sub(-10, "c", 0L);
		Sub s2 = new Sub(-10, "c", 0L);

		IntString iS1 = new IntString(42, "42");
		IntString iS2 = new IntString(42, "42");

		Wrapper w1 = new Wrapper(wrapped);
		Wrapper w2 = new Wrapper(null); // here differs

		Big b1 = new Big(56, "X", 1000L, s1, "name", -1, 127L, iS1, w1);
		Big b2 = new Big(56, "X", 1000L, s2, "name", -1, 127L, iS2, w2);

		assertUnequals(b1, b2);
	}

	class Empty extends DataType {
	}

	class Empty2 extends DataType {
	}

	class IntString extends DataType {
		final int i;
		final String s;

		IntString(int i, String s) {
			this.i = i;
			this.s = s;
		}
	}

	class Wrapper extends DataType {
		final IntString iS;

		Wrapper(IntString iS) {
			this.iS = iS;
		}
	}

	class Sub extends IntString {
		final long b;

		public Sub(int i, String s, long b) {
			super(i, s);
			this.b = b;
		}
	}

	class Big extends Sub {
		final Sub sub;
		final String name;
		final int x;
		final long w;
		final IntString iS;
		final Wrapper wp;

		Big(int i, String s, long b, Sub sub, String name, int x, long w, IntString iS, Wrapper wp) {
			super(i, s, b);
			this.sub = sub;
			this.name = name;
			this.x = x;
			this.w = w;
			this.iS = iS;
			this.wp = wp;
		}
	}

	private static void assertEqualsAndHashCode(Object o1, Object o2) {
		Assert.assertTrue(o1.equals(o2));
		Assert.assertTrue(o1.hashCode() == o2.hashCode());
	}

	private static void assertUnequals(Object o1, Object o2) {
		Assert.assertFalse(o1.equals(o2));
	}

}
