package hu.elte.txtuml.xtxtuml.tests.parser;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

import static hu.elte.txtuml.xtxtuml.tests.parser.XtxtUMLParserTestUtils.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLExpressionParserTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension XtxtUMLParserTestUtils;

	@Test
	def parseSendSignalExpression() {
		'''
			package test.model;
			signal TestSignal;
			class TestClass {
				void testOperation() {
					send new TestSignal() to this;
					send new TestSignal() to new TestClass();

					TestSignal sig = new TestSignal();
					send sig to this->(TestPort);
				}

				port TestPort {}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[signal("TestSignal", null, #[])],
				[class_(
					"TestClass", null, #[
						[operation(
							PACKAGE, "void", "testOperation", #[], #[
								[sendSignal(
									[constructorCall("TestSignal", null, #[])],
									[this_]
								)],
								[sendSignal(
									[constructorCall("TestSignal", null, #[])],
									[constructorCall("TestClass", null, #[])]
								)],
								[variableDeclaration(
									"TestSignal", null, "sig",
									[constructorCall("TestSignal", null, #[])]
								)],
								[sendSignal(
									[variable("sig")],
									[propertyAccess([this_], "TestPort")]
								)]
							]
						)],
						[port(false, "TestPort", #[])]
					]
				)]
			]
		)
	}

	@Test
	def parseSignalAccessExpression() {
		'''
			package test.model;
			signal TestSignal {
				public String message;
			}
			class TestClass {
				state TestState;
				transition TestTransition {
					from TestState;
					to TestState;
					trigger TestSignal;
					effect {
						log(trigger.message);
					}
				}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[signal(
					"TestSignal", null, #[
						[attribute(PUBLIC, "String", "message")]
					]
				)],
				[class_(
					"TestClass", null, #[
						[state(PLAIN, "TestState", #[])],
						[transition(
							"TestTransition", #[
								[vertex(true, "TestState")],
								[vertex(false, "TestState")],
								[trigger("TestSignal")],
								[effect(#[
									[featureCall(
										null, "log", #[
											[featureCall([signalAccess], "message", null)]
										]
									)]
								])]
							]
						)]
					]
				)]
			]
		)
	}

	@Test
	def parseDeleteObjectExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					A a = new A();
					delete a;
					delete new A();
				}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"A", null, #[
						[operation(
							PACKAGE, "void", "foo", #[], #[
								[variableDeclaration(
									"A", null, "a",
									[constructorCall("A", null, #[])]
								)],
								[delete[variable("a")]],
								[delete[constructorCall("A", null, #[])]]
							]
						)]
					]
				)]
			]
		)
	}

	@Test
	def parseVariableDeclaration() {
		'''
			package test.model;

			import hu.elte.txtuml.api.model.Collection;
			import hu.elte.txtuml.api.model.runtime.collections.Sequence;

			class TestClass {
				void testOperation() {
					int a;
					String b = "test";
					Collection<TestClass> c;
					Collection<TestClass> d = new Sequence<TestClass>();
				}
			}
		'''
		.parse.
		file(
			"test.model", #[
				[importDeclaration(false, "hu.elte.txtuml.api.model.Collection", false)],
				[importDeclaration(false, "hu.elte.txtuml.api.model.runtime.collections.Sequence", false)]
			], #[
				[class_(
					"TestClass", null, #[
						[operation(
							PACKAGE, "void", "testOperation", #[], #[
								[variableDeclaration("int", null, "a", null)],
								[variableDeclaration("String", null, "b", [string("test")])],
								[variableDeclaration("Collection", "TestClass", "c", null)],
								[variableDeclaration(
									"Collection", "TestClass", "d",
									[constructorCall("Sequence", "TestClass", #[])]
								)]
							]
						)]
					]
				)]
			]
		)
	}

	@Test
	def parseClassPropertyAccessExpression() {
		'''
			package test.model;

			class A {
				void foo() {
					send new S() to this->(P);
					send new S() to this->(AB.b).selectAny();
					new B()->(AB.a).selectAny();
				}

				port P {}
			}

			signal S;
			class B;
			association AB {
				A a;
				B b;
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"A", null, #[
						[operation(
							PACKAGE, "void", "foo", #[], #[
								[sendSignal(
									[constructorCall("S", null, #[])],
									[propertyAccess([this_], "P")]
								)],
								[sendSignal(
									[constructorCall("S", null, #[])],
									[featureCall(
										[propertyAccess([this_], "b")],
										"selectAny", #[]
									)]
								)],
								[featureCall(
									[propertyAccess(
										[constructorCall("B", null, #[])], "a"
									)], "selectAny", #[]
								)]
							]
						)],
						[port(false, "P", #[])]
					]
				)],
				[signal("S", null, #[])],
				[class_("B", null, #[])],
				[association(
					false, "AB", #[
						[associationEnd(PACKAGE, false, null, false, "A", "a")],
						[associationEnd(PACKAGE, false, null, false, "B", "b")]
					]
				)]
			]
		)
	}

	@Test
	def parseWhileExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					int i = 0;
					while (i < 10) {
						i++;
					}
				}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"A", null, #[
						[operation(
							PACKAGE, "void", "foo", #[], #[
								[variableDeclaration("int", null, "i", [number(0)])],
								[whileLoop(
									[binaryOperation(OPERATOR_LESS_THAN, [variable("i")], [number(10)])], #[
										[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
									]
								)]
							]
						)]
					]
				)]
			]
		)
	}

	@Test
	def parseDoWhileExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					int i = 0;
					do {
						i++;
					} while (i < 10);
				}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"A", null, #[
						[operation(
							PACKAGE, "void", "foo", #[], #[
								[variableDeclaration("int", null, "i", [number(0)])],
								[doWhileLoop(
									#[
										[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
									], [binaryOperation(OPERATOR_LESS_THAN, [variable("i")], [number(10)])]
								)]
							]
						)]
					]
				)]
			]
		)
	}

	@Test
	def parseIfExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					int i;
					if (true) {
						i++;
					}
					if (false) {
						i--;
					} else {
						i++;
					}
					if (false) {
						i--;
					} else if (true) {
						i++;
					}
				}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"A", null, #[
						[operation(
							PACKAGE, "void", "foo", #[], #[
								[variableDeclaration("int", null, "i", null)],
								[if_(
									[bool(true)], #[
										[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
									], null, null
								)],
								[if_(
									[bool(false)], #[
										[postfixOperation(OPERATOR_MINUS_MINUS, [variable("i")])]
									], null, #[
										[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
									]
								)],
								[if_(
									[bool(false)], #[
										[postfixOperation(OPERATOR_MINUS_MINUS, [variable("i")])]
									],
									[if_(
										[bool(true)], #[
											[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
										], null, null
									)], null
								)]
							]
						)]
					]
				)]
			]
		)
	}

	@Test
	def parseSwitchExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					int i;
					switch (i) {
						case 0 : {}
						default : {
							i++;
						}
					}
					switch (i) {
						case 0 : {}
						case 1,
						case 2 : {
							i--;
						}
					}
				}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"A", null, #[
						[operation(
							PACKAGE, "void", "foo", #[], #[
								[variableDeclaration("int", null, "i", null)],
								[switch_(
									[variable("i")], #[
										[case_([number(0)], #[])]
									], #[
										[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
									]
								)],
								[switch_(
									[variable("i")], #[
										[case_([number(0)], #[])],
										[case_([number(1)], null)],
										[case_([number(2)], #[
											[postfixOperation(OPERATOR_MINUS_MINUS, [variable("i")])]
										])]
									], null
								)]
							]
						)]
					]
				)]
			]
		)
	}

	@Test
	def parseForLoopExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					for (B b : this->(AB.b)) {
						b.bar();
					}
				}
			}
			class B {
				void bar() {}
			}
			association AB {
				A a;
				B b;
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"A", null, #[
						[operation(
							PACKAGE, "void", "foo", #[], #[
								[forEachLoop(
									[parameter("B", "b")], [propertyAccess([this_], "b")], #[
										[featureCall([variable("b")], "bar", #[])]
									]
								)]
							]
						)]
					]
				)],
				[class_(
					"B", null, #[
						[operation(PACKAGE, "void", "bar", #[], #[])]
					]
				)],
				[association(
					false, "AB", #[
						[associationEnd(PACKAGE, false, null, false, "A", "a")],
						[associationEnd(PACKAGE, false, null, false, "B", "b")]
					]
				)]
			]
		)
	}

	@Test
	def parseBasicForLoopExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					for (int i = 0; i < 10; i++) {
						foo();
					}
					for (int i = 0;;) {}
					for (;;) {}
					int i;
					for (i = 2;; i++) {}
				}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"A", null, #[
						[operation(
							PACKAGE, "void", "foo", #[], #[
								[forLoop(
									[variableDeclaration("int", null, "i", [number(0)])],
									[binaryOperation(OPERATOR_LESS_THAN, [variable("i")], [number(10)])],
									[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])], #[
										[featureCall(null, "foo", #[])]
									]
								)],
								[forLoop(
									[variableDeclaration("int", null, "i", [number(0)])], null, null, #[]
								)],
								[forLoop(
									null, null, null, #[]
								)],
								[variableDeclaration("int", null, "i", null)],
								[forLoop(
									[assignment("i", [number(2)])],
									null,
									[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])],
									#[]
								)]
							]
						)]
					]
				)]
			]
		)
	}

}
