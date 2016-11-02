package hu.elte.txtuml.xtxtuml.tests.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLAssociationValidatorTest {

	@Inject extension ParseHelper<TUFile>
	@Inject extension ValidationTestHelper

	@Test
	def void testAssociationWithTwoEnds() {
		'''
			package model.test;
			
			class A {}
			class B {}
			association A_B {
				1 A a;
				1 B b;
			}
		'''.parse.assertNoErrors
	}

	@Test
	def void testAssociationWithOneEnd() {
		'''
			package model.test;
			
			class A {}
			class B {}
			association A_B {
				1 A a;
			}
		'''.parse.assertError(XtxtUMLPackage.eINSTANCE.TUAssociation, ASSOCIATION_END_COUNT_MISMATCH)
	}

	@Test
	def void testAssociationWithThreeEnds() {
		'''
			package model.test;
			
			class A {}
			class B {}
			association A_B {
				1 A a;
				1 B b;
				1 B b2;
			}
		'''.parse.assertError(XtxtUMLPackage.eINSTANCE.TUAssociation, ASSOCIATION_END_COUNT_MISMATCH)
	}

	@Test
	def void testAssociationWithNonUniqueEndNames() {
		'''
			package model.test;
			
			class A {}
			class B {}
			association A_B {
				1 A a;
				1 B a;
			}
		'''.parse.assertError(XtxtUMLPackage.eINSTANCE.TUAssociationEnd, NOT_UNIQUE_NAME)
	}

	@Test
	def void testAssociationWithContainerEnd() {
		'''
			package model.test;
			
			class A {}
			class B {}
			association A_B {
				container A a;
				1 B b;
			}
		'''.parse.assertError(XtxtUMLPackage.eINSTANCE.TUAssociationEnd, CONTAINER_END_IN_ASSOCIATION)
	}

	@Test
	def void testCompositionWithoutContainerEnd() {
		'''
			package model.test;
			
			class A {}
			class B {}
			composition A_B {
				1 A a;
				1 B b;
			}
		'''.parse.assertError(XtxtUMLPackage.eINSTANCE.TUComposition, CONTAINER_END_COUNT_MISMATCH)
	}

	@Test
	def void testCompositionWithTwoContainerEnds() {
		'''
			package model.test;
			
			class A {}
			class B {}
			composition A_B {
				container A a;
				container B b;
			}
		'''.parse.assertError(XtxtUMLPackage.eINSTANCE.TUComposition, CONTAINER_END_COUNT_MISMATCH)
	}

	@Test
	def void testCompositionWithOneContainerEnd() {
		'''
			package model.test;
			
			class A {}
			class B {}
			composition A_B {
				container A a;
				1 B b;
			}
		'''.parse.assertNoErrors
	}
}
