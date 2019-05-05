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
			class Foo;
			association As {
				Foo e1;
				Foo e2;
			}
		'''.parse.assertNoError(ASSOCIATION_END_COUNT_MISMATCH);

		val rawFile = '''
			class Foo;
			association A1 {}
			association A2 {
				Foo e1;
			}
			association A3 {
				Foo e1;
				Foo e2;
				Foo e3;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_ASSOCIATION, ASSOCIATION_END_COUNT_MISMATCH, rawFile.indexOf("A1"), 2);
		parsedFile.assertError(TU_ASSOCIATION, ASSOCIATION_END_COUNT_MISMATCH, rawFile.indexOf("A2"), 2);
		parsedFile.assertError(TU_ASSOCIATION, ASSOCIATION_END_COUNT_MISMATCH, rawFile.indexOf("A3"), 2);
	}

	@Test
	def checkContainerEndIsAllowedAndNeededOnlyInComposition() {
		val validFile = '''
			class Foo;
			composition Co {
				container Foo e1;
				Foo e2;
			}
		'''.parse;

		validFile.assertNoError(CONTAINER_END_COUNT_MISMATCH);
		validFile.assertNoError(CONTAINER_END_IN_ASSOCIATION);

		val invalidFileRaw = '''
			class Foo;
			association As {
				container Foo e1;
				Foo e2;
			}
			composition C1 {
				Foo e1;
				Foo e2;
			}
			composition C2 {
				container Foo e1;
				container Foo e2;
			}
		''';

		val invalidFileParsed = invalidFileRaw.parse;
		invalidFileParsed.assertError(TU_ASSOCIATION_END, CONTAINER_END_IN_ASSOCIATION,
			invalidFileRaw.indexOf("container"), 9);
		invalidFileParsed.assertError(TU_COMPOSITION, CONTAINER_END_COUNT_MISMATCH, invalidFileRaw.indexOf("C1"), 2);
		invalidFileParsed.assertError(TU_COMPOSITION, CONTAINER_END_COUNT_MISMATCH, invalidFileRaw.indexOf("C2"), 2);
	}

	@Test
	def checkContainerEndMultiplicity() {
		'''
			class Foo;
			composition Co {
				container Foo foo;
			}
		'''.parse.assertNoWarnings(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY);

		val rawFile = '''
			class Foo;
			composition Co {
				0..1 container Foo e1;
				* container Foo e2;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertWarning(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY, rawFile.indexOf("0..1"), 4);
		parsedFile.assertWarning(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY, rawFile.indexOf("*"), 1);
	}

	@Test
	def checkMultiplicity() {
		'''
			class Foo;
			association As {
				Foo e1;
				* Foo e2;
				1 Foo e3;
				2 Foo e4;
				0..* Foo e5;
				1..* Foo e6;
				1..1 Foo e7;
				1..2 Foo e8;
			}
		'''.parse.assertNoWarnings(TU_ASSOCIATION_END, WRONG_ASSOCIATION_END_MULTIPLICITY);

		val rawFile = '''
			class Foo;
			association As {
				0 Foo e1;
				0..0 Foo e2;
				1..0 Foo e3;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertWarning(TU_MULTIPLICITY, WRONG_MULTIPLICITY, rawFile.indexOf("0"), 1);
		parsedFile.assertWarning(TU_MULTIPLICITY, WRONG_MULTIPLICITY, rawFile.indexOf("0..0"), 4);
		parsedFile.assertWarning(TU_MULTIPLICITY, WRONG_MULTIPLICITY, rawFile.indexOf("1..0"), 4);
	}

}
