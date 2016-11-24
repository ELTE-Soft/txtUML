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
import static org.eclipse.xtext.xbase.XbasePackage.Literals.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLExpressionValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;

	@Test
	def checkMandatoryIntentionalReturn() {
		'''
			class A {
				void foo() { return; }
				int bar() { return 0; }
				int baz() {
					if (true) {
						return 0;
					} else {
						return 0;
					}
				}
				int foobar() {
					for (int i = 0; i < 1; i++) {
						return 0;
					}
				}
				int foobaz() {
					while (true) {
						return 0;
					}
				}
				int barbaz() {
					do {
						return 0;
					} while (true);
				}
				int foobarbaz(int i) {
					switch (i) {
						case 0 : { return 0; }
						default : { return 0; }
					}
				}
			}
		'''.parse.assertNoError(MISSING_RETURN);

		val file = '''
			class A {
				int foo() {}
				int bar() {
					for (int i = 0; i < 1; i++) {}
				}
				int baz() {
					while (true) {}
				}
				int foobar() {
					do {} while (true);
				}
				int barbaz(int i) {
					switch (i) {}
				}
				int foobaz() {
					if (false) { return 0; }
				}
			}
		'''.parse;

		file.assertError(TU_OPERATION, MISSING_RETURN, 16, 3);
		file.assertError(TU_OPERATION, MISSING_RETURN, 31, 3);
		file.assertError(TU_OPERATION, MISSING_RETURN, 83, 3);
		file.assertError(TU_OPERATION, MISSING_RETURN, 120, 6);
		file.assertError(TU_OPERATION, MISSING_RETURN, 164, 6);
		file.assertError(TU_OPERATION, MISSING_RETURN, 207, 6);
	}

	@Test
	def checkNoExplicitExtensionCall() {
		'''
			class A {
				void foo(A a) {
					foo(a);
				}
			}
		'''.parse.assertNoError(UNDEFINED_OPERATION);

		val file = '''
			class A {
				void foo(A a) {
					foo();
					this.foo();
				}
			}
		'''.parse;

		file.assertError(XFEATURE_CALL, UNDEFINED_OPERATION, 31, 3)
		file.assertError(XMEMBER_FEATURE_CALL, UNDEFINED_OPERATION, 46, 3);
	}

	@Test
	def checkXtxtUMLExplicitOperationCall() {
		'''
			class A {
				void foo() {
					foo();
					this.foo();
				}
			}
		'''.parse.assertNoError(MISSING_OPERATION_PARENTHESES);

		val file = '''
			class A {
				void foo() {
					foo;
					this.foo;
				}
			}
		'''.parse;

		file.assertError(XFEATURE_CALL, MISSING_OPERATION_PARENTHESES, 28, 3);
		file.assertError(XMEMBER_FEATURE_CALL, MISSING_OPERATION_PARENTHESES, 41, 3);
	}

	@Test
	def checkSignalAccessExpression() {
		'''
			class A {
				state S {
					entry {
						trigger;
					}
					exit {
						trigger;
					}
				}
			}
		'''.parse.assertNoError(INVALID_SIGNAL_ACCESS);

		'''
			class A {
				transition T {
					effect {
						trigger;
					}
				}
			}
		'''.parse.assertNoError(INVALID_SIGNAL_ACCESS);

		'''
			class A {
				initial Init;
				state S {
					exit {
						trigger;
					}
				}
				transition T {
					from Init;
					to S;
				}
			}
		'''.parse.assertNoError(INVALID_SIGNAL_ACCESS);

		'''
			class A {
				initial Init;
				state S;
				choice C;
				transition T1 {
					from Init;
					to S;
				}
				transition T2 {
					from S;
					to C;
					effect {
						trigger;
					}
				}
				transition T3 {
					from C;
					to S;
					effect {
						trigger;
					}
				}
			}
		'''.parse.assertNoError(INVALID_SIGNAL_ACCESS);

		val trivialInvalid = '''
			execution E {
				trigger;
			}
			class A {
				void foo() {
					trigger;
				}
			}
		'''.parse;

		trivialInvalid.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS, 16, 7);
		trivialInvalid.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS, 57, 7);

		val nonTrivialInvalid = '''
			class A {
				initial Init;
				choice C1;
				choice C2;
				choice C3;
				choice C4;
				state S1 {
					entry { trigger; }
				}
				state S2;
				transition T1 {
					from Init;
					to C1;
					effect { trigger; }
				}
				transition T2 {
					from C1;
					to C2;
					effect { trigger; }
				}
				transition T3 {
					from C2;
					to C3;
				}
				transition T4 {
					from C3;
					to C1;
				}
				transition T5 {
					from C3;
					to S1;
					effect { trigger; }
				}
				transition T6 {
					from S2;
					to S1;
				}
			}
		'''.parse;

		nonTrivialInvalid.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS, 102, 7);
		nonTrivialInvalid.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS, 183, 7);
		nonTrivialInvalid.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS, 250, 7);
		nonTrivialInvalid.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS, 405, 7);
	}

	@Test
	def checkSignalSentToPortIsRequired() {
		'''
			signal S1;
			signal S2;
			interface I {
				reception S1;
				reception S2;
			}
			class A {
				port P { required I; }
				void foo() {
					send new S1() to this->(P);
					send new S2() to this->(P);
				}
			}
		'''.parse.assertNoError(NOT_REQUIRED_SIGNAL);

		val file = '''
			signal S1;
			signal S2;
			interface I {
				reception S1;
			}
			class A {
				port P1 { required I; }
				port P2 {}
				void foo() {
					send new S1() to this->(P1);
					send new S2() to this->(P1);
					send new S1() to this->(P2);
					send new S2() to this->(P2);
				}
			}
		'''.parse;

		file.assertError(TU_SEND_SIGNAL_EXPRESSION, NOT_REQUIRED_SIGNAL, 162, 8);
		file.assertError(TU_SEND_SIGNAL_EXPRESSION, NOT_REQUIRED_SIGNAL, 194, 8);
		file.assertError(TU_SEND_SIGNAL_EXPRESSION, NOT_REQUIRED_SIGNAL, 226, 8);
	}

	@Test
	def checkQueriedPortIsOwned() {
		'''
			class A {
				port P {}
				void foo() {
					send null to this->(P);
				}
			}
			class B {
				port P {}
				void foo() {
					send null to this->(P);
				}
			}
			class C {
				port P {}
			}
			class D extends C {
				void foo() {
					send null to this->(C.P);
				}
			}
			class E extends D {
				void foo() {
					send null to this->(C.P);
				}
			}
		'''.parse.assertNoError(QUERIED_PORT_IS_NOT_OWNED);

		val file = '''
			class A {
				port P {}
				void foo() {
					send null to this->(B.P);
				}
			}
			class B {
				port P {}
				void foo() {
					send null to this->(A.P);
				}
			}
			class C {
				void bar() {
					send null to this->(D.P);
				}
			}
			class D extends C {
				port P {}
			}
			class E extends C {
				void foo() {
					send null to this->(D.P);
				}
			}
		'''.parse;

		file.assertError(TU_SEND_SIGNAL_EXPRESSION, QUERIED_PORT_IS_NOT_OWNED, 53, 11);
		file.assertError(TU_SEND_SIGNAL_EXPRESSION, QUERIED_PORT_IS_NOT_OWNED, 127, 11);
		file.assertError(TU_SEND_SIGNAL_EXPRESSION, QUERIED_PORT_IS_NOT_OWNED, 189, 11);
		file.assertError(TU_SEND_SIGNAL_EXPRESSION, QUERIED_PORT_IS_NOT_OWNED, 297, 11);
	}

	@Test
	def checkAccessedClassPropertyIsSpecified() {
		'''
			class A {
				void foo() {
					this->(AB.b);
				}
			}
			class B;
			association AB {
				A a;
				B b;
			}
		'''.parse.assertNoError(MISSING_CLASS_PROPERTY);

		'''
			class A {
				void foo() {
					this->();
				}
			}
			class B;
			association AB {
				A a;
				B b;
			}
		'''.parse.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, MISSING_CLASS_PROPERTY, 32, 2);
	}

	@Test
	def checkOwnerOfAccessedClassProperty() {
		val accessibleProps = '''
			class A {
				port P {}
				void foo() {
					this->(AB.b);
					this->(P);
				}
			}
			class AD extends A {
				void bar() {
					this->(AB.b);
					this->(A.P);
				}
			}
			class ADD extends AD {
				void baz() {
					this->(AB.b);
					this->(A.P);
				}
			}
			class B {
				port P {}
				void bar() {
					this->(AB.a);
					this->(P);
				}
			}
			association AB {
				A a;
				B b;
			}
		'''.parse;

		accessibleProps.assertNoError(NOT_NAVIGABLE_ASSOCIATION_END);
		accessibleProps.assertNoError(NOT_ACCESSIBLE_ASSOCIATION_END);
		accessibleProps.assertNoError(NOT_ACCESSIBLE_PORT);

		val notAccessibleProps = '''
			class A {
				port P {}
				void foo() {
					this->(AB1.a);
					this->(AB2.b);
					this->(AD1B.b);
					this->(AD1.ADP);
				}
			}
			class AD1 extends A {
				port ADP {}
			}
			class AD2 extends A {
				void bar() {
					this->(AD1.ADP);
				}
			}
			class B {
				port P {}
				void bar() {
					this->(AB1.b);
					this->(A.P);
				}
			}
			association AB1 {
				A a;
				B b;
			}
			association AB2 {
				A a;
				hidden B b;
			}
			association AD1B {
				AD1 ad1;
				B b;
			}
		'''.parse;

		notAccessibleProps.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_ASSOCIATION_END, 47, 5);
		notAccessibleProps.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_NAVIGABLE_ASSOCIATION_END, 65, 5);
		notAccessibleProps.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_ASSOCIATION_END, 83, 6);
		notAccessibleProps.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_PORT, 102, 7);

		notAccessibleProps.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_PORT, 207, 7);
		notAccessibleProps.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_ASSOCIATION_END, 272, 5);
		notAccessibleProps.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_PORT, 290, 3);
	}

}
