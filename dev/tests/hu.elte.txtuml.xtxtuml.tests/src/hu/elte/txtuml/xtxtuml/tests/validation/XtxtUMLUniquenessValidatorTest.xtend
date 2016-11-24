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
class XtxtUMLUniquenessValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;

	@Test
	def checkModelElementNameIsUniqueExternal() {
		'''
			package java.lang;
			execution __4416cWAUcf77 {}
			signal __4416cWAUcf78;
			class __4416cWAUcf79;
			association __4416cWAUcf80 {}
			interface __4416cWAUcf81 {}
			connector __4416cWAUcf82 {}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val file = '''
			package java.lang;
			execution Object {}
			signal Double;
			class Float;
			association Exception {}
			interface Error {}
			connector String {}
		'''.parse;

		file.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 30, 6);
		file.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 48, 6);
		file.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 63, 5);
		file.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 83, 9);
		file.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 107, 5);
		file.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 127, 6);
	}

	@Test
	def checkModelElementNameIsUniqueInternal() {
		'''
			class A;
			class B;
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val sameType = '''
			class A;
			class A;
			class B;
			class b;
		'''.parse;

		sameType.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 6, 1);
		sameType.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 16, 1);
		sameType.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 26, 1);
		sameType.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 36, 1);

		val differentTypes = '''
			class A;
			signal A;
			class B;
			signal b;
		'''.parse;

		differentTypes.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 6, 1);
		differentTypes.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 17, 1);
		differentTypes.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 27, 1);
		differentTypes.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, 38, 1);
	}

	@Test
	def checkReceptionIsUnique() {
		'''
			signal S;
			interface I {
				reception S;
			}
		'''.parse.assertNoError(NOT_UNIQUE_RECEPTION);

		val file = '''
			signal S;
			interface I {
				reception S;
				reception S;
			}
		'''.parse;

		file.assertError(TU_RECEPTION, NOT_UNIQUE_RECEPTION, 37, 1);
		file.assertError(TU_RECEPTION, NOT_UNIQUE_RECEPTION, 52, 1);
	}

	@Test
	def checkSignalAttributeNameIsUnique() {
		'''
			signal S {
				int a;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val file = '''
			signal S {
				int a;
				int a;
				double a;
			}
		'''.parse;

		file.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_NAME, 17, 1);
		file.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_NAME, 26, 1);
		file.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_NAME, 38, 1);
	}

	@Test
	def checkAttributeNameIsUnique() {
		'''
			class A {
				int a;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val file = '''
			class A {
				int a;
				int a;
				double a;
			}
		'''.parse;

		file.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, 16, 1);
		file.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, 25, 1);
		file.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, 37, 1);
	}

	@Test
	def checkConstructorIsUnique() {
		'''
			class A {
				public A() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_CONSTRUCTOR);

		'''
			class A {
				public A() {}
				public A(int a) {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_CONSTRUCTOR);

		val file = '''
			class A {
				public A() {}
				A() {}
				public A(int a) {}
				A(int a) {}
			}
		'''.parse;

		file.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, 19, 1);
		file.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, 28, 1);
		file.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, 44, 1);
		file.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, 58, 1);
	}

	@Test
	def checkOperationIsUnique() {
		'''
			class A {
				void foo() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		'''
			class A {
				void foo() {}
				void bar() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		'''
			package test.model;
			class A {
				void foo() {}
				void foo(int a) {}
				void foo(double a) {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		val file = '''
			package test.model;
			class A {
				void foo() {}
				int foo() { return 0; }
				void foo(int a) {}
				void foo(int b) {}
			}
		'''.parse;

		file.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, 38, 3);
		file.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, 53, 3);
		file.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, 80, 3);
		file.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, 101, 3);
	}

	@Test
	def checkInitialStateIsUnique() {
		'''
			class A {
				initial Init;
			}
		'''.parse.assertNoError(NOT_UNIQUE_INITIAL_STATE);

		'''
			class A {
				initial Init;
				composite CS {
					initial Init;
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_INITIAL_STATE);

		val file = '''
			class A {
				initial Init1;
				initial Init2;
				composite CS {
					initial Init3;
					initial Init4;
				}
			}
		'''.parse;

		file.assertError(TU_STATE, NOT_UNIQUE_INITIAL_STATE, 20, 5);
		file.assertError(TU_STATE, NOT_UNIQUE_INITIAL_STATE, 37, 5);
		file.assertError(TU_STATE, NOT_UNIQUE_INITIAL_STATE, 72, 5);
		file.assertError(TU_STATE, NOT_UNIQUE_INITIAL_STATE, 90, 5);
	}

	@Test
	def checkNestedClassMemberNameIsUnique() {
		'''
			class A {
				state S;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		'''
			class A {
				transition T {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		'''
			class A {
				port P {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val states = '''
			class A {
				state S;
				state S;
				state s;
			}
		'''.parse;

		states.assertError(TU_STATE, NOT_UNIQUE_NAME, 18, 1);
		states.assertError(TU_STATE, NOT_UNIQUE_NAME, 29, 1);
		states.assertError(TU_STATE, NOT_UNIQUE_NAME, 40, 1);

		val transitions = '''
			class A {
				transition T {}
				transition T {}
				transition t {}
			}
		'''.parse;

		transitions.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, 23, 1);
		transitions.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, 41, 1);
		transitions.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, 59, 1);

		val ports = '''
			class A {
				port P {}
				port P {}
				port p {}
			}
		'''.parse;

		ports.assertError(TU_PORT, NOT_UNIQUE_NAME, 17, 1);
		ports.assertError(TU_PORT, NOT_UNIQUE_NAME, 29, 1);
		ports.assertError(TU_PORT, NOT_UNIQUE_NAME, 41, 1);

		val all = '''
			class A {
				state D;
				transition D {}
				port D {}
				state d;
				transition d {}
				port d {}
			}
		'''.parse;

		all.assertError(TU_STATE, NOT_UNIQUE_NAME, 18, 1);
		all.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, 34, 1);
		all.assertError(TU_PORT, NOT_UNIQUE_NAME, 46, 1);
		all.assertError(TU_STATE, NOT_UNIQUE_NAME, 59, 1);
		all.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, 75, 1);
		all.assertError(TU_PORT, NOT_UNIQUE_NAME, 87, 1);
	}

	@Test
	def checkStateActivityIsUnique() {
		'''
			class A {
				state S {
					entry {}
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_STATE_ACTIVITY);

		'''
			class A {
				state S {
					entry {}
					exit {}
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_STATE_ACTIVITY);

		val file = '''
			class A {
				state S {
					entry {}
					entry {}
					exit {}
					exit {}
				}
			}
		'''.parse;

		file.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, NOT_UNIQUE_STATE_ACTIVITY, 25, 5);
		file.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, NOT_UNIQUE_STATE_ACTIVITY, 37, 5);
		file.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, NOT_UNIQUE_STATE_ACTIVITY, 49, 4);
		file.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, NOT_UNIQUE_STATE_ACTIVITY, 60, 4);
	}

	@Test
	def checkInitialTransitionIsUnique() {
		'''
			class A {
				initial I;
				state S;
				transition {
					from I;
					to S;
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_INITIAL_TRANSITION);

		'''
			class A {
				initial Init;
				transition T1 {
					from Init;
					to CS;
				}
				composite CS {
					initial Init;
					state S;
					transition T2 {
						from Init;
						to S;
					}
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_INITIAL_TRANSITION);

		val flat = '''
			class A {
				initial Init;
				state S1;
				state S2;
				transition T1 {
					from Init;
					to S1;
				}
				transition T2 {
					from Init;
					to S2;
				}
			}
		'''.parse;

		flat.assertError(TU_TRANSITION, NOT_UNIQUE_INITIAL_TRANSITION, 63, 2);
		flat.assertError(TU_TRANSITION, NOT_UNIQUE_INITIAL_TRANSITION, 109, 2);

		val hierarchical = '''
			class A {
				composite CS {
					initial Init;
					state S1;
					state S2;
					transition T1 {
						from Init;
						to S1;
					}
					transition T2 {
						from Init;
						to S2;
					}
				}
			}
		'''.parse;

		hierarchical.assertError(TU_TRANSITION, NOT_UNIQUE_INITIAL_TRANSITION, 84, 2);
		hierarchical.assertError(TU_TRANSITION, NOT_UNIQUE_INITIAL_TRANSITION, 134, 2);
	}

	@Test
	def checkTransitionMemberIsUnique() {
		'''
			signal Sig;
			class A {
				port P {}
				state St;
				transition T {
					from St;
					to St;
					trigger Sig;
					port P;
					guard ( true );
					effect {}
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_TRANSITION_MEMBER);

		val file = '''
			signal Sig;
			class A {
				port P {}
				state St;
				transition T {
					from St; from St;
					to St; to St;
					trigger Sig; trigger Sig;
					port P; port P;
					guard ( true ); guard ( true );
					effect {} effect {}
				}
			}
		'''.parse;

		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 67, 4);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 76, 4);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 88, 2);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 95, 2);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 105, 7);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 118, 7);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 134, 4);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 142, 4);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 153, 5);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 169, 5);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 188, 6);
		file.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, 198, 6);
	}

	@Test
	def checkPortMemberIsUnique() {
		'''
			interface I {}
			class A {
				port P {
					provided I;
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_PORT_MEMBER);

		'''
			interface I {}
			class A {
				port P {
					provided I;
					required I1;
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_PORT_MEMBER);

		val file = '''
			interface I1 {}
			interface I2 {}
			class A {
				port P {
					provided I1;
					provided I1;
					required I1;
					required I2;
				}
			}
		'''.parse

		file.assertError(TU_PORT_MEMBER, NOT_UNIQUE_PORT_MEMBER, 58, 8);
		file.assertError(TU_PORT_MEMBER, NOT_UNIQUE_PORT_MEMBER, 74, 8);
		file.assertError(TU_PORT_MEMBER, NOT_UNIQUE_PORT_MEMBER, 90, 8);
		file.assertError(TU_PORT_MEMBER, NOT_UNIQUE_PORT_MEMBER, 106, 8);
	}

	@Test
	def checkAssociationEndNamesAreUnique() {
		'''
			class A;
			class B;
			association AB {
				A a;
				B b;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		'''
			class A;
			association AA {
				A e1;
				A e2;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val file = '''
			class A;
			class B;
			association AB {
				A e;
				B e;
			}
			association AA {
				A a1;
				A A1;
			}
		'''.parse;

		file.assertError(TU_ASSOCIATION_END, NOT_UNIQUE_NAME, 41, 1);
		file.assertError(TU_ASSOCIATION_END, NOT_UNIQUE_NAME, 48, 1);
		file.assertError(TU_ASSOCIATION_END, NOT_UNIQUE_NAME, 76, 2);
		file.assertError(TU_ASSOCIATION_END, NOT_UNIQUE_NAME, 84, 2);
	}

	@Test
	def checkConnectorEndNamesAreUnique() {
		'''
			class A { port P {} }
			class B { port P {} }
			composition CAB {
				container A a;
				B b;
			}
			delegation DAB {
				CAB.a->A.P a;
				CAB.b->B.P b;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val file = '''
			class A { port P {} }
			class B { port P {} }
			composition CAB {
				container A a;
				B b;
			}
			delegation DAB1 {
				CAB.a->A.P a;
				CAB.b->B.P a;
			}
			delegation DAB2 {
				CAB.a->A.P a1;
				CAB.b->B.P A1;
			}
		'''.parse;

		file.assertError(TU_CONNECTOR_END, NOT_UNIQUE_NAME, 123, 1);
		file.assertError(TU_CONNECTOR_END, NOT_UNIQUE_NAME, 139, 1);
		file.assertError(TU_CONNECTOR_END, NOT_UNIQUE_NAME, 177, 2);
		file.assertError(TU_CONNECTOR_END, NOT_UNIQUE_NAME, 194, 2);
	}

}
