package hu.elte.txtuml.xtxtuml.tests.scoping;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.eclipse.xtext.xbase.XAbstractFeatureCall
import org.eclipse.xtext.xbase.XBlockExpression
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLScopingTests {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;

	@Test
	def resolveJtxtUMLActionMethods() {
		val file = '''
			package test.model;
			class C {
				void foo() {
					A a = new A();
					B b = new B();
					log("message");
					start(a);
					start(b);
					link(CAB.a, a, CAB.b, b);
					connect(a->(A.P), DAB.bp, b->(B.P));
					^send(new S(), a);
					unlink(CAB.a, a, CAB.b, b);
					^delete(a);
					^delete(b);
				}
			}
			signal S;
			interface I { reception S; }
			class A {
				port P {
					required I;
					provided I;
				}
			}
			class B {
				port P {
					required I;
					provided I;
				}
			}
			composition CAB {
				container A a;
				B b;
			}
			delegation DAB {
				CAB.a->A.P ap;
				CAB.b->B.P bp;
			}
		'''.parse;

		file.assertNoErrors;
		val c = file.elements.head as TUClass;
		val op = c.members.head as TUOperation;
		val block = op.body as XBlockExpression;

		assertTrue(block.expressions.drop(2).forall [
			it instanceof XAbstractFeatureCall &&
				(it as XAbstractFeatureCall).feature.identifier.startsWith("hu.elte.txtuml.api.model.Action")
		]);
	}

}
