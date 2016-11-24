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

	@Test
	def checkNoCycleInClassHiearchy() {
		'''
			class A;
			class B extends A;
			class C extends B;
			class D extends B;
			class E extends C;
		'''.parse.assertNoError(CLASS_HIERARCHY_CYCLE);

		val file = '''
			class A extends A;
			class B extends C;
			class C extends D;
			class D extends B;
			class E extends D;
			class F extends E;
		'''.parse;

		file.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, 16, 1);
		file.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, 36, 1);
		file.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, 56, 1);
		file.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, 76, 1);
		file.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, 96, 1);
		file.assertError(TU_CLASS, CLASS_HIERARCHY_CYCLE, 116, 1);
	}

	@Test
	def checkConstructorName() {
		'''
			class A {
				A() {}
			}
		'''.parse.assertNoError(INVALID_CONSTRUCTOR_NAME);

		'''
			class A {
				a() {}
			}
		'''.parse.assertError(TU_CONSTRUCTOR, INVALID_CONSTRUCTOR_NAME, 12, 1);
	}

	@Test
	def checkInitialStateIsDefined() {
		val noWarningFile = '''
			class A;
			class B {
				initial Init;
				composite CS;
			}
		'''.parse;

		noWarningFile.assertNoWarnings(TU_CLASS, MISSING_INITIAL_STATE);
		noWarningFile.assertNoWarnings(TU_STATE, MISSING_INITIAL_STATE);

		val file = '''
			class A {
				composite CS {
					state S;
				}
			}
		'''.parse;

		file.assertWarning(TU_CLASS, MISSING_INITIAL_STATE, 6, 1);
		file.assertWarning(TU_STATE, MISSING_INITIAL_STATE, 22, 2);
	}

	@Test
	def checkPseudostateIsLeavable() {
		'''
			class A {
				initial Init;
				choice C;
				state S;
				transition T1 {
					from Init;
					to C;
				}
				transition T2 {
					from C;
					to S;
				}
			}
		'''.parse.assertNoError(NOT_LEAVABLE_PSEUDOSTATE);

		val file = '''
			class A {
				initial Init;
				choice C;
				state S;
				transition T {
					from S;
					to C;
				}
			}
		'''.parse;

		file.assertError(TU_STATE, NOT_LEAVABLE_PSEUDOSTATE, 20, 4);
		file.assertError(TU_STATE, NOT_LEAVABLE_PSEUDOSTATE, 35, 1);
	}

	@Test
	def checkStateIsReachable() {
		'''
			class A {
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

		val file = '''
			class A {
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
		'''.parse;

		file.assertWarning(TU_STATE, UNREACHABLE_STATE, 58, 2);
		file.assertWarning(TU_STATE, UNREACHABLE_STATE, 74, 2);
		file.assertWarning(TU_STATE, UNREACHABLE_STATE, 105, 2);
	}

	@Test
	def checkStateOrTransitionIsDefinedInClassOrCompositeState() {
		'''
			class A {
				composite CS {
					state S;
					transition T2 {}
				}
				transition T1 {}
			}
		'''.parse.assertNoError(STATE_OR_TRANSITION_IN_NOT_COMPOSITE_STATE);

		val file = '''
			class A {
				state PS {
					state S;
					transition T2 {}
				}
			}
		'''.parse;

		file.assertError(TU_STATE, STATE_OR_TRANSITION_IN_NOT_COMPOSITE_STATE, 32, 1);
		file.assertError(TU_TRANSITION, STATE_OR_TRANSITION_IN_NOT_COMPOSITE_STATE, 49, 2);
	}

	@Test
	def checkNoActivityInPseudostate() {
		'''
			class A {
				state S {
					entry {}
					exit {}
				}
				composite CS {
					entry {}
					exit {}
				}
			}
		'''.parse.assertNoError(ACTIVITY_IN_PSEUDOSTATE);

		val file = '''
			class A {
				initial Init {
					entry {}
					exit {}
				}
				choice C {
					entry {}
					exit {}
				}
			}
		'''.parse;

		file.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, ACTIVITY_IN_PSEUDOSTATE, 30, 5);
		file.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, ACTIVITY_IN_PSEUDOSTATE, 42, 4);
		file.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, ACTIVITY_IN_PSEUDOSTATE, 70, 5);
		file.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, ACTIVITY_IN_PSEUDOSTATE, 82, 4);
	}

	@Test
	def checkMandatoryTransitionMembers() {
		'''
			signal Sig;
			class A {
				initial Init;
				state St;
				choice C;
				transition T1 {
					from Init;
					to St;
				}
				transition T2 {
					from C;
					to St;
				}
				transition T3 {
					from St;
					to C;
					trigger Sig;
				}
			}
		'''.parse.assertNoError(MISSING_MANDATORY_TRANSITION_MEMBER);

		val file = '''
			signal Sig;
			class A {
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
		'''.parse;

		file.assertError(TU_TRANSITION, MISSING_MANDATORY_TRANSITION_MEMBER, 48, 2);
		file.assertError(TU_TRANSITION, MISSING_MANDATORY_TRANSITION_MEMBER, 96, 2);
		file.assertError(TU_TRANSITION, MISSING_MANDATORY_TRANSITION_MEMBER, 146, 2);
		file.assertError(TU_TRANSITION, MISSING_MANDATORY_TRANSITION_MEMBER, 190, 2);
	}

	@Test
	def checkTransitionTargetIsNotInitialState() {
		'''
			class A {
				state S;
				transition T {
					to S;
				}
			}
		'''.parse.assertNoError(TARGET_IS_INITIAL_STATE);

		'''
			class A {
				initial Init;
				transition T {
					to Init;
				}
			}
		'''.parse.assertError(TU_TRANSITION_VERTEX, TARGET_IS_INITIAL_STATE, 49, 4);
	}

	@Test
	def checkGuardIsNotForInitialTransition() {
		'''
			class A {
				state S;
				transition T {
					from S;
					guard ( true );
				}
			}
		'''.parse.assertNoError(INVALID_TRANSITION_MEMBER);

		'''
			class A {
				initial Init;
				transition T {
					from Init;
					guard ( true );
				}
			}
		'''.parse.assertError(TU_TRANSITION_GUARD, INVALID_TRANSITION_MEMBER, 60, 5);
	}

	@Test
	def checkMemberOfTransitionFromPseudostate() {
		'''
			signal Sig;
			class A {
				behavior port P {}
				state St;
				composite CS;
				transition T1 {
					from St;
					trigger Sig;
					port P;
				}
				transition T2 {
					from CS;
					trigger Sig;
					port P;
				}
			}
		'''.parse.assertNoError(INVALID_TRANSITION_MEMBER);

		val file = '''
			signal Sig;
			class A {
				behavior port P {}
				initial Init;
				choice C;
				transition T1 {
					from Init;
					trigger Sig;
					port P;
				}
				transition T2 {
					from C;
					trigger Sig;
					port P;
				}
			}
		'''.parse;

		file.assertError(TU_TRANSITION_TRIGGER, INVALID_TRANSITION_MEMBER, 107, 7)
		file.assertError(TU_TRANSITION_PORT, INVALID_TRANSITION_MEMBER, 123, 4)
		file.assertError(TU_TRANSITION_TRIGGER, INVALID_TRANSITION_MEMBER, 167, 7)
		file.assertError(TU_TRANSITION_PORT, INVALID_TRANSITION_MEMBER, 183, 4);
	}

	@Test
	def checkElseGuard() {
		'''
			class A {
				choice C;
				transition T1 {
					from C;
					guard ( false );
				}
				transition T2 {
					from C;
					guard ( else );
				}
			}
		'''.parse.assertNoError(INVALID_ELSE_GUARD);

		val file = '''
			class A {
				state S;
				composite CS;
				transition T1 {
					from S;
					guard ( else );
				}
				transition T2 {
					from CS;
					guard ( else );
				}
			}
		'''.parse;

		file.assertError(TU_TRANSITION_GUARD, INVALID_ELSE_GUARD, 77, 4);
		file.assertError(TU_TRANSITION_GUARD, INVALID_ELSE_GUARD, 130, 4);
	}

	@Test
	def checkOwnerOfTriggerPort() {
		'''
			class A {
				behavior port P {}
				transition T {
					port P;
				}
				composite CS {
					transition T {
						port P;
					}
				}
			}
			class B extends A {
				transition T {
					port A.P;
				}
			}
			class C extends B {
				composite CS {
					transition T {
						port A.P;
					}
				}
			}
			class D {
				behavior port P {}
			}
			class E extends D {
				transition T {
					port D.P;
				}
			}
		'''.parse.assertNoError(NOT_OWNED_TRIGGER_PORT);

		val file = '''
			class A {
				behavior port P {}
				transition T1 {
					port B.P;
				}
				transition T2 {
					port C.P;
				}
			}
			class B {
				behavior port P {}
				transition T {
					port A.P;
				}
			}
			class C extends A {
				behavior port P {}
				transition T {
					port B.P;
				}
			}
		'''.parse;

		file.assertError(TU_TRANSITION_PORT, NOT_OWNED_TRIGGER_PORT, 57, 3);
		file.assertError(TU_TRANSITION_PORT, NOT_OWNED_TRIGGER_PORT, 92, 3);
		file.assertError(TU_TRANSITION_PORT, NOT_OWNED_TRIGGER_PORT, 161, 3);
		file.assertError(TU_TRANSITION_PORT, NOT_OWNED_TRIGGER_PORT, 240, 3);
	}

	@Test
	def checkTriggerPortIsBehavior() {
		'''
			class A {
				behavior port P {}
				transition T {
					port P;
				}
			}
		'''.parse.assertNoError(NOT_BEHAVIOR_TRIGGER_PORT);

		'''
			class A {
				port P {}
				transition T {
					port P;
				}
			}
		'''.parse.assertError(TU_TRANSITION_PORT, NOT_BEHAVIOR_TRIGGER_PORT, 47, 1);
	}

	@Test
	def checkTransitionVertexLevel() {
		'''
			class A {
				composite CS {
					state S;
					transition T2 {
						from S;
						to S;
					}
				}
				transition T1 {
					from CS;
					to CS;
				}
			}
		'''.parse.assertNoError(VERTEX_LEVEL_MISMATCH);

		val file = '''
			class A {
				composite CS {
					state S;
					transition T2 {
						from CS;
						to CS;
					}
					transition T3 {
						from S;
						to CS;
					}
				}
				transition T1 {
					from CS.S;
					to CS;
				}
				transition T4 {
					from CS;
					to B.S;
				}
			}
			class B {
				state S;
			}
		'''.parse;

		file.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, 67, 2);
		file.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, 78, 2);
		file.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, 125, 2);
		file.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, 164, 4);
		file.assertError(TU_TRANSITION_VERTEX, VERTEX_LEVEL_MISMATCH, 220, 3);
	}

}
