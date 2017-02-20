package hu.elte.txtuml.xtxtuml.tests.parser;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

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
		file(
			"test.model", #[
				[importDeclaration(false, "java.lang.Integer", false)],
				[importDeclaration(true, "java.lang.Math", true)]
			],
			#[]
		)
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
			package test.model;
			execution TestExecution {}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[execution("TestExecution", #[])]
			]
		)
	}

	@Test
	def parseSignal() {
		'''
			package test.model;
			signal EmptyTestSignal;
			signal NotEmptyTestSignal {
				public int testAttribute;
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[signal("EmptyTestSignal", #[])],
				[signal(
					"NotEmptyTestSignal", #[
						[attribute(PUBLIC, "int", "testAttribute")]
					]
				)]
			]
		)
	}

	@Test
	def parseEmptyClass() {
		'''
			package test.model;
			class BaseTestClass;
			class DerivedTestClass extends BaseTestClass {}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_("BaseTestClass", null, #[])],
				[class_("DerivedTestClass", "BaseTestClass", #[])]
			]
		)
	}

	@Test
	def parseClassAttributeAndOperation() {
		'''
			package test.model;
			class TestClass {
				int a1;
				protected int a2;

				public void o1() {}

				private int o2() {
					return 0;
				}

				package TestClass o3(int p) {
					return null;
				}

				public TestClass(int p1, TestClass p2) {}
			}
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_(
					"TestClass", null, #[
						[attribute(PACKAGE, "int", "a1")],
						[attribute(PROTECTED, "int", "a2")],
						[operation(PUBLIC, "void", "o1", #[], #[])],
						[operation(
							PRIVATE, "int", "o2", #[], #[
								[return_[number(0)]]
							]
						)],
						[operation(
							PACKAGE, "TestClass", "o3", #[
								[parameter("int", "p")]
							], #[
								[return_[null_]]
							]
						)],
						[constructor(
							PUBLIC, "TestClass", #[
								[parameter("int", "p1")],
								[parameter("TestClass", "p2")]
							], #[]
						)]
					]
				)]
			]
		)
	}
	
	@Test
	def parseEmptyEnum() {
		'''
			package test.model;
			enum TestEnum;
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[enumeration(
					"TestEnum", #[
					]
				)]
			]
		)
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
		.parse.file("test.model", null, #[
			[enumeration(
				"TestEnum", #[
					[enumerationLiteral("A")],
					[enumerationLiteral("B")]
				]
			)]
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
		file(
			"test.model",
			null, #[
				[signal("Sig", #[])],
				[class_(
					"TestClass", null, #[
						[port(false, "Port", #[])],
						[state(INITIAL, "Init", #[])],
						[state(CHOICE, "Choice", #[])],
						[transition(
							"T1", #[
								[vertex(true, "Init")],
								[vertex(false, "Choice")],
								[effect(#[])]
							]
						)],
						[transition(
							"T2", #[
								[vertex(true, "Choice")],
								[vertex(false, "Composite")],
								[guard(null)]
							]
						)],
						[state(
							COMPOSITE, "Composite", #[
								[entryOrExit(true, #[])],
								[entryOrExit(false, #[])],
								[state(INITIAL, "CInit", #[])],
								[state(PLAIN, "State", #[])],
								[transition(
									"T3", #[
										[vertex(true, "CInit")],
										[vertex(false, "State")]
									]
								)],
								[transition(
									"T4", #[
										[vertex(true, "State")],
										[vertex(false, "State")],
										[trigger("Sig")],
										[port("Port")]
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
		file(
			"test.model",
			null, #[
				[interface_("TestInterface", #[])],
				[class_(
					"TestClass", null, #[
						[port(false, "EmptyPort", #[])],
						[port(
							true, "BehaviorPort", #[
								[portMember(true, "TestInterface")],
								[portMember(false, "TestInterface")]
							]
						)]
					]
				)]
			]
		)
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
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_("TestClass", null, #[])],
				[association(
					false, "TestAssociation1", #[
						[associationEnd(PACKAGE, false, null, false, "TestClass", "plainEnd")],
						[associationEnd(PACKAGE, true, [any], false, "TestClass", "hiddenEnd")]
					]
				)],
				[association(
					false, "TestAssociation2", #[
						[associationEnd(PACKAGE, true, [interval(1, null)], false, "TestClass", "intervalEnd")],
						[associationEnd(PACKAGE, false, [exact(5)], false, "TestClass", "exactEnd")]
					]
				)]
			]
		)
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
		'''
		.parse.
		file(
			"test.model",
			null, #[
				[class_("TestClass", null,  #[])],
				[association(
					true, "TestComposition", #[
						[associationEnd(PACKAGE, true, null, true, "TestClass", "containerEnd")],
						[associationEnd(PACKAGE, false, null, false, "TestClass", "otherEnd")]
					]
				)]
			]
		)
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
		file(
			"test.model",
			null, #[
				[signal("TestSignal", #[])],
				[interface_("EmptyTestInterface", #[])],
				[interface_(
					"NotEmptyTestInterface", #[
						[reception("TestSignal")]
					]
				)]
			]
		)
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
		file(
			"test.model",
			null, #[
				[interface_("I1", #[])],
				[interface_("I2", #[])],
				[interface_("I3", #[])],
				[class_(
					"A", null, #[
						[port(
							false, "AP1", #[
								[portMember(false, "I1")],
								[portMember(true, "I2")]
							]
						)],
						[port(
							false, "AP2", #[
								[portMember(false, "I3")]
							]
						)]
					]
				)],
				[class_(
					"B", null, #[
						[port(
							false, "BP", #[
								[portMember(false, "I2")],
								[portMember(true, "I1")]
							]
						)]
					]
				)],
				[class_(
					"C", null, #[
						[port(
							false, "CP", #[
								[portMember(false, "I3")]
							]
						)]
					]
				)],
				[association(
					true, "CA", #[
						[associationEnd(PACKAGE, false, null, true, "C", "c")],
						[associationEnd(PACKAGE, false, null, false, "A", "a")]
					]
				)],
				[association(
					true, "CB", #[
						[associationEnd(PACKAGE, false, null, true, "C", "c")],
						[associationEnd(PACKAGE, false, null, false, "B", "b")]
					]
				)],
				[connector(
					false, "A_AB", #[
						[connectorEnd("a", "AP1", "aap")],
						[connectorEnd("b", "BP", "abp")]
					]
				)],
				[connector(
					true, "D_CA", #[
						[connectorEnd("c", "CP", "dcp")],
						[connectorEnd("a", "AP2", "dap")]
					]
				)]
			]
		)
	}

}
