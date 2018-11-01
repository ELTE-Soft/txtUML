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
import static org.eclipse.xtext.xbase.XbasePackage.Literals.*

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
				external Class a6;
				void o1(int p) {}
				int o2(boolean p) {}
				boolean o3(double p) {}
				double o4(String p) {}
				Foo o5(Foo p) {}
				E o6(E e) {}
				Sig o7(Sig s) {}
				external Class o8(Class p) {}
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
				external-body Class o4(Class p) {}
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
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Class", 4), 5);
		parsedFile.assertError(JVM_PARAMETERIZED_TYPE_REFERENCE, INVALID_TYPE, rawFile.indexOfNth("Class", 5), 5);
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
	def checkStartObjectExpressionTypes() {
		'''
			class Foo {
				void foo() {
					start this;
				}
			}
		'''.parse.assertNoError(TYPE_MISMATCH);

		val nullRaw = '''
			class Foo {
				void foo() {
					start null;
				}
			}
		''';

		nullRaw.parse.assertError(TU_START_OBJECT_EXPRESSION, TYPE_MISMATCH, nullRaw.indexOf("null"), 4);

		val stringRaw = '''
			class Foo {
				void foo() {
					start "object";
				}
			}
		''';

		stringRaw.parse.assertError(TU_START_OBJECT_EXPRESSION, TYPE_MISMATCH, stringRaw.indexOf('"object"'), 8);
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
	def checkLogExpressionTypes() {
		'''
			class Foo {
				void foo() {
					log "message";
					log-error "message";
				}
			}
		'''.parse.assertNoError(TYPE_MISMATCH);

		val nullRaw = '''
			class Foo {
				void foo() {
					log null;
					log-error null;
				}
			}
		''';

		nullRaw.parse.assertError(TU_LOG_EXPRESSION, TYPE_MISMATCH, nullRaw.indexOfNth("null", 0), 4);
		nullRaw.parse.assertError(TU_LOG_EXPRESSION, TYPE_MISMATCH, nullRaw.indexOfNth("null", 1), 4);

		val intRaw = '''
			class Foo {
				void foo() {
					log 24;
					log-error 24;
				}
			}
		''';

		intRaw.parse.assertError(TU_LOG_EXPRESSION, TYPE_MISMATCH, intRaw.indexOfNth("24", 0), 2);
		intRaw.parse.assertError(TU_LOG_EXPRESSION, TYPE_MISMATCH, intRaw.indexOfNth("24", 1), 2);
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

	@Test
	def checkBindExpressionTypes() {
		val valid = '''
			package test.model;
			class A {
				void f() {
					AD ad = new AD();
					B b = new B();
					C c = new C();

					// link, 1-1 match
					link this, b via AB;
					link this as AB.a, b via AB;
					link this, b as AB.b via AB;
					link this as AB.a, b as AB.b via AB;

					// link, 2-2 match
					link this as AA.e, this via AA;
					link this, this as AA.f via AA;
					link this as AA.e, this as AA.f via AA;

					// link, 1-2 match
					link this, ad via AAD;
					link this as AAD.a, ad via AAD;
					link this, ad as AAD.ad via AAD;
					link this as AAD.a, ad as AAD.ad via AAD;

					// link, 2-1 match
					link ad, this via AAD;
					link ad as AAD.ad, this via AAD;
					link ad, this as AAD.a via AAD;
					link ad as AAD.ad, this as AAD.a via AAD;

					// unlink, 1-1 match
					unlink this, b via AB;
					unlink this as AB.a, b via AB;
					unlink this, b as AB.b via AB;
					unlink this as AB.a, b as AB.b via AB;

					// unlink, 2-2 match
					unlink this as AA.e, this via AA;
					unlink this, this as AA.f via AA;
					unlink this as AA.e, this as AA.f via AA;

					// unlink, 1-2 match
					unlink this, ad via AAD;
					unlink this as AAD.a, ad via AAD;
					unlink this, ad as AAD.ad via AAD;
					unlink this as AAD.a, ad as AAD.ad via AAD;

					// unlink, 2-1 match
					unlink ad, this via AAD;
					unlink ad as AAD.ad, this via AAD;
					unlink ad, this as AAD.a via AAD;
					unlink ad as AAD.ad, this as AAD.a via AAD;

					// connect, assembly, 1-1 match
					connect this->(P), b->(B.P) via CAB;
					connect this->(P) as CAB.e, b->(B.P) via CAB;
					connect this->(P), b->(B.P) as CAB.f via CAB;
					connect this->(P) as CAB.e, b->(B.P) as CAB.f via CAB;

					// connect, assembly, 2-2 match
					connect this->(P) as CAA.e, this->(P) via CAA;
					connect this->(P), this->(P) as CAA.f via CAA;
					connect this->(P) as CAA.e, this->(P) as CAA.f via CAA;

					// connect, delegation, 1-1 match
					connect c->(C.P), this->(P) via DCA;
					connect c->(C.P) as DCA.e, this->(P) via DCA;
					connect c->(C.P), this->(P) as DCA.f via DCA;
					connect c->(C.P) as DCA.e, this->(P) as DCA.f via DCA;

					// connect, delegation, 1-1 match, reverse
					connect this->(P), c->(C.P) via DCA;
					connect this->(P) as DCA.f, c->(C.P) via DCA;
					connect this->(P), c->(C.P) as DCA.e via DCA;
					connect this->(P) as DCA.f, c->(C.P) as DCA.e via DCA;

					// connect, delegation, 2-2 match
					connect c->(C.P) as DCC.e, c->(C.P) via DCC;
					connect c->(C.P), c->(C.P) as DCC.f via DCC;
					connect c->(C.P) as DCC.e, c->(C.P) as DCC.f via DCC;

					// connect, delegation, 2-2 match, reverse
					connect c->(C.P) as DCC.f, c->(C.P) via DCC;
					connect c->(C.P), c->(C.P) as DCC.e via DCC;
					connect c->(C.P) as DCC.f, c->(C.P) as DCC.e via DCC;
				}
				port P {}
			}
			class AD extends A;
			class B {
				port P {}
			}
			class C {
				port P {}
			}
			association AB {
				A a;
				B b;
			}
			association AA {
				A e;
				A f;
			}
			association AAD {
				A a;
				AD ad;
			}
			composition CA {
				container C c;
				A a;
			}
			composition CA2 {
				container C c;
				A a;
			}
			composition CB {
				container C c;
				B b;
			}
			composition CC {
				container C e;
				C f;
			}
			connector CAB {
				CA.a->A.P e;
				CB.b->B.P f;
			}
			connector CAA {
				CA.a->A.P e;
				CA2.a->A.P f;
			}
			delegation DCA {
				CA.c->C.P e;
				CA.a->A.P f;
			}
			delegation DCC {
				CC.e->C.P e;
				CC.f->C.P f;
			}
		'''.parse;

		valid.assertNoError(TYPE_MISMATCH);
		valid.assertNoError(AMBIGUOUS_BIND_EXPRESSION);
		valid.assertNoError(DUPLICATE_END_IN_BIND_EXPRESSION);
		valid.assertNoError(END_MISMATCH_IN_BIND_EXPRESSION);
		valid.assertNoError(CONNECTIVE_KIND_MISMATCH_IN_BIND_EXPRESSION);

		val invalidRaw = '''
			package test.model;
			class A {
				C c;
				B b;
				port P {}
				void connectiveKindMismatch() {
					link this, b via CAB;
					link this, b via DCA;
					unlink this, b via CAB;
					unlink this, b via DCA;
					connect this->(A.P), b->(B.P) via AB;
					connect this->(A.P), b->(B.P) via CA;
				}
				void endMismatch() {
					link this as CA.a, b via AB;
					link this, b as CB.b via AB;
					link this as CA.a, b as CB.b via AB;
					unlink this as CA.a, b via AB;
					unlink this, b as CB.b via AB;
					unlink this as CA.a, b as CB.b via AB;
					connect this->(A.P) as DCA.ap, b->(B.P) via CAB;
					connect this->(A.P), b->(B.P) as DCA.cp via CAB;
					connect this->(A.P) as DCA.ap, b->(B.P) as DCA.cp via CAB;
				}
				void duplicateEnd() {
					link this as AB.a, b as AB.a via AB;
					unlink this as AB.a, b as AB.a via AB;
					connect this->(A.P) as CAB.ap, b->(B.P) as CAB.ap via CAB;
				}
				void typeMismatch() {
					link this, c via AB;
					link c, this via AB;
					link this as CA.c, c via CA;
					link this, c as CA.a via CA;
					link this as AB.b, b via AB;
					link this, b as AB.a via AB;
					link this as AB.b, c as AB.a via AB;
					unlink this, c via AB;
					unlink c, this via AB;
					unlink this as CA.c, c via CA;
					unlink this, c as CA.a via CA;
					unlink this as AB.b, b via AB;
					unlink this, b as AB.a via AB;
					unlink this as AB.b, c as AB.a via AB;
					connect this->(A.P), c->(C.P) via CAB;
					connect c->(C.P), this->(A.P) via CAB;
					connect this->(A.P) as CAB.bp, c->(C.P) via CAB;
					connect this->(A.P), c->(C.P) as CAB.ap via CAB;
					connect this->(A.P) as CAB.bp, c->(C.P) as CAB.ap via CAB;
				}
				void ambiguity() {
					link this, this via AA;
					link this, b via AA;
					link b, this via AA;
					link b, b via AA;
					link b, b via AB;
					unlink this, this via AA;
					unlink this, b via AA;
					unlink b, this via AA;
					unlink b, b via AB;
					unlink b, b via AB;
					connect this->(A.P), this->(A.P) via CAA;
					connect c->(C.P), c->(C.P) via DCC;
				}
			}
			class B extends A {
				port P {}
			}
			class C {
				port P {}
			}
			association AA {
				A a1;
				A a2;
			}
			association AB {
				A a;
				B b;
			}
			composition CA {
				container C c;
				A a;
			}
			composition CA2 {
				container C c;
				A a;
			}
			composition CB {
				container C c;
				B b;
			}
			composition CC {
				container C c1;
				C c2;
			}
			connector CAB {
				CA.a->A.P ap;
				CB.b->B.P bp;
			}
			connector CAA {
				CA.a->A.P ap1;
				CA2.a->A.P ap2;
			}
			delegation DCA {
				CA.c->C.P cp;
				CA.a->A.P ap;
			}
			delegation DCC {
				CC.c1->C.P cp1;
				CC.c2->C.P cp2;
			}
		''';

		val invalidParsed = invalidRaw.parse;

		invalidParsed.assertError(TU_BIND_EXPRESSION, CONNECTIVE_KIND_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CAB", 0), 3);
		invalidParsed.assertError(TU_BIND_EXPRESSION, CONNECTIVE_KIND_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("DCA", 0), 3);
		invalidParsed.assertError(TU_BIND_EXPRESSION, CONNECTIVE_KIND_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("DCA", 1), 3);
		invalidParsed.assertError(TU_BIND_EXPRESSION, CONNECTIVE_KIND_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("DCA", 1), 3);
		invalidParsed.assertError(TU_BIND_EXPRESSION, CONNECTIVE_KIND_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("AB", 2), 2);
		invalidParsed.assertError(TU_BIND_EXPRESSION, CONNECTIVE_KIND_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA", 4), 2);

		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA.a", 0), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA.b", 0), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA.a", 1), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA.b", 1), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA.a", 2), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA.b", 2), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA.a", 3), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CA.b", 3), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("DCA.ap", 0), 6);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("DCA.cp", 0), 6);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("DCA.ap", 1), 6);
		invalidParsed.assertError(TU_BIND_EXPRESSION, END_MISMATCH_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("DCA.cp", 1), 6);

		invalidParsed.assertError(TU_BIND_EXPRESSION, DUPLICATE_END_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("AB.a", 0), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, DUPLICATE_END_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("AB.a", 1), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, DUPLICATE_END_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("AB.a", 2), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, DUPLICATE_END_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("AB.a", 3), 4);
		invalidParsed.assertError(TU_BIND_EXPRESSION, DUPLICATE_END_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CAB.ap", 0), 6);
		invalidParsed.assertError(TU_BIND_EXPRESSION, DUPLICATE_END_IN_BIND_EXPRESSION,
			invalidRaw.indexOfNth("CAB.ap", 1), 6);

		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c ", 0), 1);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c,", 0), 1);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 20), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c ", 1), 1);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 21), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c ", 2), 1);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 22), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 23), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 24), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c ", 3), 1);

		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c ", 4), 1);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c,", 2), 1);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 27), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c ", 5), 1);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 28), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c ", 6), 1);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 29), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 30), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("this", 31), 4);
		invalidParsed.assertError(XFEATURE_CALL, TYPE_MISMATCH, invalidRaw.indexOfNth("c ", 7), 1);

		invalidParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH,
			invalidRaw.indexOfNth("c->(C.P)", 0), 8);
		invalidParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH,
			invalidRaw.indexOfNth("c->(C.P)", 1), 8);
		invalidParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH,
			invalidRaw.indexOfNth("this->(A.P)", 8), 11);
		invalidParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH,
			invalidRaw.indexOfNth("c->(C.P)", 2), 8);
		invalidParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH,
			invalidRaw.indexOfNth("this->(A.P)", 9), 11);
		invalidParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH,
			invalidRaw.indexOfNth("c->(C.P)", 3), 8);
		invalidParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH,
			invalidRaw.indexOfNth("this->(A.P)", 10), 11);
		invalidParsed.assertError(TU_CLASS_PROPERTY_ACCESS_EXPRESSION, TYPE_MISMATCH,
			invalidRaw.indexOfNth("c->(C.P)", 4), 8);

		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("link this, this via AA"), 22);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("link this, b via AA"), 19);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("link b, this via AA"), 19);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("link b, b via AA"), 16);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("link b, b via AB"), 16);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("unlink this, this via AA"), 24);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("unlink this, b via AA"), 21);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("unlink b, this via AA"), 21);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("unlink b, b via AA"), 18);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("unlink b, b via AB"), 18);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("connect this->(A.P), this->(A.P) via CAA"), 40);
		invalidParsed.assertError(TU_BIND_EXPRESSION, AMBIGUOUS_BIND_EXPRESSION,
			invalidRaw.indexOf("connect c->(C.P), c->(C.P) via DCC"), 34);
	}

}
