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
	@Inject extension XtxtUMLValidationTestUtils;

	@Test
	def checkMandatoryIntentionalReturn() {
		'''
			class Foo {
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

		val rawFile = '''
			class Foo {
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
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_OPERATION, MISSING_RETURN, rawFile.indexOf("foo"), 3);
		parsedFile.assertError(TU_OPERATION, MISSING_RETURN, rawFile.indexOf("bar"), 3);
		parsedFile.assertError(TU_OPERATION, MISSING_RETURN, rawFile.indexOf("baz"), 3);
		parsedFile.assertError(TU_OPERATION, MISSING_RETURN, rawFile.indexOf("foobar"), 6);
		parsedFile.assertError(TU_OPERATION, MISSING_RETURN, rawFile.indexOf("barbaz"), 6);
		parsedFile.assertError(TU_OPERATION, MISSING_RETURN, rawFile.indexOf("foobaz"), 6);
	}

	@Test
	def checkNoExplicitExtensionCall() {
		'''
			class Foo {
				void foo(Foo bar) {
					foo(bar);
				}
			}
		'''.parse.assertNoError(UNDEFINED_OPERATION);

		val rawFile = '''
			class Foo {
				void foo(Foo bar) {
					foo();
					this.foo();
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(XFEATURE_CALL, UNDEFINED_OPERATION, rawFile.indexOfNth("foo", 1), 3);
		parsedFile.assertError(XMEMBER_FEATURE_CALL, UNDEFINED_OPERATION, rawFile.indexOfNth("foo", 2), 3);
	}

	@Test
	def checkXtxtUMLExplicitOperationCall() {
		'''
			class Foo {
				void foo() {
					foo();
					this.foo();
				}
			}
		'''.parse.assertNoError(MISSING_OPERATION_PARENTHESES);

		val rawFile = '''
			class Foo {
				void foo() {
					foo;
					this.foo;
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(XFEATURE_CALL, MISSING_OPERATION_PARENTHESES, rawFile.indexOfNth("foo", 1), 3);
		parsedFile.assertError(XMEMBER_FEATURE_CALL, MISSING_OPERATION_PARENTHESES, rawFile.indexOfNth("foo", 2), 3);
	}

	@Test
	def checkSignalAccessExpression() {
		'''
			class Foo {
				state St {
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
			class Foo {
				transition Tr {
					effect {
						trigger;
					}
				}
			}
		'''.parse.assertNoError(INVALID_SIGNAL_ACCESS);

		'''
			class Foo {
				initial Init;
				state St {
					exit {
						trigger;
					}
				}
				transition Tr {
					from Init;
					to St;
				}
			}
		'''.parse.assertNoError(INVALID_SIGNAL_ACCESS);

		'''
			class Foo {
				initial Init;
				state St;
				choice Ch;
				transition T1 {
					from Init;
					to St;
				}
				transition T2 {
					from St;
					to Ch;
					effect {
						trigger;
					}
				}
				transition T3 {
					from Ch;
					to St;
					effect {
						trigger;
					}
				}
			}
		'''.parse.assertNoError(INVALID_SIGNAL_ACCESS);

		val trivialInvalidRaw = '''
			execution Ex {
				trigger;
			}
			class Foo {
				void op() {
					trigger;
				}
			}
		''';

		val trivialInvalidParsed = trivialInvalidRaw.parse;
		trivialInvalidParsed.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS,
			trivialInvalidRaw.indexOfNth("trigger", 0), 7);
		trivialInvalidParsed.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS,
			trivialInvalidRaw.indexOfNth("trigger", 1), 7);

		val nonTrivialInvalidRaw = '''
			class Foo {
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
		''';

		val nonTrivialInvalidParsed = nonTrivialInvalidRaw.parse;
		nonTrivialInvalidParsed.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS,
			nonTrivialInvalidRaw.indexOfNth("trigger", 0), 7);
		nonTrivialInvalidParsed.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS,
			nonTrivialInvalidRaw.indexOfNth("trigger", 1), 7);
		nonTrivialInvalidParsed.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS,
			nonTrivialInvalidRaw.indexOfNth("trigger", 2), 7);
		nonTrivialInvalidParsed.assertError(TU_SIGNAL_ACCESS_EXPRESSION, INVALID_SIGNAL_ACCESS,
			nonTrivialInvalidRaw.indexOfNth("trigger", 3), 7);
	}

	@Test
	def checkSignalSentToPortIsRequired() {
		'''
			signal S1;
			signal S2;
			signal S3 extends S2;
			interface If {
				reception S1;
				reception S2;
			}
			class Foo {
				port Po { required If; }
				void foo() {
					send new S1() to this->(Po);
					send new S2() to this->(Po);
					send new S3() to this->(Po);
				}
			}
		'''.parse.assertNoError(NOT_REQUIRED_SIGNAL);

		val rawFile = '''
			signal S0;
			signal S1 extends S0;
			signal S2;
			interface If {
				reception S1;
			}
			class Foo {
				port P1 { required If; }
				port P2 {}
				void foo() {
					send new S1() to this->(P1);
					send new S2() to this->(P1);
					send new S1() to this->(P2);
					send new S2() to this->(P2);
					send new S0() to this->(P1);
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_SEND_SIGNAL_EXPRESSION, NOT_REQUIRED_SIGNAL, rawFile.indexOfNth("new S2()", 0), 8);
		parsedFile.assertError(TU_SEND_SIGNAL_EXPRESSION, NOT_REQUIRED_SIGNAL, rawFile.indexOfNth("new S1()", 1), 8);
		parsedFile.assertError(TU_SEND_SIGNAL_EXPRESSION, NOT_REQUIRED_SIGNAL, rawFile.indexOfNth("new S2()", 1), 8);
		parsedFile.assertError(TU_SEND_SIGNAL_EXPRESSION, NOT_REQUIRED_SIGNAL, rawFile.indexOf("new S0()"), 8);
	}

	@Test
	def checkQueriedPortIsOwned() {
		'''
			class Cl1 {
				port Po {}
				void foo() {
					send null to this->(Po);
				}
			}
			class Cl2 {
				port Po {}
				void foo() {
					send null to this->(Po);
				}
			}
			class Cl3 {
				port Po {}
			}
			class Cl4 extends Cl3 {
				void foo() {
					send null to this->(Cl3.Po);
				}
			}
			class Cl5 extends Cl4 {
				void foo() {
					send null to this->(Cl3.Po);
				}
			}
		'''.parse.assertNoError(QUERIED_PORT_IS_NOT_OWNED);

		val rawFile = '''
			class Cl1 {
				port Po {}
				void foo() {
					send null to this->(Cl2.Po);
				}
			}
			class Cl2 {
				port Po {}
				void foo() {
					send null to this->(Cl1.Po);
				}
			}
			class Cl3 {
				void bar() {
					send null to this->(Cl4.Po);
				}
			}
			class Cl4 extends Cl3 {
				port Po {}
			}
			class Cl5 extends Cl3 {
				void foo() {
					send null to this->(Cl4.Po);
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_SEND_SIGNAL_EXPRESSION, QUERIED_PORT_IS_NOT_OWNED, rawFile.indexOf("this->(Cl2.Po)"),
			14);
		parsedFile.assertError(TU_SEND_SIGNAL_EXPRESSION, QUERIED_PORT_IS_NOT_OWNED, rawFile.indexOf("this->(Cl1.Po)"),
			14);
		parsedFile.assertError(TU_SEND_SIGNAL_EXPRESSION, QUERIED_PORT_IS_NOT_OWNED,
			rawFile.indexOfNth("this->(Cl4.Po)", 0), 14);
		parsedFile.assertError(TU_SEND_SIGNAL_EXPRESSION, QUERIED_PORT_IS_NOT_OWNED,
			rawFile.indexOfNth("this->(Cl4.Po)", 1), 14);
	}

	@Test
	def checkAccessedClassPropertyIsSpecified() {
		'''
			class Cl1 {
				void foo() {
					this->(Cl1Cl2.cl2);
				}
			}
			class Cl2;
			association Cl1Cl2 {
				Cl1 cl1;
				Cl2 cl2;
			}
		'''.parse.assertNoError(MISSING_CLASS_PROPERTY);

		val rawFile = '''
			class Cl1 {
				void foo() {
					this->();
				}
			}
			class Cl2;
			association Cl1Cl2 {
				Cl1 cl1;
				Cl2 cl2;
			}
		''';

		rawFile.parse.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, MISSING_CLASS_PROPERTY, rawFile.indexOf("->"), 2);
	}

	@Test
	def checkBindExpressionPartIsNotMissing() {
		val valid = '''
			class A {
				port P {}
				void f() {
					B b = new B();

					link this, b via AB;
					link this as AB.a, b via AB;
					link this, b as AB.b via AB;
					link this as AB.a, b as AB.b via AB;

					unlink this, b via AB;
					unlink this as AB.a, b via AB;
					unlink this, b as AB.b via AB;
					unlink this as AB.a, b as AB.b via AB;

					connect this->(A.P), b->(B.P) via CAB;
					connect this->(A.P) as CAB.ap, b->(B.P) via CAB;
					connect this->(A.P), b->(B.P) as CAB.bp via CAB;
					connect this->(A.P) as CAB.ap, b->(B.P) as CAB.bp via CAB;
				}
			}
			class B {
				port P {}
			}
			class C;
			association AB {
				A a;
				B b;
			}
			composition CA {
				container C c;
				A a;
			}
			composition CB {
				container C c;
				B b;
			}
			connector CAB {
				CA.a->A.P ap;
				CB.b->B.P bp;
			}
		'''.parse;

		valid.assertNoError(MISSING_CONNECTIVE_IN_BIND_EXPRESSION);
		valid.assertNoError(MISSING_END_IN_BIND_EXPRESSION);

		val missingPartsRaw = '''
			class A {
				port P {}
				void f() {
					B b = new B();

					link this, b via;
					link this as, b via AB;
					link this, b as via AB;
					link this as, b as via;

					unlink this, b via;
					unlink this as, b via AB;
					unlink this, b as via AB;
					unlink this as, b as via;

					connect this->(A.P), b->(B.P) via;
					connect this->(A.P) as, b->(B.P) via CAB;
					connect this->(A.P), b->(B.P) as via CAB;
					connect this->(A.P) as, b->(B.P) as via;
				}
			}
			class B {
				port P {}
			}
			class C;
			association AB {
				A a;
				B b;
			}
			composition CA {
				container C c;
				A a;
			}
			composition CB {
				container C c;
				B b;
			}
			connector CAB {
				CA.a->A.P ap;
				CB.b->B.P bp;
			}
		''';

		val missingPartsParsed = missingPartsRaw.parse;
		#[0, 1, 2].forEach[
			missingPartsParsed.assertError(TU_BIND_EXPRESSION, MISSING_CONNECTIVE_IN_BIND_EXPRESSION,
				missingPartsRaw.indexOfNth("via", it * 4), 3);
			missingPartsParsed.assertError(TU_BIND_EXPRESSION, MISSING_END_IN_BIND_EXPRESSION,
				missingPartsRaw.indexOfNth("as", it * 4 + 1), 2);
			missingPartsParsed.assertError(TU_BIND_EXPRESSION, MISSING_END_IN_BIND_EXPRESSION,
				missingPartsRaw.indexOfNth("as", it * 4 + 2), 2);
			missingPartsParsed.assertError(TU_BIND_EXPRESSION, MISSING_END_IN_BIND_EXPRESSION,
				missingPartsRaw.indexOfNth("as", it * 4 + 3), 2);
			missingPartsParsed.assertError(TU_BIND_EXPRESSION, MISSING_END_IN_BIND_EXPRESSION,
				missingPartsRaw.indexOfNth("as", it * 4 + 4), 2);
			missingPartsParsed.assertError(TU_BIND_EXPRESSION, MISSING_CONNECTIVE_IN_BIND_EXPRESSION,
				missingPartsRaw.indexOfNth("via", it * 4 + 3), 3);
		]
	}

	@Test
	def checkOwnerOfAccessedClassProperty() {
		val accessibleProps = '''
			class Cl1 {
				port Po {}
				void foo() {
					this->(Cl1Cl2.cl2);
					this->(Po);
				}
			}
			class Cl1D extends Cl1 {
				void bar() {
					this->(Cl1Cl2.cl2);
					this->(Cl1.Po);
				}
			}
			class Cl1DD extends Cl1D {
				void baz() {
					this->(Cl1Cl2.cl2);
					this->(Cl1.Po);
				}
			}
			class Cl2 {
				port Po {}
				void bar() {
					this->(Cl1Cl2.cl1);
					this->(Po);
				}
			}
			association Cl1Cl2 {
				Cl1 cl1;
				Cl2 cl2;
			}
		'''.parse;

		accessibleProps.assertNoError(NOT_NAVIGABLE_ASSOCIATION_END);
		accessibleProps.assertNoError(NOT_ACCESSIBLE_ASSOCIATION_END);
		accessibleProps.assertNoError(NOT_ACCESSIBLE_PORT);

		val notAccessiblePropsRaw = '''
			class Foo {
				port Po {}
				void foo() {
					this->(FooBar1.foo);
					this->(FooBar2.bar);
					this->(FooD1Bar.bar);
					this->(FooD1.FooDPo);
				}
			}
			class FooD1 extends Foo {
				port FooDPo {}
			}
			class FooD2 extends Foo {
				void bar() {
					this->(FooD1.FooDPo);
				}
			}
			class Bar {
				port Po {}
				void bar() {
					this->(FooBar1.bar);
					this->(Foo.Po);
				}
			}
			association FooBar1 {
				Foo foo;
				Bar bar;
			}
			association FooBar2 {
				Foo foo;
				hidden Bar bar;
			}
			association FooD1Bar {
				FooD1 fooD1;
				Bar bar;
			}
		''';

		val notAccessiblePropsParsed = notAccessiblePropsRaw.parse;
		notAccessiblePropsParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_ASSOCIATION_END,
			notAccessiblePropsRaw.indexOf("FooBar1.foo"), 11);
		notAccessiblePropsParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_NAVIGABLE_ASSOCIATION_END,
			notAccessiblePropsRaw.indexOf("FooBar2.bar)"), 11);
		notAccessiblePropsParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_ASSOCIATION_END,
			notAccessiblePropsRaw.indexOf("FooD1Bar.bar"), 12);
		notAccessiblePropsParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_PORT,
			notAccessiblePropsRaw.indexOfNth("FooD1.FooDPo", 0), 12);

		notAccessiblePropsParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_PORT,
			notAccessiblePropsRaw.indexOfNth("FooD1.FooDPo", 1), 12);
		notAccessiblePropsParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_ASSOCIATION_END,
			notAccessiblePropsRaw.indexOf("FooBar1.bar"), 11);
		notAccessiblePropsParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, NOT_ACCESSIBLE_PORT,
			notAccessiblePropsRaw.indexOf("Foo.Po"), 6);
	}

}
