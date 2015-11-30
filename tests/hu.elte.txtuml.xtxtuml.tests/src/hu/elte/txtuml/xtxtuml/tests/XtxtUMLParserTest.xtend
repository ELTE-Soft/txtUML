package hu.elte.txtuml.xtxtuml.tests;

// Junit
import org.junit.Test;
import org.junit.runner.RunWith;

// Guice
import com.google.inject.Inject;

// Xtext
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;

// XtxtUML 
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile;
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider;

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLParserTest {
	
	// Helper extensions
	
	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;
	
	// Test methods
	
	@Test
	def testEmptyModel() {
		'''	
			model M {}
		'''.parse.assertNoErrors;
	}
	
}
