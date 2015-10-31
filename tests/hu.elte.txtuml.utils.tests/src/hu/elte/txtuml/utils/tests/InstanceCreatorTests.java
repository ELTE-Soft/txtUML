package hu.elte.txtuml.utils.tests;

import hu.elte.txtuml.utils.InstanceCreator;

import org.junit.Assert;
import org.junit.Test;

public class InstanceCreatorTests extends Assert {

	@Test
	public void testInstantaitionWithDefaultConstructor() {
		TestClassWithNoConstructorsDefined inst = InstanceCreator
				.create(TestClassWithNoConstructorsDefined.class);

		assertTrue(inst != null);
	}

	public static class TestClassWithNoConstructorsDefined {
	}

	@Test
	public void testInstantiationWithExplicitConstructor() {
		String name = "new object";
		TestClassWithStringParamConstructor inst = InstanceCreator.create(
				TestClassWithStringParamConstructor.class, name);

		assertTrue(inst != null);
		assertTrue(inst.name == name);
		// In this case, the two String objects have to be the same, not only
		// equal.
	}

	public static class TestClassWithStringParamConstructor {
		String name;

		public TestClassWithStringParamConstructor(String name) {
			this.name = name;
		}
	}

	@Test
	public void testInstantiationWithPrimitiveParams() {

		TestClassWithIntParamConstructor inst = InstanceCreator.create(
				TestClassWithIntParamConstructor.class, 10);

		assertTrue(inst != null);
		assertTrue(inst.field == 10);
	}

	public static class TestClassWithIntParamConstructor {
		int field;

		public TestClassWithIntParamConstructor(int param) {
			this.field = param;
		}
	}

	/**
	 * InstanceCreator should also create instances of classes that are not
	 * visible for the InstanceCreator class.
	 */
	@Test
	public void testInstantaitionOfPackagePrivateClass() {
		PackagePrivateTestClass inst = InstanceCreator
				.create(PackagePrivateTestClass.class);

		assertTrue(inst != null);
	}

	static class PackagePrivateTestClass {
	}

	/**
	 * InstanceCreator should also use constructors that are not visible for the
	 * InstanceCreator class.
	 */
	@Test
	public void testInstantaitionWithPackagePrivateConstructor() {
		TestClassWithPackagePrivateConstructor inst = InstanceCreator.create(
				TestClassWithPackagePrivateConstructor.class, true);

		assertTrue(inst != null);
		assertTrue(inst.val);
	}

	public static class TestClassWithPackagePrivateConstructor {
		boolean val;

		TestClassWithPackagePrivateConstructor(Boolean val) {
			this.val = val;
		}
	}

	@Test
	public void testInstantiationOfClassWithMultipleConstructors() {
		TestClassWithMultipleConstructors inst = InstanceCreator.create(
				TestClassWithMultipleConstructors.class, true);

		assertTrue(inst != null);
		assertTrue(inst.val);
	}

	public static class TestClassWithMultipleConstructors {
		boolean val;

		public TestClassWithMultipleConstructors() {
			// unused
		}

		public TestClassWithMultipleConstructors(int i) {
			// unused
		}

		public TestClassWithMultipleConstructors(boolean val) {
			this.val = val;
		}

		public TestClassWithMultipleConstructors(boolean val, int i) {
			// unused
		}

	}

	/**
	 * When instantiating an inner class, the actual enclosing class instance
	 * should be explicitly set.
	 */
	@Test
	public void testInstantiationOfInnerClass() {

		EnclosingTestClass enclosing = InstanceCreator
				.create(EnclosingTestClass.class);

		assertTrue(enclosing != null);

		EnclosingTestClass.InnerTestClass inst = InstanceCreator.create(
				EnclosingTestClass.InnerTestClass.class, enclosing);

		assertTrue(inst != null);
		assertTrue(inst.getEnclosingInstance() == enclosing);
	}

	public static class EnclosingTestClass {
		public class InnerTestClass {
			EnclosingTestClass getEnclosingInstance() {
				return EnclosingTestClass.this;
			}
		}
	}

	@Test
	public void testInstantiationWithTooManyGivenParameters() {

		TestClassWithNoConstructorsDefined inst = InstanceCreator.create(
				TestClassWithNoConstructorsDefined.class, 0);
		
		// creation should fail, inst should be null
		
		assertTrue(inst == null);
	}

	@Test
	public void testInstantiationWithNotEnoughGivenParameters() {

		TestClassWithIntParamConstructor inst = InstanceCreator.create(
				TestClassWithIntParamConstructor.class);
		
		// creation should fail, inst should be null
		
		assertTrue(inst == null);
	}
	
	@Test
	public void testInstantiationWithWrongGivenParameters() {

		TestClassWithIntParamConstructor inst = InstanceCreator.create(
				TestClassWithIntParamConstructor.class, true);
		
		// creation should fail, inst should be null
		
		assertTrue(inst == null);
	}

	
}
