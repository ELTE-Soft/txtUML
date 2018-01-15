package hu.elte.txtuml.xtxtuml.tests.compiler;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLExpressionCompilerTest {

	@Inject extension CompilationTestHelper;

	@Test
	def compileSendSignalExpression() {
		'''
			package test.model;
			signal S;
			interface I {
				reception S;
			}
			class A {
				void foo() {
					send new S() to this;
					send new S() to new A();
					S s = new S();
					send s to this->(P);
				}
				port P {
					required I;
				}
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.Action;
			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;
			import test.model.I;
			import test.model.S;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  void foo() {
			    Action.send(new S(), this);
			    Action.send(new S(), new A());
			    S s = new S();
			    Action.send(s, this.port(A.P.class).required::reception);
			  }
			  
			  public class P extends ModelClass.Port<Interface.Empty, I> {
			  }
			}

			File 2 : /myProject/./src-gen/test/model/I.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;
			import test.model.S;

			@SuppressWarnings("all")
			public interface I extends Interface {
			  void reception(final S signal);
			}

			File 3 : /myProject/./src-gen/test/model/S.java

			package test.model;

			import hu.elte.txtuml.api.model.Signal;

			@SuppressWarnings("all")
			public class S extends Signal {
			}

		''')
	}

	@Test
	def compileDeleteObjectExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					delete this;
					delete new A();
					A a = new A();
					delete a;
				}
			}
		'''.assertCompilesTo('''
			package test.model;

			import hu.elte.txtuml.api.model.Action;
			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  void foo() {
			    Action.delete(this);
			    Action.delete(new A());
			    A a = new A();
			    Action.delete(a);
			  }
			}
		''')
	}

	@Test
	def compileSignalAccessExpression() {
		'''
			package test.model;
			signal Signal {
				public String message;
			}
			class A {
				state State;
				transition T {
					from State;
					to State;
					trigger Signal;
					effect {
						log(trigger.message);
					}
				}
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.Action;
			import hu.elte.txtuml.api.model.From;
			import hu.elte.txtuml.api.model.ModelClass;
			import hu.elte.txtuml.api.model.StateMachine;
			import hu.elte.txtuml.api.model.To;
			import hu.elte.txtuml.api.model.Trigger;
			import test.model.Signal;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  public class State extends StateMachine.State {
			  }
			  
			  @From(A.State.class)
			  @To(A.State.class)
			  @Trigger(Signal.class)
			  public class T extends StateMachine.Transition {
			    @Override
			    public void effect() {
			      Action.log(getTrigger(Signal.class).message);
			    }
			  }
			}

			File 2 : /myProject/./src-gen/test/model/Signal.java

			package test.model;

			@SuppressWarnings("all")
			public class Signal extends hu.elte.txtuml.api.model.Signal {
			  public String message;
			  
			  public Signal(final String message) {
			    this.message = message;
			  }
			}

		''')
	}

	@Test
	def compileClassPropertyAccessExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					send new S() to this->(P);
					send new S() to this->(AB.b).one();
					new B()->(AB.a).one();
				}
				port P {
					required I;
				}
			}
			signal S;
			interface I {
				reception S;
			}
			class B;
			association AB {
				A a;
				B b;
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.Action;
			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;
			import test.model.AB;
			import test.model.B;
			import test.model.I;
			import test.model.S;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  void foo() {
			    Action.send(new S(), this.port(A.P.class).required::reception);
			    Action.send(new S(), this.assoc(AB.b.class).one());
			    new B().assoc(AB.a.class).one();
			  }
			  
			  public class P extends ModelClass.Port<Interface.Empty, I> {
			  }
			}

			File 2 : /myProject/./src-gen/test/model/AB.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.One;
			import test.model.A;
			import test.model.B;

			@SuppressWarnings("all")
			public class AB extends Association {
			  public class a extends Association.End<One<A>> {
			  }
			  
			  public class b extends Association.End<One<B>> {
			  }
			}

			File 3 : /myProject/./src-gen/test/model/B.java

			package test.model;

			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class B extends ModelClass {
			}

			File 4 : /myProject/./src-gen/test/model/I.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;
			import test.model.S;

			@SuppressWarnings("all")
			public interface I extends Interface {
			  void reception(final S signal);
			}

			File 5 : /myProject/./src-gen/test/model/S.java

			package test.model;

			import hu.elte.txtuml.api.model.Signal;

			@SuppressWarnings("all")
			public class S extends Signal {
			}

		''')
	}

}
