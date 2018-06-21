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
	def compileStartObjectExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					start this;
					start new A();
					A a = new A();
					start a;
				}
			}
		'''.assertCompilesTo('''
			package test.model;

			import hu.elte.txtuml.api.model.Action;
			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  void foo() {
			    Action.start(this);
			    Action.start(new A());
			    A a = new A();
			    Action.start(a);
			  }
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
	def compileLogExpression() {
		'''
			package test.model;
			class A {
				void foo() {
					log "foo";
					log-error "bar";
					String message = "baz";
					log message;
				}
			}
		'''.assertCompilesTo('''
			package test.model;

			import hu.elte.txtuml.api.model.Action;
			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  void foo() {
			    Action.log("foo");
			    Action.logError("bar");
			    String message = "baz";
			    Action.log(message);
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

	@Test
	def compileCreateObjectExpression() {
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
		'''.assertCompilesTo('''
			package test.model;

			import hu.elte.txtuml.api.model.Action;
			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  A() {
			  }
			  
			  A(final int i) {
			  }
			  
			  void f() {
			    new A();
			    new A(0);
			    Action.create(A.class);
			    Action.create(A.class, 0);
			    Action.createWithName(A.class, "a");
			    Action.createWithName(A.class, "a", 0);
			    Action.createWithName(A.class, "a");
			    Action.createWithName(A.class, "a", 0);
			    A a = null;
			    a = new A();
			    a = new A(0);
			    a = Action.create(A.class);
			    a = Action.create(A.class, 0);
			    a = Action.createWithName(A.class, "a");
			    a = Action.createWithName(A.class, "a", 0);
			    a = Action.createWithName(A.class, "a");
			    a = Action.createWithName(A.class, "a", 0);
			  }
			}
		''')
	}

	@Test
	def compileBindExpression() {
		'''
			package test.model;
			class A {
				void f() {
					AD ad = new AD();
					B b = new B();
					C c = new C();

					// link, 1-1 match
					link this, b via AB;
					link this as AB.a, b via AB;
					link this, b as AB.b via AB;
					link this as AB.a, b as AB.b via AB;

					// link, 2-2 match
					link this as AA.e, this via AA;
					link this, this as AA.f via AA;
					link this as AA.e, this as AA.f via AA;

					// link, 1-2 match
					link this, ad via AAD;
					link this as AAD.a, ad via AAD;
					link this, ad as AAD.ad via AAD;
					link this as AAD.a, ad as AAD.ad via AAD;

					// link, 2-1 match
					link ad, this via AAD;
					link ad as AAD.ad, this via AAD;
					link ad, this as AAD.a via AAD;
					link ad as AAD.ad, this as AAD.a via AAD;

					// unlink, 1-1 match
					unlink this, b via AB;
					unlink this as AB.a, b via AB;
					unlink this, b as AB.b via AB;
					unlink this as AB.a, b as AB.b via AB;

					// unlink, 2-2 match
					unlink this as AA.e, this via AA;
					unlink this, this as AA.f via AA;
					unlink this as AA.e, this as AA.f via AA;

					// unlink, 1-2 match
					unlink this, ad via AAD;
					unlink this as AAD.a, ad via AAD;
					unlink this, ad as AAD.ad via AAD;
					unlink this as AAD.a, ad as AAD.ad via AAD;

					// unlink, 2-1 match
					unlink ad, this via AAD;
					unlink ad as AAD.ad, this via AAD;
					unlink ad, this as AAD.a via AAD;
					unlink ad as AAD.ad, this as AAD.a via AAD;

					// connect, assembly, 1-1 match
					connect this->(P), b->(B.P) via CAB;
					connect this->(P) as CAB.e, b->(B.P) via CAB;
					connect this->(P), b->(B.P) as CAB.f via CAB;
					connect this->(P) as CAB.e, b->(B.P) as CAB.f via CAB;

					// connect, assembly, 2-2 match
					connect this->(P) as CAA.e, this->(P) via CAA;
					connect this->(P), this->(P) as CAA.f via CAA;
					connect this->(P) as CAA.e, this->(P) as CAA.f via CAA;

					// connect, delegation, 1-1 match
					connect c->(C.P), this->(P) via DCA;
					connect c->(C.P) as DCA.e, this->(P) via DCA;
					connect c->(C.P), this->(P) as DCA.f via DCA;
					connect c->(C.P) as DCA.e, this->(P) as DCA.f via DCA;

					// connect, delegation, 1-1 match, reverse
					connect this->(P), c->(C.P) via DCA;
					connect this->(P) as DCA.f, c->(C.P) via DCA;
					connect this->(P), c->(C.P) as DCA.e via DCA;
					connect this->(P) as DCA.f, c->(C.P) as DCA.e via DCA;

					// connect, delegation, 2-2 match
					connect c->(C.P) as DCC.e, c->(C.P) via DCC;
					connect c->(C.P), c->(C.P) as DCC.f via DCC;
					connect c->(C.P) as DCC.e, c->(C.P) as DCC.f via DCC;

					// connect, delegation, 2-2 match, reverse
					connect c->(C.P) as DCC.f, c->(C.P) via DCC;
					connect c->(C.P), c->(C.P) as DCC.e via DCC;
					connect c->(C.P) as DCC.f, c->(C.P) as DCC.e via DCC;
				}
				port P {}
			}
			class AD extends A;
			class B {
				port P {}
			}
			class C {
				port P {}
			}
			association AB {
				A a;
				B b;
			}
			association AA {
				A e;
				A f;
			}
			association AAD {
				A a;
				AD ad;
			}
			composition CA {
				container C c;
				A a;
			}
			composition CA2 {
				container C c;
				A a;
			}
			composition CB {
				container C c;
				B b;
			}
			composition CC {
				container C e;
				C f;
			}
			connector CAB {
				CA.a->A.P e;
				CB.b->B.P f;
			}
			connector CAA {
				CA.a->A.P e;
				CA2.a->A.P f;
			}
			delegation DCA {
				CA.c->C.P e;
				CA.a->A.P f;
			}
			delegation DCC {
				CC.e->C.P e;
				CC.f->C.P f;
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.Action;
			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;
			import test.model.AA;
			import test.model.AAD;
			import test.model.AB;
			import test.model.AD;
			import test.model.B;
			import test.model.C;
			import test.model.CAA;
			import test.model.CAB;
			import test.model.DCA;
			import test.model.DCC;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  void f() {
			    AD ad = new AD();
			    B b = new B();
			    C c = new C();
			    Action.link(AB.a.class, this, test.model.AB.b.class, b);
			    Action.link(AB.a.class, this, test.model.AB.b.class, b);
			    Action.link(AB.a.class, this, test.model.AB.b.class, b);
			    Action.link(AB.a.class, this, test.model.AB.b.class, b);
			    Action.link(AA.e.class, this, AA.f.class, this);
			    Action.link(AA.e.class, this, AA.f.class, this);
			    Action.link(AA.e.class, this, AA.f.class, this);
			    Action.link(AAD.a.class, this, test.model.AAD.ad.class, ad);
			    Action.link(AAD.a.class, this, test.model.AAD.ad.class, ad);
			    Action.link(AAD.a.class, this, test.model.AAD.ad.class, ad);
			    Action.link(AAD.a.class, this, test.model.AAD.ad.class, ad);
			    Action.link(test.model.AAD.ad.class, ad, AAD.a.class, this);
			    Action.link(test.model.AAD.ad.class, ad, AAD.a.class, this);
			    Action.link(test.model.AAD.ad.class, ad, AAD.a.class, this);
			    Action.link(test.model.AAD.ad.class, ad, AAD.a.class, this);
			    Action.unlink(AB.a.class, this, test.model.AB.b.class, b);
			    Action.unlink(AB.a.class, this, test.model.AB.b.class, b);
			    Action.unlink(AB.a.class, this, test.model.AB.b.class, b);
			    Action.unlink(AB.a.class, this, test.model.AB.b.class, b);
			    Action.unlink(AA.e.class, this, AA.f.class, this);
			    Action.unlink(AA.e.class, this, AA.f.class, this);
			    Action.unlink(AA.e.class, this, AA.f.class, this);
			    Action.unlink(AAD.a.class, this, test.model.AAD.ad.class, ad);
			    Action.unlink(AAD.a.class, this, test.model.AAD.ad.class, ad);
			    Action.unlink(AAD.a.class, this, test.model.AAD.ad.class, ad);
			    Action.unlink(AAD.a.class, this, test.model.AAD.ad.class, ad);
			    Action.unlink(test.model.AAD.ad.class, ad, AAD.a.class, this);
			    Action.unlink(test.model.AAD.ad.class, ad, AAD.a.class, this);
			    Action.unlink(test.model.AAD.ad.class, ad, AAD.a.class, this);
			    Action.unlink(test.model.AAD.ad.class, ad, AAD.a.class, this);
			    Action.connect(CAB.e.class, this.port(A.P.class), CAB.f.class, b.port(B.P.class));
			    Action.connect(CAB.e.class, this.port(A.P.class), CAB.f.class, b.port(B.P.class));
			    Action.connect(CAB.e.class, this.port(A.P.class), CAB.f.class, b.port(B.P.class));
			    Action.connect(CAB.e.class, this.port(A.P.class), CAB.f.class, b.port(B.P.class));
			    Action.connect(CAA.e.class, this.port(A.P.class), CAA.f.class, this.port(A.P.class));
			    Action.connect(CAA.e.class, this.port(A.P.class), CAA.f.class, this.port(A.P.class));
			    Action.connect(CAA.e.class, this.port(A.P.class), CAA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCA.f.class, this.port(A.P.class));
			    Action.connect(c.port(C.P.class), DCC.f.class, c.port(C.P.class));
			    Action.connect(c.port(C.P.class), DCC.f.class, c.port(C.P.class));
			    Action.connect(c.port(C.P.class), DCC.f.class, c.port(C.P.class));
			    Action.connect(c.port(C.P.class), DCC.f.class, c.port(C.P.class));
			    Action.connect(c.port(C.P.class), DCC.f.class, c.port(C.P.class));
			    Action.connect(c.port(C.P.class), DCC.f.class, c.port(C.P.class));
			  }
			  
			  public class P extends ModelClass.Port<Interface.Empty, Interface.Empty> {
			  }
			}

			File 2 : /myProject/./src-gen/test/model/AA.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.One;
			import test.model.A;

			@SuppressWarnings("all")
			public class AA extends Association {
			  public class e extends Association.End<One<A>> {
			  }
			  
			  public class f extends Association.End<One<A>> {
			  }
			}

			File 3 : /myProject/./src-gen/test/model/AAD.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.One;
			import test.model.A;
			import test.model.AD;

			@SuppressWarnings("all")
			public class AAD extends Association {
			  public class a extends Association.End<One<A>> {
			  }
			  
			  public class ad extends Association.End<One<AD>> {
			  }
			}

			File 4 : /myProject/./src-gen/test/model/AB.java

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

			File 5 : /myProject/./src-gen/test/model/AD.java

			package test.model;

			import test.model.A;

			@SuppressWarnings("all")
			public class AD extends A {
			}

			File 6 : /myProject/./src-gen/test/model/B.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class B extends ModelClass {
			  public class P extends ModelClass.Port<Interface.Empty, Interface.Empty> {
			  }
			}

			File 7 : /myProject/./src-gen/test/model/C.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class C extends ModelClass {
			  public class P extends ModelClass.Port<Interface.Empty, Interface.Empty> {
			  }
			}

			File 8 : /myProject/./src-gen/test/model/CA.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.Composition;
			import hu.elte.txtuml.api.model.One;
			import test.model.A;
			import test.model.C;

			@SuppressWarnings("all")
			public class CA extends Composition {
			  public class c extends Composition.ContainerEnd<C> {
			  }
			  
			  public class a extends Association.End<One<A>> {
			  }
			}

			File 9 : /myProject/./src-gen/test/model/CA2.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.Composition;
			import hu.elte.txtuml.api.model.One;
			import test.model.A;
			import test.model.C;

			@SuppressWarnings("all")
			public class CA2 extends Composition {
			  public class c extends Composition.ContainerEnd<C> {
			  }
			  
			  public class a extends Association.End<One<A>> {
			  }
			}

			File 10 : /myProject/./src-gen/test/model/CAA.java

			package test.model;

			import hu.elte.txtuml.api.model.Connector;
			import hu.elte.txtuml.api.model.ConnectorBase;
			import test.model.A;
			import test.model.CA;
			import test.model.CA2;

			@SuppressWarnings("all")
			public class CAA extends Connector {
			  public class e extends ConnectorBase.One<CA.a, A.P> {
			  }
			  
			  public class f extends ConnectorBase.One<CA2.a, A.P> {
			  }
			}

			File 11 : /myProject/./src-gen/test/model/CAB.java

			package test.model;

			import hu.elte.txtuml.api.model.Connector;
			import hu.elte.txtuml.api.model.ConnectorBase;
			import test.model.A;
			import test.model.B;
			import test.model.CA;
			import test.model.CB;

			@SuppressWarnings("all")
			public class CAB extends Connector {
			  public class e extends ConnectorBase.One<CA.a, A.P> {
			  }
			  
			  public class f extends ConnectorBase.One<CB.b, B.P> {
			  }
			}

			File 12 : /myProject/./src-gen/test/model/CB.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.Composition;
			import hu.elte.txtuml.api.model.One;
			import test.model.B;
			import test.model.C;

			@SuppressWarnings("all")
			public class CB extends Composition {
			  public class c extends Composition.ContainerEnd<C> {
			  }
			  
			  public class b extends Association.End<One<B>> {
			  }
			}

			File 13 : /myProject/./src-gen/test/model/CC.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.Composition;
			import hu.elte.txtuml.api.model.One;
			import test.model.C;

			@SuppressWarnings("all")
			public class CC extends Composition {
			  public class e extends Composition.ContainerEnd<C> {
			  }
			  
			  public class f extends Association.End<One<C>> {
			  }
			}

			File 14 : /myProject/./src-gen/test/model/DCA.java

			package test.model;

			import hu.elte.txtuml.api.model.ConnectorBase;
			import hu.elte.txtuml.api.model.Delegation;
			import test.model.A;
			import test.model.C;
			import test.model.CA;

			@SuppressWarnings("all")
			public class DCA extends Delegation {
			  public class e extends ConnectorBase.One<CA.c, C.P> {
			  }
			  
			  public class f extends ConnectorBase.One<CA.a, A.P> {
			  }
			}

			File 15 : /myProject/./src-gen/test/model/DCC.java

			package test.model;

			import hu.elte.txtuml.api.model.ConnectorBase;
			import hu.elte.txtuml.api.model.Delegation;
			import test.model.C;
			import test.model.CC;

			@SuppressWarnings("all")
			public class DCC extends Delegation {
			  public class e extends ConnectorBase.One<CC.e, C.P> {
			  }
			  
			  public class f extends ConnectorBase.One<CC.f, C.P> {
			  }
			}

		''')
	}

}
