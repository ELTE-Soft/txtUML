package hu.elte.txtuml.xtxtuml.tests.typesystem;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAccessExpression
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.typesystem.IBatchTypeResolver
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLTypeComputerTests {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension IBatchTypeResolver;

	@Test
	def computeStatementTypes() {
		val file = '''
			package test.model;
			class A {
				void foo() {
					send new S() to this;
					delete this;
				}
			}
			signal S;
		'''.parse;

		val class = file.elements.head as TUClass;
		val op = class.members.head as TUOperation;
		val block = op.body as XBlockExpression;

		assertEquals("void", block.resolvedTypeId);
		assertEquals("void", block.expressions.head.resolvedTypeId);
		assertEquals("void", block.expressions.get(1).resolvedTypeId);
	}

	@Test
	def computeClassPropertyAccessExpressionTypes() {
		val file = '''
			package test.model;
			class A {
				void foo() {
					this->(AA.a1);
					this->(P);
				}
				port P {}
			}
			association AA {
				A a1;
				A a2;
			}
		'''.parse;

		val class = file.elements.head as TUClass;
		val op = class.members.head as TUOperation;
		val block = op.body as XBlockExpression;

		val assocEndAccess = block.expressions.head as TUClassPropertyAccessExpression;
		assertEquals("hu.elte.txtuml.api.model.Collection<test.model.A>", assocEndAccess.resolvedTypeId);

		val portAccess = block.expressions.get(1) as TUClassPropertyAccessExpression;
		assertEquals("test.model.A$P", portAccess.resolvedTypeId);
	}

	@Test
	def computeSignalAccessExpressionTypesWithoutInheritance() {
		val file1 = '''
			package test.model;
			class A {
				void foo() {
					trigger;
				}
				initial Init;
				state S1 {
					entry { trigger; }
					exit { trigger; }
				}
				state S2 {
					entry { trigger; }
					exit { trigger; }
				}
				state S3 {
					entry { trigger; }
					exit { trigger; }
				}
				choice C1;
				choice C2;
				transition T1 {
					effect { trigger; }
				}
				transition T2 {
					from Init;
					effect { trigger; }
				}
				transition T3 {
					from S1;
					to S2;
					trigger Sig1;
					effect { trigger; }
				}
				transition T4 {
					from S2;
					trigger Sig1;
				}
				transition T5 {
					from S2;
					trigger Sig2;
				}
				transition T6 {
					from C1;
					to C2;
					effect { trigger; }
				}
				transition T7 {
					from C2;
					to S3;
					effect { trigger; }
				}
				transition T8 {
					to C1;
					trigger Sig2;
				}
			}
			signal Sig1;
			signal Sig2;
		'''.parse;

		val accessExpressions1 = file1.eAllContents.filter(TUSignalAccessExpression).toList;
		assertEquals("hu.elte.txtuml.api.model.Signal", accessExpressions1.get(0).resolvedTypeId);
		assertEquals("hu.elte.txtuml.api.model.Signal", accessExpressions1.get(1).resolvedTypeId);
		assertEquals("test.model.Sig1", accessExpressions1.get(2).resolvedTypeId);
		assertEquals("test.model.Sig1", accessExpressions1.get(3).resolvedTypeId);
		assertEquals("hu.elte.txtuml.api.model.Signal", accessExpressions1.get(4).resolvedTypeId);
		assertEquals("test.model.Sig2", accessExpressions1.get(5).resolvedTypeId);
		assertEquals("hu.elte.txtuml.api.model.Signal", accessExpressions1.get(6).resolvedTypeId);
		assertEquals("hu.elte.txtuml.api.model.Signal", accessExpressions1.get(7).resolvedTypeId);
		assertEquals("hu.elte.txtuml.api.model.Signal", accessExpressions1.get(8).resolvedTypeId);
		assertEquals("test.model.Sig1", accessExpressions1.get(9).resolvedTypeId);
		assertEquals("test.model.Sig2", accessExpressions1.get(10).resolvedTypeId);
		assertEquals("test.model.Sig2", accessExpressions1.get(11).resolvedTypeId);

		val file2 = '''
			package test.model;
			class A {
				choice C1;
				choice C2;
				choice C3;
				state S1 {
					entry { trigger; }
				}
				transition T1 {
					to C1;
					trigger S;
				}
				transition T2 {
					from C1;
					to C2;
					effect { trigger; }
				}
				transition T3 {
					from C2;
					to C3;
					effect { trigger; }
				}
				transition T4 {
					from C3;
					to C1;
					effect { trigger; }
				}
				transition T5 {
					from C3;
					to S1;
				}
			}
			signal S;
		'''.parse;

		val accessExpressions2 = file2.eAllContents.filter(TUSignalAccessExpression);
		assertTrue(accessExpressions2.forall[resolvedTypeId == "hu.elte.txtuml.api.model.Signal"]);
	}

	@Test
	def computeSignalAccessExpressionTypesWithInheritance() {
		val simpleParsed = '''
			package test.model;
			signal Sig1;
			signal Sig2 extends Sig1;
			signal Sig3 extends Sig1;
			signal Sig4 extends Sig3;
			signal Sig5;
			signal Sig6 extends Sig5;
			class A {
				state S1 {
					entry { trigger; }
					exit { trigger; }
				}
				state S2 {
					entry { trigger; }
					exit { trigger; }
				}
				state S3 {
					entry { trigger; }
					exit { trigger; }
				}
				transition T1 {
					from S1;
					trigger Sig2;
				}
				transition T2 {
					from S1;
					trigger Sig4;
				}
				transition T3 {
					from S2;
					trigger Sig4;
				}
				transition T4 {
					from S2;
					trigger Sig5;
				}
				transition T5 {
					from S3;
					trigger Sig3;
				}
				transition T6 {
					from S3;
					trigger Sig3;
				}
				transition T7 {
					to S1;
					trigger Sig3;
				}
				transition T8 {
					to S1;
					trigger Sig3;
				}
				transition T9 {
					to S2;
					trigger Sig2;
				}
				transition T10 {
					to S2;
					trigger Sig6;
				}
				transition T11 {
					to S3;
					trigger Sig2;
				}
				transition T12 {
					to S3;
					trigger Sig4;
				}
			}
		'''.parse;

		val simpleAccesses = simpleParsed.eAllContents.filter(TUSignalAccessExpression).toList;
		assertEquals("test.model.Sig3", simpleAccesses.get(0).resolvedTypeId);
		assertEquals("test.model.Sig1", simpleAccesses.get(1).resolvedTypeId);
		assertEquals("hu.elte.txtuml.api.model.Signal", simpleAccesses.get(2).resolvedTypeId);
		assertEquals("hu.elte.txtuml.api.model.Signal", simpleAccesses.get(3).resolvedTypeId);
		assertEquals("test.model.Sig1", simpleAccesses.get(4).resolvedTypeId);
		assertEquals("test.model.Sig3", simpleAccesses.get(5).resolvedTypeId);

		val choiceParsed = '''
			package test.model;
			signal Sig1;
			signal Sig2 extends Sig1;
			signal Sig3 extends Sig1;
			signal Sig4 extends Sig3;
			signal Sig5;
			signal Sig6 extends Sig5;
			class A {
				choice C1;
				choice C2;
				choice C3;
				choice C4;
				choice C5;
				state S1 {
					entry { trigger; }
				}
				transition T1 {
					to C1;
					trigger Sig4;
				}
				transition T2 {
					from C1;
					to C2;
					effect { trigger; }
				}
				transition T3 {
					to C2;
					trigger Sig3;
				}
				transition T4 {
					from C2;
					to C3;
					effect { trigger; }
				}
				transition T5 {
					to C3;
					trigger Sig2;
				}
				transition T6 {
					from C3;
					to S1;
					effect { trigger; }
				}
				transition T7 {
					to C4;
					trigger Sig5;
				}
				transition T8 {
					from C4;
					to C5;
					effect { trigger; }
				}
				transition T9 {
					to C5;
					trigger Sig6;
				}
				transition T10 {
					from C5;
					to S1;
					effect { trigger; }
				}
			}
		'''.parse;

		val choiceAccesses = choiceParsed.eAllContents.filter(TUSignalAccessExpression).toList;
		assertEquals("hu.elte.txtuml.api.model.Signal", choiceAccesses.get(0).resolvedTypeId);
		assertEquals("test.model.Sig4", choiceAccesses.get(1).resolvedTypeId);
		assertEquals("test.model.Sig3", choiceAccesses.get(2).resolvedTypeId);
		assertEquals("test.model.Sig1", choiceAccesses.get(3).resolvedTypeId);
		assertEquals("test.model.Sig5", choiceAccesses.get(4).resolvedTypeId);
		assertEquals("test.model.Sig5", choiceAccesses.get(5).resolvedTypeId);
	}

	def private getResolvedTypeId(XExpression expr) {
		expr.resolveTypes.getActualType(expr).identifier
	}

}
