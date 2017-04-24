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
import static org.eclipse.xtext.common.types.TypesPackage.Literals.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLTypeValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;
	@Inject extension XtxtUMLValidationTestUtils;

	@Test
	def checkTypeReference() {
		'''
			enum E;
			signal Sig {
				int a1;
				boolean a2;
				double a3;
				String a4;
				E a5;
			}
			class Foo {
				int a1;
				boolean a2;
				double a3;
				String a4;
				E a5;
				void o1(int p) {}
				int o2(boolean p) {}
				boolean o3(double p) {}
				double o4(String p) {}
				Foo o5(Foo p) {}
				E o6(E e) {}
				Sig o7(Sig s) {}
			}
		'''.parse.assertNoError(INVALID_TYPE);

		val rawFile = '''
			signal Sig {
				long a1;
				Foo a2;
				Class a3;
				void a4;
				Sig a5;
			}
			class Foo {
				long a1;
				Foo a2;
				Class a3;
				void a4;
				Sig a5;
				long o1() {}
				Class o2(Class p) {}
				void o3(void v) {}
			}
		''';

		val parsedFile = rawFile.parse;

		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("long", 0), 4);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Foo", 0), 3);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Class", 0), 5);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("void", 0), 4);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Sig", 1), 3);

		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("long", 1), 4);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Foo", 2), 3);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Class", 1), 5);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("void", 1), 4);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("long", 2), 4);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Class", 2), 5);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Class", 3), 5);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Sig", 2), 3);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("void", 3), 4);
	}

	@Test
	def checkSendSignalExpressionTypes() {
		'''
			signal Sig;
			class Foo {
				void foo() {
					send new Sig() to this;
				}
			}
		'''.parse.assertNoError(TYPE_MISMATCH);

		val nullsRaw = '''
			class Foo {
				void foo() {
					send null to null;
				}
			}
		''';

		val nullsParsed = nullsRaw.parse;
		nullsParsed.assertError(TU_SEND_SIGNAL_EXPRESSION, TYPE_MISMATCH, nullsRaw.indexOfNth("null", 0), 4);
		nullsParsed.assertError(TU_SEND_SIGNAL_EXPRESSION, TYPE_MISMATCH, nullsRaw.indexOfNth("null", 1), 4);

		val stringsRaw = '''
			class Foo {
				void foo() {
					send "signal" to "object";
				}
			}
		''';

		val stringsParsed = stringsRaw.parse;
		stringsParsed.assertError(TU_SEND_SIGNAL_EXPRESSION, TYPE_MISMATCH, stringsRaw.indexOf('"signal"'), 8);
		stringsParsed.assertError(TU_SEND_SIGNAL_EXPRESSION, TYPE_MISMATCH, stringsRaw.indexOf('"object"'), 8);
	}

	@Test
	def checkDeleteObjectExpressionTypes() {
		'''
			class Foo {
				void foo() {
					delete this;
				}
			}
		'''.parse.assertNoError(TYPE_MISMATCH);

		val nullRaw = '''
			class Foo {
				void foo() {
					delete null;
				}
			}
		''';

		nullRaw.parse.assertError(TU_DELETE_OBJECT_EXPRESSION, TYPE_MISMATCH, nullRaw.indexOf("null"), 4);

		val stringRaw = '''
			class Foo {
				void foo() {
					delete "object";
				}
			}
		''';

		stringRaw.parse.assertError(TU_DELETE_OBJECT_EXPRESSION, TYPE_MISMATCH, stringRaw.indexOf('"object"'), 8);
	}

	@Test
	def checkClassPropertyAccessExpressionTypes() {
		'''
			signal Sig;
			class Foo {
				port Po {}
				void foo() {
					send new Sig() to this->(Po);
				}
			}
		'''.parse.assertNoError(TYPE_MISMATCH);

		val nullRaw = '''
			signal Sig;
			class Foo {
				port Po {}
				void foo() {
					send new Sig() to null->(Po);
				}
			}
		''';

		nullRaw.parse.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH, nullRaw.indexOf("null"), 4);

		val stringRaw = '''
			signal Sig;
			class Foo {
				port Po {}
				void foo() {
					send new Sig() to "this"->(Po);
				}
			}
		''';

		stringRaw.parse.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH, stringRaw.indexOf('"this"'), 6);
	}

}
