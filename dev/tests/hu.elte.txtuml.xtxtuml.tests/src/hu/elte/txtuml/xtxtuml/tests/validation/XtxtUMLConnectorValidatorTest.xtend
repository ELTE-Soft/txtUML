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
class XtxtUMLConnectorValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;
	@Inject extension XtxtUMLValidationTestUtils;

	@Test
	def checkConnectorHasExactlyTwoEnds() {
		'''
			connector AC {
				r->p e1;
				r->p e2;
			}
			delegation DC {
				r->p e1;
				r->p e2;
			}
		'''.parse.assertNoError(CONNECTOR_END_COUNT_MISMATCH);

		val rawFile = '''
			connector C1 {}
			connector C2 {
				r->p e1;
			}
			connector C3 {
				r->p e1;
				r->p e2;
				r->p e3;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_CONNECTOR, CONNECTOR_END_COUNT_MISMATCH, rawFile.indexOf("C1"), 2);
		parsedFile.assertError(TU_CONNECTOR, CONNECTOR_END_COUNT_MISMATCH, rawFile.indexOf("C2"), 2);
		parsedFile.assertError(TU_CONNECTOR, CONNECTOR_END_COUNT_MISMATCH, rawFile.indexOf("C3"), 2);
	}

	@Test
	def checkContainerEndIsAllowedAndNeededOnlyInDelegation() {
		val validFile = '''
			composition Co {
				container Cl1 e1;
				Cl2 e2;
			}
			delegation DC {
				Co.e1->p e1;
				Co.e2->q e2;
			}
		'''.parse;

		validFile.assertNoError(CONTAINER_ROLE_COUNT_MISMATCH);
		validFile.assertNoError(CONTAINER_ROLE_IN_ASSSEMBLY_CONNECTOR);

		val invalidFileRaw = '''
			composition Co {
				container Cl1 e1;
				Cl2 e2;
			}
			connector AC {
				Co.e1->p e1;
				Co.e2->q e2;
			}
			delegation DC1 {
				Co.e2->q e1;
				Co.e2->q e2;
			}
			delegation DC2 {
				Co.e1->p e1;
				Co.e1->p e2;
			}
		''';

		val invalidFileParsed = invalidFileRaw.parse;
		invalidFileParsed.assertError(TU_CONNECTOR_END, CONTAINER_ROLE_IN_ASSSEMBLY_CONNECTOR,
			invalidFileRaw.indexOf("Co.e1"), 5);
		invalidFileParsed.assertError(TU_CONNECTOR, CONTAINER_ROLE_COUNT_MISMATCH, invalidFileRaw.indexOf("DC1"), 3);
		invalidFileParsed.assertError(TU_CONNECTOR, CONTAINER_ROLE_COUNT_MISMATCH, invalidFileRaw.indexOf("DC2"), 3);
	}

	@Test
	def checkCompositionsBehindConnectorEnds() {
		val validFile = '''
			class A;
			class B;
			class C;
			composition CA {
				container C c;
				A a;
			}
			composition CB {
				container C c;
				B b;
			}
			connector AC {
				CA.a->p e1;
				CB.b->q e2;
			}
			connector AA {
				CA.a->p e1;
				CA.a->p e2;
			}
			delegation DC {
				CA.c->q e1;
				CA.a->p e2;
			}
		'''.parse;

		validFile.assertNoError(COMPOSITION_MISMATCH_IN_ASSEMBLY_CONNECTOR);
		validFile.assertNoError(COMPOSITION_MISMATCH_IN_DELEGATION_CONNECTOR);

		val rawFile = '''
			class A;
			class B;
			class C;
			class D;
			composition CA {
				container C c;
				A a;
			}
			composition CB {
				container C c;
				B b;
			}
			composition DB {
				container D d;
				B b;
			}
			connector AC {
				CA.a->p e1;
				DB.b->q e2;
			}
			delegation DC {
				CB.c->r e1;
				DB.b->q e2;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_CONNECTOR, COMPOSITION_MISMATCH_IN_ASSEMBLY_CONNECTOR, rawFile.indexOf("AC"), 2);
		parsedFile.assertError(TU_CONNECTOR, COMPOSITION_MISMATCH_IN_DELEGATION_CONNECTOR, rawFile.indexOf("DC"), 2);
	}

	@Test
	def checkConnectorEndPortCompatibility() {
		'''
			interface I1 {}
			interface I2 {}
			interface I3 {}
			class A {
				port P1 {
					provided I1;
					required I2;
				}
				port P2 {
					provided I3;
				}
				port P3 {
					provided I2;
					required I1;
				}
				port P4 {
					provided I3;
				}
				port P5 {}
				port P6 {
					provided I1;
					required I1;
				}
				port P7 {
					required I3;
				}
			}
			connector C1 {
				r->A.P1 e1;
				s->A.P3 e2;
			}
			connector C2 {
				r->A.P5 e1;
				s->A.P5 e2;
			}
			connector C3 {
				r->A.P6 e1;
				s->A.P6 e2;
			}
			connector C4 {
				r->A.P2 e1;
				s->A.P7 e2;
			}
			delegation D1 {
				r->A.P2 e1;
				s->A.P4 e2;
			}
			delegation D2 {
				r->A.P1 e1;
				s->A.P1 e2;
			}
		'''.parse.assertNoError(INCOMPATIBLE_PORTS);

		val rawFile = '''
			interface I1 {}
			interface I2 {}
			interface I3 {}
			class A {
				port P1 {
					provided I1;
					required I2;
				}
				port P2 {
					provided I2;
				}
				port P3 {
					required I3;
				}
				port P4 {
					provided I2;
					required I3;
				}
			}
			connector C1 {
				r->A.P1 e1;
				s->A.P1 e2;
			}
			connector C2 {
				r->A.P1 e1;
				s->A.P2 e2;
			}
			connector C3 {
				r->A.P2 e1;
				s->A.P3 e2;
			}
			delegation D1 {
				r->A.P1 e1;
				s->A.P4 e2;
			}
			delegation D2 {
				r->A.P2 e1;
				s->A.P3 e2;
			}
			delegation D3 {
				r->A.P2 e1;
				s->A.P4 e2;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_CONNECTOR, INCOMPATIBLE_PORTS, rawFile.indexOf("C1"), 2);
		parsedFile.assertError(TU_CONNECTOR, INCOMPATIBLE_PORTS, rawFile.indexOf("C2"), 2);
		parsedFile.assertError(TU_CONNECTOR, INCOMPATIBLE_PORTS, rawFile.indexOf("C3"), 2);
		parsedFile.assertError(TU_CONNECTOR, INCOMPATIBLE_PORTS, rawFile.indexOf("D1"), 2);
		parsedFile.assertError(TU_CONNECTOR, INCOMPATIBLE_PORTS, rawFile.indexOf("D2"), 2);
		parsedFile.assertError(TU_CONNECTOR, INCOMPATIBLE_PORTS, rawFile.indexOf("D3"), 2);
	}

	@Test
	def checkOwnerOfConnectorEndPort() {
		'''
			class A { port P {} }
			class B { port P {} }
			class C extends A;
			class D extends C;
			composition AB {
				container A a;
				B b;
			}
			composition DB {
				container D d;
				B b;
			}
			delegation DAB {
				AB.a->A.P e1;
				AB.b->B.P e2;
			}
			delegation DDB {
				DB.d->A.P e1;
				DB.b->B.P e2;
			}
		'''.parse.assertNoError(NOT_OWNED_PORT);

		val rawFile = '''
			class A { port P {} }
			class B { port P {} }
			class C extends A;
			class D extends C {
				port P {}
			}
			composition AB {
				container A a;
				B b;
			}
			delegation DAB1 {
				AB.a->B.P e1;
				AB.b->A.P e2;
			}
			delegation DAB2 {
				AB.a->B.P e3;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_CONNECTOR_END, NOT_OWNED_PORT, rawFile.indexOfNth("B.P", 0), 3);
		parsedFile.assertError(TU_CONNECTOR_END, NOT_OWNED_PORT, rawFile.indexOf("A.P"), 3);
		parsedFile.assertError(TU_CONNECTOR_END, NOT_OWNED_PORT, rawFile.indexOfNth("B.P", 1), 3);
	}

}
