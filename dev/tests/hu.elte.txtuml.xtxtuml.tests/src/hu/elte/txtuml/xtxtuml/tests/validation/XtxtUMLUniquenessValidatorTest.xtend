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
	@Inject extension XtxtUMLValidationTestUtils;

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
			enum  __4416cWAUcf83 {}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val rawFile = '''
			package java.lang;
			execution Object {}
			signal Double;
			class Float;
			association Exception {}
			interface Error {}
			connector String {}
			enum Integer {}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, rawFile.indexOf("Object"), 6);
		parsedFile.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, rawFile.indexOf("Double"), 6);
		parsedFile.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, rawFile.indexOf("Float"), 5);
		parsedFile.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, rawFile.indexOf("Exception"), 9);
		parsedFile.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, rawFile.indexOf("Error"), 5);
		parsedFile.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, rawFile.indexOf("String"), 6);
		parsedFile.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, rawFile.indexOf("Integer"), 7);
	}

	@Test
	def checkModelElementNameIsUniqueInternal() {
		'''
			class Foo;
			class Bar;
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val sameTypeRaw = '''
			class Foo;
			class Foo;
			class Bar;
			class bar;
		''';

		val sameTypeParsed = sameTypeRaw.parse;
		sameTypeParsed.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, sameTypeRaw.indexOfNth("Foo", 0), 3);
		sameTypeParsed.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, sameTypeRaw.indexOfNth("Foo", 1), 3);
		sameTypeParsed.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, sameTypeRaw.indexOf("Bar"), 3);
		sameTypeParsed.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, sameTypeRaw.indexOf("bar"), 3);

		val differentTypesRaw = '''
			class Foo;
			signal Foo;
			class Bar;
			signal bar;
		''';

		val differentTypesParsed = differentTypesRaw.parse;
		differentTypesParsed.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, differentTypesRaw.indexOfNth("Foo", 0), 3);
		differentTypesParsed.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, differentTypesRaw.indexOfNth("Foo", 1), 3);
		differentTypesParsed.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, differentTypesRaw.indexOf("Bar"), 3);
		differentTypesParsed.assertError(TU_MODEL_ELEMENT, NOT_UNIQUE_NAME, differentTypesRaw.indexOf("bar"), 3);
	}
	
	@Test
	def checkExecutionAttributeNameIsUnique() {
		'''
			execution Foo {
				int bar;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val rawFile = '''
			execution Foo {
				int bar;
				int bar;
				double bar;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_EXECUTION_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 0), 3);
		parsedFile.assertError(TU_EXECUTION_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 1), 3);
		parsedFile.assertError(TU_EXECUTION_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 2), 3);
	}

	@Test
	def checkExecutionBlockNameIsUnique() {
		'''
			execution Foo {
				initialization {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val rawFile = '''
			execution Foo {
				initialization {}
				initialization {}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_EXECUTION_BLOCK, NOT_UNIQUE_NAME, rawFile.indexOfNth("initialization", 0), 14);
		parsedFile.assertError(TU_EXECUTION_BLOCK, NOT_UNIQUE_NAME, rawFile.indexOfNth("initialization", 1), 14);
	}

	@Test
	def checkEnumLiteralIsUnique() {
		'''
			enum Foo {
				A, B, C
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val rawFile = '''
			enum Foo {
				BAR,
				BAR
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_ENUMERATION_LITERAL, NOT_UNIQUE_NAME, rawFile.indexOfNth("BAR", 1), 3);
		parsedFile.assertError(TU_ENUMERATION_LITERAL, NOT_UNIQUE_NAME, rawFile.indexOfNth("BAR", 2), 3);
	}

	@Test
	def checkReceptionIsUnique() {
		'''
			signal Foo;
			interface Bar {
				reception Foo;
			}
		'''.parse.assertNoError(NOT_UNIQUE_RECEPTION);

		val rawFile = '''
			signal Foo;
			interface Bar {
				reception Foo;
				reception Foo;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_RECEPTION, NOT_UNIQUE_RECEPTION, rawFile.indexOfNth("Foo", 1), 3);
		parsedFile.assertError(TU_RECEPTION, NOT_UNIQUE_RECEPTION, rawFile.indexOfNth("Foo", 2), 3);
	}

	@Test
	def checkSignalAttributeIsUnique() {
		'''
			signal Foo {
				int bar;
			}
			signal Bar extends Foo {
				int baz;
			}
		'''.parse.assertNoError(NOT_UNIQUE_SIGNAL_ATTRIBUTE);

		val sameLevelDuplicateRaw = '''
			signal Foo {
				int bar;
				int bar;
				double bar;
			}
		''';

		val sameLevelDuplicateParsed = sameLevelDuplicateRaw.parse;
		sameLevelDuplicateParsed.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_SIGNAL_ATTRIBUTE,
			sameLevelDuplicateRaw.indexOfNth("bar", 0), 3);
		sameLevelDuplicateParsed.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_SIGNAL_ATTRIBUTE,
			sameLevelDuplicateRaw.indexOfNth("bar", 1), 3);
		sameLevelDuplicateParsed.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_SIGNAL_ATTRIBUTE,
			sameLevelDuplicateRaw.indexOfNth("bar", 2), 3);

		val shadowingRaw = '''
			signal S1 {
				int i1;
				double d1;
			}
			signal S2 extends S1 {
				String i1;
				int i2;
			}
			signal S3 extends S2 {
				int i1;
				double d1;
			}
		''';

		val shadowingParsed = shadowingRaw.parse;
		shadowingParsed.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_SIGNAL_ATTRIBUTE,
			shadowingRaw.indexOfNth("i1", 1), 2);
		shadowingParsed.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_SIGNAL_ATTRIBUTE,
			shadowingRaw.indexOfNth("i1", 2), 2);
		shadowingParsed.assertError(TU_SIGNAL_ATTRIBUTE, NOT_UNIQUE_SIGNAL_ATTRIBUTE,
			shadowingRaw.indexOfNth("d1", 1), 2);
	}

	@Test
	def checkAttributeNameIsUnique() {
		'''
			class Foo {
				int bar;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val rawFile = '''
			class Foo {
				int bar;
				int bar;
				double bar;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 0), 3);
		parsedFile.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 1), 3);
		parsedFile.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 2), 3);
	}

	@Test
	def checkConstructorIsUnique() {
		'''
			class Foo {
				public Foo() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_CONSTRUCTOR);

		'''
			class Foo {
				public Foo() {}
				public Foo(int bar) {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_CONSTRUCTOR);

		val rawFile = '''
			class Foo {
				public Foo() {}
				Foo() {}
				public Foo(int bar) {}
				Foo(int bar) {}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, rawFile.indexOfNth("Foo", 1), 3);
		parsedFile.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, rawFile.indexOfNth("Foo", 2), 3);
		parsedFile.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, rawFile.indexOfNth("Foo", 3), 3);
		parsedFile.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, rawFile.indexOfNth("Foo", 4), 3);
	}

	@Test
	def checkOperationIsUnique() {
		'''
			class Foo {
				void bar() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		'''
			class Foo {
				void bar() {}
				void baz() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		'''
			class Foo {
				void bar() {}
				void bar(int baz) {}
				void bar(double baz) {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		val rawFile = '''
			class Foo {
				void bar() {}
				int bar() { return 0; }
				void bar(int baz) {}
				void bar(int foo) {}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, rawFile.indexOfNth("bar", 0), 3);
		parsedFile.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, rawFile.indexOfNth("bar", 1), 3);
		parsedFile.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, rawFile.indexOfNth("bar", 2), 3);
		parsedFile.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, rawFile.indexOfNth("bar", 3), 3);
	}
	
	@Test
	def checkDataTypeAttributeNameIsUnique() {
		'''
			datatype Foo {
				int bar;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val rawFile = '''
			datatype Foo {
				int bar;
				int bar;
				double bar;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 0), 3);
		parsedFile.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 1), 3);
		parsedFile.assertError(TU_ATTRIBUTE, NOT_UNIQUE_NAME, rawFile.indexOfNth("bar", 2), 3);
	}
	
	@Test
	def checkDataTypeConstructorIsUnique() {
		'''
			datatype Foo {
				public Foo() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_CONSTRUCTOR);

		'''
			datatype Foo {
				public Foo() {}
				public Foo(int bar) {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_CONSTRUCTOR);

		val rawFile = '''
			datatype Foo {
				public Foo() {}
				Foo() {}
				public Foo(int bar) {}
				Foo(int bar) {}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, rawFile.indexOfNth("Foo", 1), 3);
		parsedFile.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, rawFile.indexOfNth("Foo", 2), 3);
		parsedFile.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, rawFile.indexOfNth("Foo", 3), 3);
		parsedFile.assertError(TU_CONSTRUCTOR, NOT_UNIQUE_CONSTRUCTOR, rawFile.indexOfNth("Foo", 4), 3);
	}

	@Test
	def checkDataTypeOperationIsUnique() {
		'''
			datatype Foo {
				void bar() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		'''
			datatype Foo {
				void bar() {}
				void baz() {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		'''
			datatype Foo {
				void bar() {}
				void bar(int baz) {}
				void bar(double baz) {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_OPERATION);

		val rawFile = '''
			datatype Foo {
				void bar() {}
				int bar() { return 0; }
				void bar(int baz) {}
				void bar(int foo) {}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, rawFile.indexOfNth("bar", 0), 3);
		parsedFile.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, rawFile.indexOfNth("bar", 1), 3);
		parsedFile.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, rawFile.indexOfNth("bar", 2), 3);
		parsedFile.assertError(TU_OPERATION, NOT_UNIQUE_OPERATION, rawFile.indexOfNth("bar", 3), 3);
	}

	@Test
	def checkInitialStateIsUnique() {
		'''
			class Foo {
				initial Init;
			}
		'''.parse.assertNoError(NOT_UNIQUE_INITIAL_STATE);

		'''
			class Foo {
				initial Init;
				composite CS {
					initial Init;
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_INITIAL_STATE);

		val rawFile = '''
			class Foo {
				initial Init1;
				initial Init2;
				composite CS {
					initial Init3;
					initial Init4;
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_STATE, NOT_UNIQUE_INITIAL_STATE, rawFile.indexOf("Init1"), 5);
		parsedFile.assertError(TU_STATE, NOT_UNIQUE_INITIAL_STATE, rawFile.indexOf("Init2"), 5);
		parsedFile.assertError(TU_STATE, NOT_UNIQUE_INITIAL_STATE, rawFile.indexOf("Init3"), 5);
		parsedFile.assertError(TU_STATE, NOT_UNIQUE_INITIAL_STATE, rawFile.indexOf("Init4"), 5);
	}

	@Test
	def checkNestedClassMemberNameIsUnique() {
		'''
			class Foo {
				state Bar;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		'''
			class Foo {
				transition Bar {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		'''
			class Foo {
				port Bar {}
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val statesRaw = '''
			class Foo {
				state Bar;
				state Bar;
				state bar;
			}
		''';

		val statesParsed = statesRaw.parse;
		statesParsed.assertError(TU_STATE, NOT_UNIQUE_NAME, statesRaw.indexOfNth("Bar", 0), 3);
		statesParsed.assertError(TU_STATE, NOT_UNIQUE_NAME, statesRaw.indexOfNth("Bar", 1), 3);
		statesParsed.assertError(TU_STATE, NOT_UNIQUE_NAME, statesRaw.indexOf("bar"), 3);

		val transitionsRaw = '''
			class Foo {
				transition Bar {}
				transition Bar {}
				transition bar {}
			}
		''';

		val transitionsParsed = transitionsRaw.parse;
		transitionsParsed.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, transitionsRaw.indexOfNth("Bar", 0), 3);
		transitionsParsed.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, transitionsRaw.indexOfNth("Bar", 1), 3);
		transitionsParsed.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, transitionsRaw.indexOf("bar"), 3);

		val portsRaw = '''
			class Foo {
				port Bar {}
				port Bar {}
				port bar {}
			}
		''';

		val portsParsed = portsRaw.parse;
		portsParsed.assertError(TU_PORT, NOT_UNIQUE_NAME, portsRaw.indexOfNth("Bar", 0), 3);
		portsParsed.assertError(TU_PORT, NOT_UNIQUE_NAME, portsRaw.indexOfNth("Bar", 1), 3);
		portsParsed.assertError(TU_PORT, NOT_UNIQUE_NAME, portsRaw.indexOf("bar"), 3);

		val allRaw = '''
			class Foo {
				state Bar;
				transition Bar {}
				port Bar {}
				state bar;
				transition bar {}
				port bar {}
			}
		''';

		val allParsed = allRaw.parse;
		allParsed.assertError(TU_STATE, NOT_UNIQUE_NAME, allRaw.indexOfNth("Bar", 0), 3);
		allParsed.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, allRaw.indexOfNth("Bar", 1), 3);
		allParsed.assertError(TU_PORT, NOT_UNIQUE_NAME, allRaw.indexOfNth("Bar", 2), 3);
		allParsed.assertError(TU_STATE, NOT_UNIQUE_NAME, allRaw.indexOfNth("bar", 0), 3);
		allParsed.assertError(TU_TRANSITION, NOT_UNIQUE_NAME, allRaw.indexOfNth("bar", 1), 3);
		allParsed.assertError(TU_PORT, NOT_UNIQUE_NAME, allRaw.indexOfNth("bar", 2), 3);
	}

	@Test
	def checkStateActivityIsUnique() {
		'''
			class Foo {
				state Bar {
					entry {}
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_STATE_ACTIVITY);

		'''
			class Foo {
				state Bar {
					entry {}
					exit {}
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_STATE_ACTIVITY);

		val rawFile = '''
			class Foo {
				state Bar {
					entry {}
					entry {}
					exit {}
					exit {}
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, NOT_UNIQUE_STATE_ACTIVITY, rawFile.indexOfNth("entry", 0), 5);
		parsedFile.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, NOT_UNIQUE_STATE_ACTIVITY, rawFile.indexOfNth("entry", 1), 5);
		parsedFile.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, NOT_UNIQUE_STATE_ACTIVITY, rawFile.indexOfNth("exit", 0), 4);
		parsedFile.assertError(TU_ENTRY_OR_EXIT_ACTIVITY, NOT_UNIQUE_STATE_ACTIVITY, rawFile.indexOfNth("exit", 1), 4);
	}

	@Test
	def checkInitialTransitionIsUnique() {
		'''
			class Foo {
				initial Init;
				state Bar;
				transition {
					from Init;
					to Bar;
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_INITIAL_TRANSITION);

		'''
			class Foo {
				initial Init;
				transition T1 {
					from Init;
					to CS;
				}
				composite CS {
					initial Init;
					state Bar;
					transition T2 {
						from Init;
						to Bar;
					}
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_INITIAL_TRANSITION);

		val flatRaw = '''
			class Foo {
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
		''';

		val flatParsed = flatRaw.parse;
		flatParsed.assertError(TU_TRANSITION, NOT_UNIQUE_INITIAL_TRANSITION, flatRaw.indexOf("T1"), 2);
		flatParsed.assertError(TU_TRANSITION, NOT_UNIQUE_INITIAL_TRANSITION, flatRaw.indexOf("T2"), 2);

		val hierarchicalRaw = '''
			class Foo {
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
		''';

		val hierarchicalParsed = hierarchicalRaw.parse;
		hierarchicalParsed.assertError(TU_TRANSITION, NOT_UNIQUE_INITIAL_TRANSITION, hierarchicalRaw.indexOf("T1"), 2);
		hierarchicalParsed.assertError(TU_TRANSITION, NOT_UNIQUE_INITIAL_TRANSITION, hierarchicalRaw.indexOf("T2"), 2);
	}

	@Test
	def checkTransitionMemberIsUnique() {
		'''
			signal Sig;
			class Foo {
				port Po {}
				state St;
				transition T {
					from St;
					to St;
					trigger Sig;
					port Po;
					guard ( true );
					effect {}
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_TRANSITION_MEMBER);

		val rawFile = '''
			signal Sig;
			class Foo {
				port Po {}
				state St;
				transition Tr {
					from St; from St;
					to St; to St;
					trigger Sig; trigger Sig;
					port Po; port Po;
					guard ( true ); guard ( true );
					effect {} effect {}
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("from", 0), 4);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("from", 1), 4);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("to", 0), 2);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("to", 1), 2);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("trigger", 0), 7);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("trigger", 1), 7);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("port", 1), 4);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("port", 2), 4);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("guard", 0), 5);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("guard", 1), 5);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("effect", 0), 6);
		parsedFile.assertError(TU_TRANSITION_MEMBER, NOT_UNIQUE_TRANSITION_MEMBER, rawFile.indexOfNth("effect", 1), 6);
	}

	@Test
	def checkPortMemberIsUnique() {
		'''
			interface If {}
			class Foo {
				port Po {
					provided If;
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_PORT_MEMBER);

		'''
			interface If {}
			class Foo {
				port Po {
					provided If;
					required If;
				}
			}
		'''.parse.assertNoError(NOT_UNIQUE_PORT_MEMBER);

		val rawFile = '''
			interface I1 {}
			interface I2 {}
			class Foo {
				port Po {
					provided I1;
					provided I1;
					required I1;
					required I2;
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_PORT_MEMBER, NOT_UNIQUE_PORT_MEMBER, rawFile.indexOfNth("provided", 0), 8);
		parsedFile.assertError(TU_PORT_MEMBER, NOT_UNIQUE_PORT_MEMBER, rawFile.indexOfNth("provided", 1), 8);
		parsedFile.assertError(TU_PORT_MEMBER, NOT_UNIQUE_PORT_MEMBER, rawFile.indexOfNth("required", 0), 8);
		parsedFile.assertError(TU_PORT_MEMBER, NOT_UNIQUE_PORT_MEMBER, rawFile.indexOfNth("required", 1), 8);
	}

	@Test
	def checkAssociationEndNamesAreUnique() {
		'''
			class Foo;
			class Bar;
			association FooBar {
				Foo foo;
				Bar bar;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		'''
			class Foo;
			association FooFoo {
				Foo e1;
				Foo e2;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val rawFile = '''
			class Foo;
			class Bar;
			association FooBar {
				Foo end;
				Bar end;
			}
			association FooFoo {
				Foo end;
				Foo End;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_ASSOCIATION_END, NOT_UNIQUE_NAME, rawFile.indexOfNth("end", 0), 3);
		parsedFile.assertError(TU_ASSOCIATION_END, NOT_UNIQUE_NAME, rawFile.indexOfNth("end", 1), 3);
		parsedFile.assertError(TU_ASSOCIATION_END, NOT_UNIQUE_NAME, rawFile.indexOfNth("end", 2), 3);
		parsedFile.assertError(TU_ASSOCIATION_END, NOT_UNIQUE_NAME, rawFile.indexOf("End"), 3);
	}

	@Test
	def checkConnectorEndNamesAreUnique() {
		'''
			class Foo { port Po {} }
			class Bar { port Po {} }
			composition CFooBar {
				container Foo foo;
				Bar bar;
			}
			delegation DFooBar {
				CFooBar.foo->Foo.Po foo;
				CFooBar.bar->Bar.Po bar;
			}
		'''.parse.assertNoError(NOT_UNIQUE_NAME);

		val rawFile = '''
			class Foo { port Po {} }
			class Bar { port Po {} }
			composition CFooBar {
				container Foo foo;
				Bar bar;
			}
			delegation DFooBar1 {
				CFooBar.foo->Foo.Po foo;
				CFooBar.bar->Bar.Po foo;
			}
			delegation DFooBar2 {
				CFooBar.foo->Foo.Po a1;
				CFooBar.bar->Bar.Po A1;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_CONNECTOR_END, NOT_UNIQUE_NAME, rawFile.indexOfNth("foo", 2), 3);
		parsedFile.assertError(TU_CONNECTOR_END, NOT_UNIQUE_NAME, rawFile.indexOfNth("foo", 3), 3);
		parsedFile.assertError(TU_CONNECTOR_END, NOT_UNIQUE_NAME, rawFile.indexOf("a1"), 2);
		parsedFile.assertError(TU_CONNECTOR_END, NOT_UNIQUE_NAME, rawFile.indexOf("A1"), 2);
	}

}
