package hu.elte.txtuml.xtxtuml.tests

// Junit
import org.junit.Test;
import org.junit.runner.RunWith;

// Guice
import com.google.inject.Inject;

// Xtext / Xbase
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper;

// XtxtUML 
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider;

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLCompilerTest {
	
	@Inject extension CompilationTestHelper
	
	@Test
	def test() {
		'''
			model M {
			}
		'''.assertCompilesTo('''
			import hu.elte.txtuml.api.Model;

			@SuppressWarnings("all")
			public class M extends Model {
			}
		''')
	}
	
}