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
import static hu.elte.txtuml.xtxtuml.xtxtUML.TUBindType.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.TUExternality.*
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
		file("test.model", null, #[
			[signal("TestSignal", null, #[])],
			[class_("TestClass", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "testOperation", #[], #[
					[sendSignal(
						[new_("TestSignal", null, #[], null)],
						[this_]
					)],
					[sendSignal(
						[new_("TestSignal", null, #[], null)],
						[new_("TestClass", null, #[], null)]
					)],
					[variableDeclaration(
						"TestSignal", null, "sig",
						[new_("TestSignal", null, #[], null)]
					)],
					[sendSignal(
						[variable("sig")],
						[propertyAccess([this_], "TestPort")]
					)]
				])],
				[port("TestPort", #[])]
			])]
		])
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
						log trigger.message;
					}
				}
			}
		'''
		.parse.
		file("test.model", null, #[
			[signal("TestSignal", null, #[
				[attribute(PUBLIC, "String", "message")]
			])],
			[class_("TestClass", null, #[
				[state(PLAIN, "TestState", #[])],
				[transition("TestTransition", #[
					[from("TestState")],
					[to("TestState")],
					[trigger("TestSignal")],
					[effect(#[
						[log([featureCall([signalAccess], "message", null)])]
					])]
				])]
			])]
		])
	}

	@Test
	def parseLogExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					String m = "message";
					log m;
					log "message";
					log-error m;
					log-error "message";
				}
			}
		'''
		.parse.
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[variableDeclaration("String", null, "m", [string("message")])],
					[log([variable("m")])],
					[log([string("message")])],
					[logError([variable("m")])],
					[logError([string("message")])]
				])]
			])]
		])
	}

	@Test
	def parseStartObjectExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					A a = new A();
					start a;
					start new A();
				}
			}
		'''
		.parse.
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[variableDeclaration(
						"A", null, "a",
						[new_("A", null, #[], null)]
					)],
					[start[variable("a")]],
					[start[new_("A", null, #[], null)]]
				])]
			])]
		])
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
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[variableDeclaration(
						"A", null, "a",
						[new_("A", null, #[], null)]
					)],
					[delete[variable("a")]],
					[delete[new_("A", null, #[], null)]]
				])]
			])]
		])
	}

	@Test
	def parseVariableDeclaration() {
		'''
			package test.model;

			import hu.elte.txtuml.api.model.GeneralCollection;

			class TestClass {
				void testOperation() {
					int i;
					String s = "test";
					GeneralCollection<TestClass> c;
					TestClass t = new TestClass();
				}
			}
		'''
		.parse.
		file("test.model", #[
			[importDeclaration(false, "hu.elte.txtuml.api.model.GeneralCollection", false)]
		], #[
			[class_("TestClass", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "testOperation", #[], #[
					[variableDeclaration("int", null, "i", null)],
					[variableDeclaration("String", null, "s", [string("test")])],
					[variableDeclaration("GeneralCollection", "TestClass", "c", null)],
					[variableDeclaration("TestClass", null, "t", [new_("TestClass", null, #[], null)])]
				])]
			])]
		])
	}

	@Test
	def parseCreateObjectExpression() {
		'''
			package test.model;
			class A {
				A() {}
				A(int i) {}
				void f() {
					new A();
					new A(0);
					create A();
					create A(0);
					new A() as "a";
					new A(0) as "a";
					create A() as "a";
					create A(0) as "a";
					A a;
					a = new A();
					a = new A(0);
					a = create A();
					a = create A(0);
					a = new A() as "a";
					a = new A(0) as "a";
					a = create A() as "a";
					a = create A(0) as "a";
				}
			}
		'''
		.parse.
		file("test.model", null, #[
			[class_("A", null, #[
				[constructor(PACKAGE, NON_EXTERNAL, "A", #[], #[])],
				[constructor(PACKAGE, NON_EXTERNAL, "A", #[
					[parameter("int", "i")]
				], #[])],
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "f", #[], #[
					[new_("A",null, #[], null)],
					[new_("A", null, #[[number(0)]], null)],
					[create_("A", null, #[], null)],
					[create_("A", null, #[[number(0)]], null)],
					[new_("A", null, #[], "a")],
					[new_("A", null, #[[number(0)]], "a")],
					[create_("A", null, #[], "a")],
					[create_("A", null, #[[number(0)]], "a")],
					[variableDeclaration("A", null,	"a", null)],
					[assignment("a", [new_("A", null, #[], null)])],
					[assignment("a", [new_("A", null, #[[number(0)]], null)])],
					[assignment("a", [create_("A", null, #[], null)])],
					[assignment("a", [create_("A", null, #[[number(0)]], null)])],
					[assignment("a", [new_("A", null, #[], "a")])],
					[assignment("a", [new_("A", null, #[[number(0)]], "a")])],
					[assignment("a", [create_("A", null, #[], "a")])],
					[assignment("a", [create_("A", null, #[[number(0)]], "a")])]
				])]
			])]
		])
	}

	@Test
	def parseBindExpression() {
		'''
			package test.model;
			class A {
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

					connect this->(P), b->(B.P) via CAB;
					connect this->(P) as CAB.e, b->(B.P) via CAB;
					connect this->(P), b->(B.P) as CAB.f via CAB;
					connect this->(P) as CAB.e, b->(B.P) as CAB.f via CAB;
				}
				port P {}
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
				CA.a->A.P e;
				CB.b->B.P f;
			}
		'''
		.parse.
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "f", #[], #[
					[variableDeclaration(
						"B", null, "b",
						[new_("B", null, #[], null)]
					)],
					[bind(LINK,
						[this_], null,
						[variable("b")], null,
						"AB"
					)],
					[bind(LINK,
						[this_], "a",
						[variable("b")], null,
						"AB"
					)],
					[bind(LINK,
						[this_], null,
						[variable("b")], "b",
						"AB"
					)],
					[bind(LINK,
						[this_], "a",
						[variable("b")], "b",
						"AB"
					)],
					[bind(UNLINK,
						[this_], null,
						[variable("b")], null,
						"AB"
					)],
					[bind(UNLINK,
						[this_], "a",
						[variable("b")], null,
						"AB"
					)],
					[bind(UNLINK,
						[this_], null,
						[variable("b")], "b",
						"AB"
					)],
					[bind(UNLINK,
						[this_], "a",
						[variable("b")], "b",
						"AB"
					)],
					[bind(CONNECT,
						[propertyAccess([this_], "P")], null,
						[propertyAccess([variable("b")], "P")], null,
						"CAB"
					)],
					[bind(CONNECT,
						[propertyAccess([this_], "P")], "e",
						[propertyAccess([variable("b")], "P")], null,
						"CAB"
					)],
					[bind(CONNECT,
						[propertyAccess([this_], "P")], null,
						[propertyAccess([variable("b")], "P")], "f",
						 "CAB"
					)],
					[bind(CONNECT,
						[propertyAccess([this_], "P")], "e",
						[propertyAccess([variable("b")], "P")], "f",
						"CAB"
					)]
				])],
				[port("P", #[])]
			])],
			[class_("B", null, #[
				[port("P", #[])]
			])],
			[class_("C", null, #[])],
			[association("AB", #[
				[associationEnd(PACKAGE, false, null, false, "A", "a", false, false)],
				[associationEnd(PACKAGE, false, null, false, "B", "b", false, false)]
			])],
			[composition("CA", #[
				[associationEnd(PACKAGE, false, null, true, "C", "c", false, false)],
				[associationEnd(PACKAGE, false, null, false, "A", "a", false, false)]
			])],
			[composition("CB", #[
				[associationEnd(PACKAGE, false, null, true, "C", "c", false, false)],
				[associationEnd(PACKAGE, false, null, false, "B", "b", false, false)]
			])],
			[connector("CAB", #[
				[connectorEnd("a", "P", "e")],
				[connectorEnd("b", "P", "f")]
			])]
		])
	}

	@Test
	def parseClassPropertyAccessExpression() {
		'''
			package test.model;

			class A {
				void foo() {
					send new S() to this->(P);
					send new S() to this->(AB.b).one();
					new B()->(AB.a).one();
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
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[sendSignal(
						[new_("S", null, #[], null)],
						[propertyAccess([this_], "P")]
					)],
					[sendSignal(
						[new_("S", null, #[], null)],
						[featureCall(
							[propertyAccess([this_], "b")],
							"one", #[]
						)]
					)],
					[featureCall(
						[propertyAccess(
							[new_("B", null, #[], null)], "a"
						)], "one", #[]
					)]
				])],
				[port("P", #[])]
			])],
			[signal("S", null, #[])],
			[class_("B", null, #[])],
			[association("AB", #[
				[associationEnd(PACKAGE, false, null, false, "A", "a", false, false)],
				[associationEnd(PACKAGE, false, null, false, "B", "b", false, false)]
			])]
		])
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
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[variableDeclaration("int", null, "i", [number(0)])],
					[whileLoop([binaryOperation(OPERATOR_LESS_THAN, [variable("i")], [number(10)])], #[
						[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
					])]
				])]
			])]
		])
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
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[variableDeclaration("int", null, "i", [number(0)])],
					[doWhileLoop(#[
						[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
					], [binaryOperation(OPERATOR_LESS_THAN, [variable("i")], [number(10)])])]
				])]
			])]
		])
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
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[variableDeclaration("int", null, "i", null)],
					[if_([bool(true)], #[
							[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
					], null, null)],
					[if_([bool(false)], #[
						[postfixOperation(OPERATOR_MINUS_MINUS, [variable("i")])]
					], null, #[
						[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
					])],
					[if_([bool(false)], #[
						[postfixOperation(OPERATOR_MINUS_MINUS, [variable("i")])]
					], [if_([bool(true)], #[
						[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
					], null, null)], null)]
				])]
			])]
		])
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
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[variableDeclaration("int", null, "i", null)],
					[switch_([variable("i")], #[
						[case_([number(0)], #[])]
					], #[
						[postfixOperation(OPERATOR_PLUS_PLUS, [variable("i")])]
					])],
					[switch_([variable("i")], #[
						[case_([number(0)], #[])],
						[case_([number(1)], null)],
						[case_([number(2)], #[
							[postfixOperation(OPERATOR_MINUS_MINUS, [variable("i")])]
						])]
					], null)]
				])]
			])]
		])
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
		file("test.model",null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
					[forEachLoop([parameter("B", "b")], [propertyAccess([this_], "b")], #[
						[featureCall([variable("b")], "bar", #[])]
					])]
				])]
			])],
			[class_("B", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "bar", #[], #[])]
			])],
			[association("AB", #[
				[associationEnd(PACKAGE, false, null, false, "A", "a", false, false)],
				[associationEnd(PACKAGE, false, null, false, "B", "b", false, false)]
			])]
		])
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
		file("test.model", null, #[
			[class_("A", null, #[
				[operation(PACKAGE, false, NON_EXTERNAL, "void", "foo", #[], #[
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
				])]
			])]
		])
	}

}
