package hu.elte.txtuml.xtxtuml.tests

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLCompilerTest {

	@Inject extension CompilationTestHelper

	@Test
	def testModelDeclarationCompilesToProperPackageInfo() {
		'''
			model-package model.declaration.test as "Test";
		'''.assertCompilesTo('''
			@Model("Test")
			package model.declaration.test;
			
			import hu.elte.txtuml.api.model.Model;
		''')
	}
}
