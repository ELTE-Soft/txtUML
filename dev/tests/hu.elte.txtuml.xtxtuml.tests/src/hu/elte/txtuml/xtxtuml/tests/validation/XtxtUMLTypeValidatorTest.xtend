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

	@Test
	def checkTypeReference() {
		'''
			signal S {
				int a1;
				boolean a2;
				double a3;
				String a4;
			}
			class A {
				int a1;
				boolean a2;
				double a3;
				String a4;
				void o1(int p) {}
				int o2(boolean p) {}
				boolean o3(double p) {}
				double o4(String p) {}
				A o5() {}
			}
		'''.parse.assertNoError(INVALID_TYPE);

		val file = '''
			signal S {
				long a1;
				A a2;
				Class a3;
				void a4;
			}
			class A {
				long a1;
				A a2;
				Class a3;
				void a4;
				long o1() {}
				Class o2(Class p) {}
			}
		'''.parse;

		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 13, 4);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 24, 1);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 32, 5);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 44, 4);

		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 69, 4);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 80, 1);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 88, 5);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 100, 4);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 111, 4);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 126, 5);
		file.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, 135, 5);
	}

	@Test
	def checkSendSignalExpressionTypes() {
		'''
			signal S;
			class A {
				void foo() {
					send new S() to this;
				}
			}
		'''.parse.assertNoError(TYPE_MISMATCH);

		val nulls = '''
			class A {
				void foo() {
					send null to null;
				}
			}
		'''.parse;

		nulls.assertError(TU_SEND_SIGNAL_EXPRESSION, TYPE_MISMATCH, 33, 4);
		nulls.assertError(TU_SEND_SIGNAL_EXPRESSION, TYPE_MISMATCH, 41, 4);

		val strings = '''
			class A {
				void foo() {
					send "signal" to "object";
				}
			}
		'''.parse;

		strings.assertError(TU_SEND_SIGNAL_EXPRESSION, TYPE_MISMATCH, 33, 8);
		strings.assertError(TU_SEND_SIGNAL_EXPRESSION, TYPE_MISMATCH, 45, 8);
	}

	@Test
	def checkDeleteObjectExpressionTypes() {
		'''
			class A {
				void foo() {
					delete this;
				}
			}
		'''.parse.assertNoError(TYPE_MISMATCH);

		'''
			class A {
				void foo() {
					delete null;
				}
			}
		'''.parse.assertError(TU_DELETE_OBJECT_EXPRESSION, TYPE_MISMATCH, 35, 4);

		'''
			class A {
				void foo() {
					delete "object";
				}
			}
		'''.parse.assertError(TU_DELETE_OBJECT_EXPRESSION, TYPE_MISMATCH, 35, 8);
	}

	@Test
	def checkClassPropertyAccessExpressionTypes() {
		'''
			signal S;
			class A {
				port P {}
				void foo() {
					send new S() to this->(P);
				}
			}
		'''.parse.assertNoError(TYPE_MISMATCH);

		'''
			signal S;
			class A {
				port P {}
				void foo() {
					send new S() to null->(P);
				}
			}
		'''.parse.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH, 67, 4);

		'''
			signal S;
			class A {
				port P {}
				void foo() {
					send new S() to "this"->(P);
				}
			}
		'''.parse.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH, 67, 6);
	}

}
