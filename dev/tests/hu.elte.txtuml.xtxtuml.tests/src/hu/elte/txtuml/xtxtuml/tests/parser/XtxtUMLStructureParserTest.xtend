package hu.elte.txtuml.xtxtuml.tests.parser;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

import static hu.elte.txtuml.xtxtuml.xtxtUML.TUExternality.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLStructureParserTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension XtxtUMLParserTestUtils;

	@Test
	def parseFile() {
		'''package test.model;'''
		.parse.
		file("test.model", null, #[])
	}

	@Test
	def parseImportDeclaration() {
		'''
			package test.model;
			import java.lang.Integer;
			import static java.lang.Math.*;
		'''
		.parse.
		file("test.model", #[
			[importDeclaration(false, "java.lang.Integer", false)],
			[importDeclaration(true, "java.lang.Math", true)]
		], #[])
	}

	@Test
	def parseModelDeclarationWithoutName() {
		'''model-package test.model;'''
		.parse.
		modelDeclaration("test.model", null)
	}

	@Test
	def parseModelDeclarationWithName() {
		'''model-package test.model as "TestModel";'''
		.parse.
		modelDeclaration("test.model", "TestModel")
	}

	@Test
	def parseExecution() {
		'''
			package test.exec;
			class TestClass; // in a model
			execution EmptyExecution;
			execution ExecutionWithInit {
				initialization {}
			}
			execution ExecutionWithMembers {
				int bTest = 5;
				TestClass tClass;
				configure {
					name = "ExecName";
				}
				initialization {
					tClass = new TestClass();
				}
				before {}
				during {}
				after {}
			}
		'''
		.parse.
		file("test.exec", null, #[
			[class_("TestClass", null, #[])],
			[execution("EmptyExecution", #[])],
			[execution("ExecutionWithInit", #[
				[block("initialization", #[])]
			])],
			[execution("ExecutionWithMembers", #[
				[attribute("int","bTest", [number(5)])],
				[attribute("TestClass", "tClass", null)],
				[block("configure",#[
					[assignment("name", [string("ExecName")])]
				])],
				[block("initialization", #[
					[assignment("tClass", [new_("TestClass", null, #[], null)])]
				])],
				[block("before", #[])],
				[block("during", #[])],
				[block("after", #[])]
			])]
		])
	}

	@Test
	def parseSignal() {
		'''
			package test.model;
			signal EmptyTestSignal;
			signal NotEmptyTestSignal {
				public int testAttribute;
			}
			signal EmptySubTestSignal extends NotEmptyTestSignal;
			signal NotEmptySubTestSignal extends EmptyTestSignal {
				public int testAttribute;
			}
		'''
		.parse.
		file("test.model", null, #[
			[signal("EmptyTestSignal", null, #[])],
			[signal("NotEmptyTestSignal", null, #[
				[attribute(PUBLIC, "int", "testAttribute")]
			])],
			[signal("EmptySubTestSignal", "NotEmptyTestSignal", #[])],
			[signal("NotEmptySubTestSignal", "EmptyTestSignal", #[
				[attribute(PUBLIC, "int", "testAttribute")]
			])]
		])
	}

	@Test
	def parseEmptyClass() {
		'''
			package test.model;
			class BaseTestClass;
			class DerivedTestClass extends BaseTestClass {}
		'''
		.parse.
		file("test.model", null, #[
			[class_("BaseTestClass", null, #[])],
			[class_("DerivedTestClass", "BaseTestClass", #[])]
		])
	}

	@Test
	def parseClassAttributeAndOperation() {
		'''
			package test.model;
			class TestClass {
				int a1;
				protected int a2;
				public static external int a3 = 0;
				private external String a4;
				public void o1() {}
				private int o2() {
					return 0;
				}
				package TestClass o3(int p) {
					return null;
				}
				public static void o4() {}
				public external void o5() {}
				public external-body void o6() {}
				public static external void o7() {}
				public TestClass(int p1, TestClass p2) {}
				external TestClass() {}
				external-body TestClass(int p) {}
			}
		'''
		.parse.
		file("test.model", null, #[
			[class_("TestClass", null, #[
				[attribute(PACKAGE, false, NON_EXTERNAL, "int", "a1", null)],
				[attribute(PROTECTED, false, NON_EXTERNAL, "int", "a2", null)],
				[attribute(PUBLIC, true, EXTERNAL, "int", "a3", [number(0)])],
				[attribute(PRIVATE, false, EXTERNAL, "String", "a4", null)],
				[operation(PUBLIC, false, NON_EXTERNAL, "void", "o1", #[], #[])],
				[operation(PRIVATE, false, NON_EXTERNAL, "int", "o2", #[], #[
					[return_[number(0)]]
				])],
				[operation(PACKAGE, false, NON_EXTERNAL, "TestClass", "o3", #[
					[parameter("int", "p")]
				], #[
					[return_[null_]]
				])],
				[operation(PUBLIC, true, NON_EXTERNAL, "void", "o4", #[], #[])],
				[operation(PUBLIC, false, EXTERNAL, "void", "o5", #[], #[])],
				[operation(PUBLIC, false, EXTERNAL_BODY, "void", "o6", #[], #[])],
				[operation(PUBLIC, true, EXTERNAL, "void", "o7", #[], #[])],
				[constructor(PUBLIC, NON_EXTERNAL, "TestClass", #[
					[parameter("int", "p1")],
					[parameter("TestClass", "p2")]
				], #[])],
				[constructor(PACKAGE, EXTERNAL, "TestClass", #[], #[])],
				[constructor(PACKAGE, EXTERNAL_BODY, "TestClass", #[[parameter("int", "p")]], #[])]
			])]
		])
	}

	@Test
	def parseEmptyDataType() {
		'''
			package test.model;
			datatype BaseTestDataType;
			datatype DerivedTestDataType extends BaseTestDataType {}
		'''
		.parse.
		file("test.model", null, #[
			[datatype("BaseTestDataType", null, #[])],
			[datatype("DerivedTestDataType", "BaseTestDataType", #[])]
		])
	}

	@Test
	def parseDataTypeAttributeAndOperation() {
		'''
			package test.model;
			datatype TestDataType {
				int a1;
				protected int a2;
				public static external int a3 = 0;
				private external String a4;
				public void o1() {}
				private int o2() {
					return 0;
				}
				package TestDataType o3(int p) {
					return null;
				}
				public static void o4() {}
				public external void o5() {}
				public external-body void o6() {}
				public static external void o7() {}
				public TestDataType(int p1, TestDataType p2) {}
				external TestDataType() {}
				external-body TestDataType(int p) {}
			}
		'''
		.parse.
		file("test.model", null, #[
			[datatype("TestDataType", null, #[
				[attribute(PACKAGE, false, NON_EXTERNAL, "int", "a1", null)],
				[attribute(PROTECTED, false, NON_EXTERNAL, "int", "a2", null)],
				[attribute(PUBLIC, true, EXTERNAL, "int", "a3", [number(0)])],
				[attribute(PRIVATE, false, EXTERNAL, "String", "a4", null)],
				[operation(PUBLIC, false, NON_EXTERNAL, "void", "o1", #[], #[])],
				[operation(PRIVATE, false, NON_EXTERNAL, "int", "o2", #[], #[
					[return_[number(0)]]
				])],
				[operation(PACKAGE, false, NON_EXTERNAL, "TestDataType", "o3", #[
					[parameter("int", "p")]
				], #[
					[return_[null_]]
				])],
				[operation(PUBLIC, true, NON_EXTERNAL, "void", "o4", #[], #[])],
				[operation(PUBLIC, false, EXTERNAL, "void", "o5", #[], #[])],
				[operation(PUBLIC, false, EXTERNAL_BODY, "void", "o6", #[], #[])],
				[operation(PUBLIC, true, EXTERNAL, "void", "o7", #[], #[])],
				[constructor(PUBLIC, NON_EXTERNAL, "TestDataType", #[
					[parameter("int", "p1")],
					[parameter("TestDataType", "p2")]
				], #[])],
				[constructor(PACKAGE, EXTERNAL, "TestDataType", #[], #[])],
				[constructor(PACKAGE, EXTERNAL_BODY, "TestDataType", #[[parameter("int", "p")]], #[])]
			])]
		])
	}

	@Test
	def parseEmptyEnum() {
		'''
			package test.model;
			enum TestEnum;
		'''
		.parse.
		file("test.model", null, #[
			[enumeration("TestEnum", #[])]
		])
	}

	@Test
	def parseEnumWithLiterals() {	
		'''
			package test.model;
			enum TestEnum {
				A,
				B
			}
		'''
		.parse.
		file("test.model", null, #[
			[enumeration("TestEnum", #[
				[enumerationLiteral("A")],
				[enumerationLiteral("B")]
			])]
		])
	}

	@Test
	def parseStatemachine() {
		'''
			package test.model;
			signal Sig;
			class TestClass {
				port Port {}

				initial Init;
				choice Choice;

				transition T1 {
					from Init;
					to Choice;
					effect {}
				}

				transition T2 {
					from Choice;
					to Composite;
					guard ( else );
				}

				composite Composite {
					entry {}
					exit {}

					initial CInit;
					state State;

					transition T3 {
						from CInit;
						to State;
					}

					transition T4 {
						from State;
						to State;
						trigger Sig;
						port Port;
					}
				}
			}
		'''
		.parse.
		file("test.model", null, #[
			[signal("Sig", null, #[])],
			[class_("TestClass", null, #[
				[port("Port", #[])],
				[state(INITIAL, "Init", #[])],
				[state(CHOICE, "Choice", #[])],
				[transition("T1", #[
					[from("Init")],
					[to("Choice")],
					[effect(#[])]
				])],
				[transition("T2", #[
					[from("Choice")],
					[to("Composite")],
					[guard(null)]
				])],
				[state(COMPOSITE, "Composite", #[
					[entry(#[])],
					[exit(#[])],
					[state(INITIAL, "CInit", #[])],
					[state(PLAIN, "State", #[])],
					[transition("T3", #[
						[from("CInit")],
						[to("State")]
					])],
					[transition("T4", #[
						[from("State")],
						[to("State")],
						[trigger("Sig")],
						[port("Port")]
					])]
				])]
			])]
		])
	}

	@Test
	def parsePort() {
		'''
			package test.model;
			interface TestInterface;
			class TestClass {
				port EmptyPort {}
				behavior port BehaviorPort {
					required TestInterface;
					provided TestInterface;
				}
			}
		'''
		.parse.
		file("test.model", null, #[
			[interface_("TestInterface", #[])],
			[class_("TestClass", null, #[
				[port("EmptyPort", #[])],
				[behaviorPort("BehaviorPort", #[
					[required("TestInterface")],
					[provided("TestInterface")]
				])]
			])]
		])
	}

	@Test
	def parseAssociation() {
		'''
			package test.model;
			class TestClass;
			association TestAssociation1 {
				TestClass plainEnd;
				hidden * TestClass hiddenEnd;
			}
			association TestAssociation2 {
				hidden 1..* TestClass intervalEnd;
				5 TestClass exactEnd;
			}
			association TestAssociation3 {
				1..6 TestClass intervalEnd;
				ordered 2..5 TestClass exactEnd;
			}
			association TestAssociation4 {
				unique 1..6 TestClass intervalEnd;
				ordered unique * TestClass exactEnd;
			}
			
		'''
		.parse.
		file("test.model", null, #[
			[class_("TestClass", null, #[])],
			[association("TestAssociation1", #[
				[associationEnd(PACKAGE, false, null, false, "TestClass", "plainEnd", false, false)],
				[associationEnd(PACKAGE, true, [any], false, "TestClass", "hiddenEnd", false, false)]
			])],
			[association("TestAssociation2", #[
				[associationEnd(PACKAGE, true, [interval(1, null)], false, "TestClass", "intervalEnd", false, false)],
				[associationEnd(PACKAGE, false, [exact(5)], false, "TestClass", "exactEnd", false, false)]
			])],
			[association("TestAssociation3", #[
				[associationEnd(PACKAGE, false, [interval(1, 6)], false, "TestClass", "intervalEnd", false, false)],
				[associationEnd(PACKAGE, false, [interval(2, 5)], false, "TestClass", "exactEnd", true, false)]
			])],
			[association("TestAssociation4", #[
				[associationEnd(PACKAGE, false, [interval(1, 6)], false, "TestClass", "intervalEnd", false, true)],
				[associationEnd(PACKAGE, false, [any], false, "TestClass", "exactEnd", true, true)]
			])]
		])
	}

	@Test
	def parseComposition() {
		'''
			package test.model;
			class TestClass;
			composition TestComposition {
				hidden container TestClass containerEnd;
				TestClass otherEnd;
			}
			composition TestComposition2 {
				hidden container TestClass containerEnd;
				ordered unique 1..6 TestClass otherEnd;
			}
		'''
		.parse.
		file("test.model", null, #[
			[class_("TestClass", null,  #[])],
			[composition("TestComposition", #[
				[associationEnd(PACKAGE, true, null, true, "TestClass", "containerEnd", false, false)],
				[associationEnd(PACKAGE, false, null, false, "TestClass", "otherEnd", false, false)]
			])],
			[composition("TestComposition2", #[
				[associationEnd(PACKAGE, true, null, true, "TestClass", "containerEnd", false, false)],
				[associationEnd(PACKAGE, false, [interval(1, 6)], false, "TestClass", "otherEnd", true, true)]
			])]
		])
	}

	@Test
	def parseInterface() {
		'''
			package test.model;
			signal TestSignal;
			interface EmptyTestInterface {}
			interface NotEmptyTestInterface {
				reception TestSignal;
			}
		'''
		.parse.
		file("test.model", null, #[
			[signal("TestSignal", null, #[])],
			[interface_("EmptyTestInterface", #[])],
			[interface_("NotEmptyTestInterface", #[
				[reception("TestSignal")]
			])]
		])
	}

	@Test
	def parseConnector() {
		'''
			package test.model;

			interface I1 {}
			interface I2 {}
			interface I3 {}

			class A {
				port AP1 {
					provided I1;
					required I2;
				}

				port AP2 {
					provided I3;
				}
			}

			class B {
				port BP {
					provided I2;
					required I1;
				}
			}

			class C {
				port CP {
					provided I3;
				}
			}

			composition CA {
				container C c;
				A a;
			}

			composition CB {
				container C c;
				B b;
			}

			connector A_AB {
				CA.a->A.AP1 aap;
				CB.b->B.BP abp;
			}

			delegation D_CA {
				CA.c->C.CP dcp;
				CA.a->A.AP2 dap;
			}
		'''
		.parse.
		file("test.model", null, #[
			[interface_("I1", #[])],
			[interface_("I2", #[])],
			[interface_("I3", #[])],
			[class_("A", null, #[
				[port("AP1", #[
					[provided("I1")],
					[required("I2")]
				])],
				[port("AP2", #[
					[provided("I3")]
				])]
			])],
			[class_("B", null, #[
				[port("BP", #[
					[provided("I2")],
					[required("I1")]
				])]
			])],
			[class_("C", null, #[
				[port("CP", #[
					[provided("I3")]
				])]
			])],
			[composition("CA", #[
				[associationEnd(PACKAGE, false, null, true, "C", "c", false, false)],
				[associationEnd(PACKAGE, false, null, false, "A", "a", false, false)]
			])],
			[composition("CB", #[
				[associationEnd(PACKAGE, false, null, true, "C", "c", false, false)],
				[associationEnd(PACKAGE, false, null, false, "B", "b", false, false)]
			])],
			[connector("A_AB", #[
				[connectorEnd("a", "AP1", "aap")],
				[connectorEnd("b", "BP", "abp")]
			])],
			[delegation("D_CA", #[
				[connectorEnd("c", "CP", "dcp")],
				[connectorEnd("a", "AP2", "dap")]
			])]
		])
	}
	
	@Test
	def parseCollection() {
		'''
			package test.model;
			class A;
			
			ordered collection of 1..10 A as CA;
			collection of * int as CB;
			unique collection of 1..* A as CC;
			
			collection of 1..10 as GCA;
			ordered unique collection of 6 as GCB;
		'''
		.parse.
		file("test.model", null, #[
			[class_("A", null, #[])],
			[collection("CA", "A", true, false, [interval(1, 10)])],
			[collection("CB", "int", false, false, [any])],
			[collection("CC", "A", false, true, [fromToInf(1)])],
			[collection("GCA", null, false, false, [interval(1, 10)])],
			[collection("GCB", null, true, true, [exact(6)])]
		])
	}

}
