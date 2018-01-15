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
class XtxtUMLClassValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;
	@Inject extension XtxtUMLValidationTestUtils;

	@Test
	def checkNoCycleInSignalHiearchy() {
		'''
			signal A;
			signal B extends A;
			signal C extends B;
			signal D extends B;
			signal E extends C;
		'''.parse.assertNoError(SIGNAL_HIERARCHY_CYCLE);

		val rawFile = '''
			signal A extends A;
			signal B extends C;
			signal C extends D;
			signal D extends B;
			signal E extends D;
			signal F extends E;
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_SIGNAL, SIGNAL_HIERARCHY_CYCLE, rawFile.indexOfNth("A", 1), 1);
		parsedFile.assertError(TU_SIGNAL, SIGNAL_HIERARCHY_CYCLE, rawFile.indexOfNth("C", 0), 1);
		parsedFile.assertError(TU_SIGNAL, SIGNAL_HIERARCHY_CYCLE, rawFile.indexOfNth("D", 0), 1);
		parsedFile.assertError(TU_SIGNAL, SIGNAL_HIERARCHY_CYCLE, rawFile.indexOfNth("B", 1), 1);
		parsedFile.assertError(TU_SIGNAL, SIGNAL_HIERARCHY_CYCLE, rawFile.indexOfNth("D", 2), 1);
		parsedFile.assertError(TU_SIGNAL, SIGNAL_HIERARCHY_CYCLE, rawFile.indexOfNth("E", 1), 1);
	}

	@Test
	def checkNoCycleInClassHiearchy() {
		'''
			class A;
			class B extends A;
			class C extends B;
			class D extends B;
			class E extends C;
		'''.parse.assertNoError(CLASS_HIERARCHY_CYCLE);

		val rawFile = '''
			class A extends A;
			class B extends C;
			class C extends D;
			class D extends B;
			class E extends D;
			class F extends E;
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, rawFile.indexOfNth("A", 1), 1);
		parsedFile.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, rawFile.indexOfNth("C", 0), 1);
		parsedFile.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, rawFile.indexOfNth("D", 0), 1);
		parsedFile.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, rawFile.indexOfNth("B", 1), 1);
		parsedFile.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, rawFile.indexOfNth("D", 2), 1);
		parsedFile.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, rawFile.indexOfNth("E", 1), 1);
	}

	@Test
	def checkConstructorName() {
		'''
			class Foo {
				Foo() {}
			}
		'''.parse.assertNoError(INVALID_CONSTRUCTOR_NAME);

		val rawFile = '''
			class Foo {
				foo() {}
			}
		''';

		rawFile.parse.assertError(TU_CONSTRUCTOR, INVALID_CONSTRUCTOR_NAME, rawFile.indexOf("foo"), 3);
	}

	@Test
	def checkInitializerIsUsedOnlyOnExternalAttribute() {
		'''
			class Foo {
				external int bar = 0;
				static external boolean baz = true;
				int foobar;
			}
		'''.parse.assertNoError(INITIALIZER_ON_NON_EXTERNAL_ATTRIBUTE);

		val rawFile = '''
			class Foo {
				int bar = 0;
			}
		'''

		rawFile.parse.assertError(TU_ATTRIBUTE, INITIALIZER_ON_NON_EXTERNAL_ATTRIBUTE, rawFile.indexOf("0"), 1);
	}

	@Test
	def checkInitialStateIsDefined() {
		val noWarningFile = '''
			class Foo;
			class Bar {
				initial Init;
				composite CS;
			}
		'''.parse;

		noWarningFile.assertNoWarnings(TU_CLASS, MISSING_INITIAL_STATE);
		noWarningFile.assertNoWarnings(TU_STATE, MISSING_INITIAL_STATE);

		val rawFile = '''
			class Foo {
				composite CS {
					state St;
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertWarning(TU_CLASS, MISSING_INITIAL_STATE, rawFile.indexOf("Foo"), 3);
		parsedFile.assertWarning(TU_STATE, MISSING_INITIAL_STATE, rawFile.indexOf("CS"), 2);
	}

	@Test
	def checkPseudostateIsLeavable() {
		'''
			class Foo {
				initial Init;
				choice Ch;
				state St;
				transition T1 {
					from Init;
					to Ch;
				}
				transition T2 {
					from Ch;
					to St;
				}
			}
		'''.parse.assertNoError(NOT_LEAVABLE_PSEUDOSTATE);

		val rawFile = '''
			class Foo {
				initial Init;
				choice Ch;
				state St;
				transition Tr {
					from St;
					to Ch;
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_STATE, NOT_LEAVABLE_PSEUDOSTATE, rawFile.indexOf("Init"), 4);
		parsedFile.assertError(TU_STATE, NOT_LEAVABLE_PSEUDOSTATE, rawFile.indexOf("Ch"), 2);
	}

	@Test
	def checkStateIsReachable() {
		'''
			class Foo {
				initial Init;
				state S1;
				state S2;
				composite CS {
					state S3;
				}
				transition T1 {
					from Init;
					to S1;
				}
				transition T2 {
					from S1;
					to S2;
				}
				transition T3 {
					from S2;
					to CS;
				}
			}
		'''.parse.assertNoWarnings(TU_STATE, UNREACHABLE_STATE);

		val rawFile = '''
			class Foo {
				initial Init;
				state S1;
				state S2;
				state S3;
				composite CS {
					initial Init;
					state S1;
				}
				transition T1 {
					from Init;
					to S1;
				}
				transition T2 {
					from S1;
					to S2;
				}
				transition T3 {
					from S3;
					to CS;
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertWarning(TU_STATE, UNREACHABLE_STATE, rawFile.indexOfNth("S1", 1), 2);
		parsedFile.assertWarning(TU_STATE, UNREACHABLE_STATE, rawFile.indexOf("S3"), 2);
		parsedFile.assertWarning(TU_STATE, UNREACHABLE_STATE, rawFile.indexOf("CS"), 2);
	}

	@Test
	def checkStateOrTransitionIsDefinedInClassOrCompositeState() {
		'''
			class Foo {
				composite CS {
					state St;
					transition T2 {}
				}
				transition T1 {}
			}
		'''.parse.assertNoError(STATE_OR_TRANSITION_IN_NOT_COMPOSITE_STATE);

		val rawFile = '''
			class Foo {
				state PS {
					state St;
					transition T2 {}
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_STATE, STATE_OR_TRANSITION_IN_NOT_COMPOSITE_STATE, rawFile.indexOf("St"), 2);
		parsedFile.assertError(TU_TRANSITION, STATE_OR_TRANSITION_IN_NOT_COMPOSITE_STATE, rawFile.indexOf("T2"), 2);
	}

	@Test
	def checkNoActivityInPseudostate() {
		'''
			class Foo {
				state St {
					entry {}
					exit {}
				}
				composite CS {
					entry {}
					exit {}
				}
			}
		'''.parse.assertNoError(ACTIVITY_IN_PSEUDOSTATE);

		val rawFile = '''
			class Foo {
				initial Init {
					entry {}
					exit {}
				}
				choice Ch {
					entry {}
					exit {}
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, ACTIVITY_IN_PSEUDOSTATE, rawFile.indexOfNth("entry", 0), 5);
		parsedFile.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, ACTIVITY_IN_PSEUDOSTATE, rawFile.indexOfNth("exit", 0), 4);
		parsedFile.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, ACTIVITY_IN_PSEUDOSTATE, rawFile.indexOfNth("entry", 1), 5);
		parsedFile.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, ACTIVITY_IN_PSEUDOSTATE, rawFile.indexOfNth("exit", 1), 4);
	}

	@Test
	def checkMandatoryTransitionMembers() {
		'''
			signal Sig;
			class Foo {
				initial Init;
				state St;
				choice Ch;
				transition T1 {
					from Init;
					to St;
				}
				transition T2 {
					from Ch;
					to St;
				}
				transition T3 {
					from St;
					to Ch;
					trigger Sig;
				}
			}
		'''.parse.assertNoError(MISSING_MANDATORY_TRANSITION_MEMBER);

		val rawFile = '''
			signal Sig;
			class Foo {
				state St;
				transition T1 {
					to St;
					trigger Sig;
				}
				transition T2 {
					from St;
					trigger Sig;
				}
				transition T3 {
					from St;
					to St;
				}
				transition T4 {}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_TRANSITION, MISSING_MANDATORY_TRANSITION_MEMBER, rawFile.indexOf("T1"), 2);
		parsedFile.assertError(TU_TRANSITION, MISSING_MANDATORY_TRANSITION_MEMBER, rawFile.indexOf("T2"), 2);
		parsedFile.assertError(TU_TRANSITION, MISSING_MANDATORY_TRANSITION_MEMBER, rawFile.indexOf("T3"), 2);
		parsedFile.assertError(TU_TRANSITION, MISSING_MANDATORY_TRANSITION_MEMBER, rawFile.indexOf("T4"), 2);
	}

	@Test
	def checkTransitionTargetIsNotInitialState() {
		'''
			class Foo {
				state St;
				transition Tr {
					to St;
				}
			}
		'''.parse.assertNoError(TARGET_IS_INITIAL_STATE);

		val rawFile = '''
			class Foo {
				initial Init;
				transition Tr {
					to Init;
				}
			}
		''';

		rawFile.parse.assertError(TU_TRANSITION_VERTEX, TARGET_IS_INITIAL_STATE, rawFile.indexOfNth("Init", 1), 4);
	}

	@Test
	def checkGuardIsNotForInitialTransition() {
		'''
			class Foo {
				state St;
				transition Tr {
					from St;
					guard ( true );
				}
			}
		'''.parse.assertNoError(INVALID_TRANSITION_MEMBER);

		val rawFile = '''
			class Foo {
				initial Init;
				transition Tr {
					from Init;
					guard ( true );
				}
			}
		''';

		rawFile.parse.assertError(TU_TRANSITION_GUARD, INVALID_TRANSITION_MEMBER, rawFile.indexOf("guard"), 5);
	}

	@Test
	def checkMemberOfTransitionFromPseudostate() {
		'''
			signal Sig;
			class Foo {
				behavior port Po {}
				state St;
				composite CS;
				transition T1 {
					from St;
					trigger Sig;
					port Po;
				}
				transition T2 {
					from CS;
					trigger Sig;
					port Po;
				}
			}
		'''.parse.assertNoError(INVALID_TRANSITION_MEMBER);

		val rawFile = '''
			signal Sig;
			class Foo {
				behavior port Po {}
				initial Init;
				choice Ch;
				transition T1 {
					from Init;
					trigger Sig;
					port Po;
				}
				transition T2 {
					from Ch;
					trigger Sig;
					port Po;
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_TRANSITION_TRIGGER, INVALID_TRANSITION_MEMBER, rawFile.indexOfNth("trigger", 0), 7)
		parsedFile.assertError(TU_TRANSITION_PORT, INVALID_TRANSITION_MEMBER, rawFile.indexOfNth("port", 1), 4)
		parsedFile.assertError(TU_TRANSITION_TRIGGER, INVALID_TRANSITION_MEMBER, rawFile.indexOfNth("trigger", 1), 7)
		parsedFile.assertError(TU_TRANSITION_PORT, INVALID_TRANSITION_MEMBER, rawFile.indexOfNth("port", 2), 4);
	}

	@Test
	def checkElseGuard() {
		'''
			class Foo {
				choice Ch;
				transition T1 {
					from Ch;
					guard ( false );
				}
				transition T2 {
					from Ch;
					guard ( else );
				}
			}
		'''.parse.assertNoError(INVALID_ELSE_GUARD);

		val rawFile = '''
			class Foo {
				state St;
				composite CS;
				transition T1 {
					from St;
					guard ( else );
				}
				transition T2 {
					from CS;
					guard ( else );
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_TRANSITION_GUARD, INVALID_ELSE_GUARD, rawFile.indexOfNth("else", 0), 4);
		parsedFile.assertError(TU_TRANSITION_GUARD, INVALID_ELSE_GUARD, rawFile.indexOfNth("else", 1), 4);
	}

	@Test
	def checkOwnerOfTriggerPort() {
		'''
			class Foo {
				behavior port Po {}
				transition Tr {
					port Po;
				}
				composite CS {
					transition Tr {
						port Po;
					}
				}
			}
			class Bar extends Foo {
				transition Tr {
					port Foo.Po;
				}
			}
			class Baz extends Bar {
				composite CS {
					transition Tr {
						port Foo.Po;
					}
				}
			}
			class Doo {
				behavior port Po {}
			}
			class Ear extends Doo {
				transition Tr {
					port Doo.Po;
				}
			}
		'''.parse.assertNoError(NOT_OWNED_TRIGGER_PORT);

		val rawFile = '''
			class Foo {
				behavior port Po {}
				transition T1 {
					port Bar.Po;
				}
				transition T2 {
					port Baz.Po;
				}
			}
			class Bar {
				behavior port Po {}
				transition Tr {
					port Foo.Po;
				}
			}
			class Baz extends Foo {
				behavior port Po {}
				transition Tr {
					port Bar.Po;
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_TRANSITION_PORT, NOT_OWNED_TRIGGER_PORT, rawFile.indexOfNth("Bar.Po", 0), 6);
		parsedFile.assertError(TU_TRANSITION_PORT, NOT_OWNED_TRIGGER_PORT, rawFile.indexOf("Baz.Po"), 6);
		parsedFile.assertError(TU_TRANSITION_PORT, NOT_OWNED_TRIGGER_PORT, rawFile.indexOf("Foo.Po"), 6);
		parsedFile.assertError(TU_TRANSITION_PORT, NOT_OWNED_TRIGGER_PORT, rawFile.indexOfNth("Bar.Po", 1), 6);
	}

	@Test
	def checkTriggerPortIsBehavior() {
		'''
			class Foo {
				behavior port BP {}
				transition Tr {
					port BP;
				}
			}
		'''.parse.assertNoError(NOT_BEHAVIOR_TRIGGER_PORT);

		val rawFile = '''
			class Foo {
				port Po {}
				transition Tr {
					port Po;
				}
			}
		''';

		rawFile.parse.assertError(TU_TRANSITION_PORT, NOT_BEHAVIOR_TRIGGER_PORT, rawFile.indexOfNth("Po", 1), 2);
	}

	@Test
	def checkTransitionVertexLevel() {
		'''
			class Foo {
				composite CS {
					state St;
					transition T2 {
						from St;
						to St;
					}
				}
				transition T1 {
					from CS;
					to CS;
				}
			}
		'''.parse.assertNoError(VERTEX_LEVEL_MISMATCH);

		val rawFile = '''
			class Foo {
				composite CS {
					state St;
					transition T2 {
						from CS;
						to CS;
					}
					transition T3 {
						from St;
						to CS;
					}
				}
				transition T1 {
					from CS.St;
					to CS;
				}
				transition T4 {
					from CS;
					to Bar.St;
				}
			}
			class Bar {
				state St;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, rawFile.indexOfNth("CS", 1), 2);
		parsedFile.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, rawFile.indexOfNth("CS", 2), 2);
		parsedFile.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, rawFile.indexOfNth("CS", 3), 2);
		parsedFile.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, rawFile.indexOf("CS.St"), 5);
		parsedFile.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, rawFile.indexOf("Bar.St"), 6);
	}

}
