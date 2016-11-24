package hu.elte.txtuml.xtxtuml.tests.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLAssociationValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;

	@Test
	def checkAssociationHasExactlyTwoEnds() {
		'''
			class A;
			association AA {
				A a1;
				A a2;
			}
		'''.parse.assertNoError(ASSOCIATION_END_COUNT_MISMATCH);

		val file = '''
			class A;
			association A1 {}
			association A2 {
				A a1;
			}
			association A3 {
				A a1;
				A a2;
				A a3;
			}
		'''.parse;

		file.assertError(TU_ASSOCIATION, ASSOCIATION_END_COUNT_MISMATCH, 22, 2);
		file.assertError(TU_ASSOCIATION, ASSOCIATION_END_COUNT_MISMATCH, 41, 2);
		file.assertError(TU_ASSOCIATION, ASSOCIATION_END_COUNT_MISMATCH, 70, 2);
	}

	@Test
	def checkContainerEndIsAllowedAndNeededOnlyInComposition() {
		val validFile = '''
			class A;
			composition C {
				container A a1;
				A a2;
			}
		'''.parse;

		validFile.assertNoError(CONTAINER_END_COUNT_MISMATCH);
		validFile.assertNoError(CONTAINER_END_IN_ASSOCIATION);

		val invalidFile = '''
			class A;
			association AA {
				container A a1;
				A a2;
			}
			composition C1 {
				A a1;
				A a2;
			}
			composition C2 {
				container A a1;
				container A a2;
			}
		'''.parse;

		invalidFile.assertError(TU_ASSOCIATION_END, CONTAINER_END_IN_ASSOCIATION, 29, 9);
		invalidFile.assertError(TU_COMPOSITION, CONTAINER_END_COUNT_MISMATCH, 69, 2);
		invalidFile.assertError(TU_COMPOSITION, CONTAINER_END_COUNT_MISMATCH, 106, 2);
	}

	@Test
	def checkContainerEndMultiplicity() {
		'''
			class A;
			composition C {
				container A a;
			}
		'''.parse.assertNoWarnings(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY);

		val file = '''
			class A;
			composition C {
				0..1 container A a1;
				* container A a2;
			}
		'''.parse;

		file.assertWarning(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY, 28, 4);
		file.assertWarning(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY, 51, 1);
	}

	@Test
	def checkMultiplicity() {
		'''
			class A;
			association AA {
				A a1;
				* A a2;
				1 A a3;
				2 A a4;
				0..* A a5;
				1..* A a6;
				1..1 A a7;
				1..2 A a8;
			}
		'''.parse.assertNoWarnings(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY);

		val file = '''
			class A;
			association AA {
				0 A a1;
				0..0 A a2;
				1..0 A a3;
			}
		'''.parse;

		file.assertWarning(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY, 29, 1);
		file.assertWarning(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY, 39, 4);
		file.assertWarning(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY, 52, 4);
	}

}
