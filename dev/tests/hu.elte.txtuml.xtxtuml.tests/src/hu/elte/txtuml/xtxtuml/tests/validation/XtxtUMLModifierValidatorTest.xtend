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
class XtxtUMLModifierValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;
	@Inject extension XtxtUMLValidationTestUtils;

	@Test
	def checkStaticIsNotUsedOnConstructor() {
		'''
			class Foo {
				Foo() {}
			}
		'''.parse.assertNoError(CONSTRUCTOR_IS_STATIC);

		val rawFile = '''
			class Foo {
				static Foo() {}
				static external Foo(int p) {}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_MODIFIERS, CONSTRUCTOR_IS_STATIC, rawFile.indexOfNth("static", 0), 6);
		parsedFile.assertError(TU_MODIFIERS, CONSTRUCTOR_IS_STATIC, rawFile.indexOfNth("static", 1), 6);
	}

	@Test
	def checkExternalBodyIsNotUsedOnAttribute() {
		'''
			class Foo {
				int bar;
				external int baz;
			}
		'''.parse.assertNoError(ATTRIBUTE_HAS_EXTERNAL_BODY);

		val rawFile = '''
			class Foo {
				external-body int baz;
			}
		''';

		rawFile.parse.assertError(TU_MODIFIERS, ATTRIBUTE_HAS_EXTERNAL_BODY, rawFile.indexOf("external-body"), 13);
	}

	@Test
	def checkStaticIsNotUsedOnAttribute() {
		'''
			class Foo {
				int bar;
				external static int baz;
			}
		'''.parse.assertNoError(ATTRIBUTE_IS_STATIC);

		val rawFile = '''
			class Foo {
				static int bar;
			}
		''';

		rawFile.parse.assertError(TU_MODIFIERS, ATTRIBUTE_IS_STATIC, rawFile.indexOf("static"), 6);
	}

}
