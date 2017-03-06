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
class XtxtUMLStructureCompilerTest {

	@Inject extension CompilationTestHelper;

	@Test
	def compileModelDeclaration() {
		'''
			model-package test.model;
		'''.assertCompilesTo('''
			@Model
			package test.model;

			import hu.elte.txtuml.api.model.Model;
		''');

		'''
			model-package test.model as "TestModel";
		'''.assertCompilesTo('''
			@Model("TestModel")
			package test.model;

			import hu.elte.txtuml.api.model.Model;
		''');
	}

	@Test
	def compileSignal() {
		'''
			package test.model;
			signal S;
		'''.assertCompilesTo('''
			package test.model;

			import hu.elte.txtuml.api.model.Signal;

			@SuppressWarnings("all")
			public class S extends Signal {
			}
		''');

		'''
			package test.model;
			signal S {
				public int a1;
				public String a2;
			}
		'''.assertCompilesTo('''
			package test.model;

			import hu.elte.txtuml.api.model.Signal;

			@SuppressWarnings("all")
			public class S extends Signal {
			  public int a1;
			  
			  public String a2;
			  
			  public S(final int a1, final String a2) {
			    this.a1 = a1;
			    this.a2 = a2;
			  }
			}
		''');
		}
		
		@Test
	def compileSignalInheritance() {
		'''
			package test.model;
			signal S1 {
				public int i;
				public boolean b;
			}
			signal S2 extends S1 {
				public String s;
			}
			signal S3 extends S2 {
				public double d;
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/S1.java

			package test.model;

			import hu.elte.txtuml.api.model.Signal;

			@SuppressWarnings("all")
			public class S1 extends Signal {
			  public int i;
			  
			  public boolean b;
			  
			  public S1(final int i, final boolean b) {
			    this.i = i;
			    this.b = b;
			  }
			}

			File 2 : /myProject/./src-gen/test/model/S2.java

			package test.model;

			import test.model.S1;

			@SuppressWarnings("all")
			public class S2 extends S1 {
			  public String s;
			  
			  public S2(final int i, final boolean b, final String s) {
			    super(i, b);
			    this.s = s;
			  }
			}

			File 3 : /myProject/./src-gen/test/model/S3.java

			package test.model;

			import test.model.S2;

			@SuppressWarnings("all")
			public class S3 extends S2 {
			  public double d;
			  
			  public S3(final int i, final boolean b, final String s, final double d) {
			    super(i, b, s);
			    this.d = d;
			  }
			}

		''');

