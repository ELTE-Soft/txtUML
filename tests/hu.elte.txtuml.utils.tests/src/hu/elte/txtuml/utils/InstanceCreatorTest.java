package hu.elte.txtuml.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class InstanceCreatorTest {

	static class TestClass1 {
		public int f() {
			return 1;
		}
	}
	
	static class TestClass2 {
		private int i;

		public TestClass2(int i) {
			this.i = i;
		}
		
		public int f() {
			return i;
		}
	}
	
	static class Outer {
		class Inner {
			public int f() {
				return 2;
			}
		}
	}

	@Test
	public void testCreatePrimitive() throws Exception {
		int i = InstanceCreator.create(int.class);
		assertEquals(0, i);
	}

	@Test
	public void testCreateSimpleClass() throws Exception {
		TestClass1 tc = InstanceCreator.create(TestClass1.class);
		assertEquals(1, tc.f());
	}

	@Test
	public void testCreateUsingConstructor() throws Exception {
		TestClass2 tc = InstanceCreator.create(TestClass2.class, 3);
		assertEquals(3, tc.f());
	}
	
	@Test
	public void testCreateNestedClass() throws Exception {
		Outer.Inner tc = InstanceCreator.create(Outer.Inner.class);
		assertEquals(2, tc.f());
	}

}