		'''
			package test.model;
			signal S1 {
				public int i;
				public boolean b;
			}
			signal S3 extends S2 {
				public String s;
			}
			signal S2 extends S1;
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/S1.java

			package test.model;

			import hu.elte.txtuml.api.model.Signal;

			@SuppressWarnings("all")
			public class S1 extends Signal {
			  public int i;
			  
			  public boolean b;
			  
			  public S1(final int i, final boolean b) {
			    this.i = i;
			    this.b = b;
			  }
			}

			File 2 : /myProject/./src-gen/test/model/S2.java

			package test.model;

			import test.model.S1;

			@SuppressWarnings("all")
			public class S2 extends S1 {
			  public S2(final int i, final boolean b) {
			    super(i, b);
			  }
			}

			File 3 : /myProject/./src-gen/test/model/S3.java

			package test.model;

			import test.model.S2;

			@SuppressWarnings("all")
			public class S3 extends S2 {
			  public String s;
			  
			  public S3(final int i, final boolean b, final String s) {
			    super(i, b);
			    this.s = s;
			  }
			}

		''');
	}

	@Test
	def compileClass() {
		'''
			package test.model;
			class A;
		'''.assertCompilesTo('''
			package test.model;

			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			}
		''');

		'''
			package test.model;
			class A;
			class B extends A;
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			}

			File 2 : /myProject/./src-gen/test/model/B.java

			package test.model;

			import test.model.A;

			@SuppressWarnings("all")
			public class B extends A {
			}

		''');
	}

	@Test
	def compileClassAttributeAndOperation() {
		'''
			package test.model;
			class A {
				int a1;
				protected int a2;
				public void o1() {}
				private int o2() {
					return 0;
				}
				package A o3(int p) {
					return null;
				}
				public A(int p1, A p2) {}
			}
		'''.assertCompilesTo('''
			package test.model;

			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  int a1;
			  
			  protected int a2;
			  
			  public void o1() {
			  }
			  
			  private int o2() {
			    return 0;
			  }
			  
			  A o3(final int p) {
			    return null;
			  }
			  
			  public A(final int p1, final A p2) {
			  }
			}
		''')
	}

	@Test
	def compileStatemachine() {
		'''
			package test.model;
			signal Sig;
			class A {
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
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.From;
			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;
			import hu.elte.txtuml.api.model.StateMachine;
			import hu.elte.txtuml.api.model.To;
			import hu.elte.txtuml.api.model.Trigger;
			import test.model.Sig;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  public class Port extends ModelClass.Port<Interface.Empty, Interface.Empty> {
			  }
			  
			  public class Init extends StateMachine.Initial {
			  }
			  
			  public class Choice extends StateMachine.Choice {
			  }
			  
			  @From(A.Init.class)
			  @To(A.Choice.class)
			  public class T1 extends StateMachine.Transition {
			    @Override
			    public void effect() {
			    }
			  }
			  
			  @From(A.Choice.class)
			  @To(A.Composite.class)
			  public class T2 extends StateMachine.Transition {
			    @Override
			    public boolean guard() {
			      return Else();
			    }
			  }
			  
			  public class Composite extends StateMachine.CompositeState {
			    @Override
			    public void entry() {
			    }
			    
			    @Override
			    public void exit() {
			    }
			    
			    public class CInit extends StateMachine.Initial {
			    }
			    
			    public class State extends StateMachine.State {
			    }
			    
			    @From(A.Composite.CInit.class)
			    @To(A.Composite.State.class)
			    public class T3 extends StateMachine.Transition {
			    }
			    
			    @From(A.Composite.State.class)
			    @To(A.Composite.State.class)
			    @Trigger(port = A.Port.class, value = Sig.class)
			    public class T4 extends StateMachine.Transition {
			    }
			  }
			}

			File 2 : /myProject/./src-gen/test/model/Sig.java

			package test.model;

			import hu.elte.txtuml.api.model.Signal;

			@SuppressWarnings("all")
			public class Sig extends Signal {
			}

		''')
	}

	@Test
	def compilePort() {
		'''
			package test.model;
			interface I1;
			interface I2;
			class A {
				port EP {}
				behavior port BP {
					provided I1;
					required I2;
				}
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.BehaviorPort;
			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;
			import test.model.I1;
			import test.model.I2;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  public class EP extends ModelClass.Port<Interface.Empty, Interface.Empty> {
			  }
			  
			  @BehaviorPort
			  public class BP extends ModelClass.Port<I1, I2> {
			  }
			}

			File 2 : /myProject/./src-gen/test/model/I1.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;

			@SuppressWarnings("all")
			public interface I1 extends Interface {
			}

			File 3 : /myProject/./src-gen/test/model/I2.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;

			@SuppressWarnings("all")
			public interface I2 extends Interface {
			}

		''')
	}

	@Test
	def compileAssociation() {
		'''
			package test.model;
			class A;
			association AA1 {
				A a1;
				hidden * A a2;
			}
			association AA2 {
				hidden 1..* A a1;
				5 A a2;
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			}

			File 2 : /myProject/./src-gen/test/model/AA1.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import test.model.A;

			@SuppressWarnings("all")
			public class AA1 extends Association {
			  public class a1 extends Association.One<A> {
			  }
			  
			  public class a2 extends Association.HiddenMany<A> {
			  }
			}

			File 3 : /myProject/./src-gen/test/model/AA2.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.Max;
			import hu.elte.txtuml.api.model.Min;
			import test.model.A;

			@SuppressWarnings("all")
			public class AA2 extends Association {
			  public class a1 extends Association.HiddenSome<A> {
			  }
			  
			  @Min(5)
			  @Max(5)
			  public class a2 extends Association.Multiple<A> {
			  }
			}

		''')
	}

	@Test
	def compileComposition() {
		'''
			package test.model;
			class A;
			composition AA {
				hidden container A a1;
				A a2;
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.ModelClass;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			}

			File 2 : /myProject/./src-gen/test/model/AA.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.Composition;
			import test.model.A;

			@SuppressWarnings("all")
			public class AA extends Composition {
			  public class a1 extends Composition.HiddenContainer<A> {
			  }
			  
			  public class a2 extends Association.One<A> {
			  }
			}

		''')
	}

	@Test
	def compileInterface() {
		'''
			package test.model;
			signal S;
			interface I1 {}
			interface I2 {
				reception S;
			}
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/I1.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;

			@SuppressWarnings("all")
			public interface I1 extends Interface {
			}

			File 2 : /myProject/./src-gen/test/model/I2.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;
			import test.model.S;

			@SuppressWarnings("all")
			public interface I2 extends Interface {
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
	def compileConnector() {
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
		'''.assertCompilesTo('''
			MULTIPLE FILES WERE GENERATED

			File 1 : /myProject/./src-gen/test/model/A.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;
			import test.model.I1;
			import test.model.I2;
			import test.model.I3;

			@SuppressWarnings("all")
			public class A extends ModelClass {
			  public class AP1 extends ModelClass.Port<I1, I2> {
			  }
			  
			  public class AP2 extends ModelClass.Port<I3, Interface.Empty> {
			  }
			}

			File 2 : /myProject/./src-gen/test/model/A_AB.java

			package test.model;

			import hu.elte.txtuml.api.model.Connector;
			import hu.elte.txtuml.api.model.ConnectorBase;
			import test.model.A;
			import test.model.B;
			import test.model.CA;
			import test.model.CB;

			@SuppressWarnings("all")
			public class A_AB extends Connector {
			  public class aap extends ConnectorBase.One<CA.a, A.AP1> {
			  }
			  
			  public class abp extends ConnectorBase.One<CB.b, B.BP> {
			  }
			}

			File 3 : /myProject/./src-gen/test/model/B.java

			package test.model;

			import hu.elte.txtuml.api.model.ModelClass;
			import test.model.I1;
			import test.model.I2;

			@SuppressWarnings("all")
			public class B extends ModelClass {
			  public class BP extends ModelClass.Port<I2, I1> {
			  }
			}

			File 4 : /myProject/./src-gen/test/model/C.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;
			import hu.elte.txtuml.api.model.ModelClass;
			import test.model.I3;

			@SuppressWarnings("all")
			public class C extends ModelClass {
			  public class CP extends ModelClass.Port<I3, Interface.Empty> {
			  }
			}

			File 5 : /myProject/./src-gen/test/model/CA.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.Composition;
			import test.model.A;
			import test.model.C;

			@SuppressWarnings("all")
			public class CA extends Composition {
			  public class c extends Composition.Container<C> {
			  }
			  
			  public class a extends Association.One<A> {
			  }
			}

			File 6 : /myProject/./src-gen/test/model/CB.java

			package test.model;

			import hu.elte.txtuml.api.model.Association;
			import hu.elte.txtuml.api.model.Composition;
			import test.model.B;
			import test.model.C;

			@SuppressWarnings("all")
			public class CB extends Composition {
			  public class c extends Composition.Container<C> {
			  }
			  
			  public class b extends Association.One<B> {
			  }
			}

			File 7 : /myProject/./src-gen/test/model/D_CA.java

			package test.model;

			import hu.elte.txtuml.api.model.ConnectorBase;
			import hu.elte.txtuml.api.model.Delegation;
			import test.model.A;
			import test.model.C;
			import test.model.CA;

			@SuppressWarnings("all")
			public class D_CA extends Delegation {
			  public class dcp extends ConnectorBase.One<CA.c, C.CP> {
			  }
			  
			  public class dap extends ConnectorBase.One<CA.a, A.AP2> {
			  }
			}

			File 8 : /myProject/./src-gen/test/model/I1.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;

			@SuppressWarnings("all")
			public interface I1 extends Interface {
			}

			File 9 : /myProject/./src-gen/test/model/I2.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;

			@SuppressWarnings("all")
			public interface I2 extends Interface {
			}

			File 10 : /myProject/./src-gen/test/model/I3.java

			package test.model;

			import hu.elte.txtuml.api.model.Interface;

			@SuppressWarnings("all")
			public interface I3 extends Interface {
			}

		''')
	}

}
