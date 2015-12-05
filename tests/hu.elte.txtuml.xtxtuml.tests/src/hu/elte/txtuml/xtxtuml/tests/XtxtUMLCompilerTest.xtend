package hu.elte.txtuml.xtxtuml.tests

import com.google.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(CustomXtxtUMLInjectorProvider)
class XtxtUMLCompilerTest {

	@Inject extension CompilationTestHelper

	@Test
	def testModelDeclarationCompilesToProperPackageInfo() {
		PackageNameCalculatorStubProvider.setPackageName("model", "declaration", "test")

		'''
			model "Test";
		'''.assertCompilesTo('''
			@Model("Test")
			package model.declaration.test;
			
			import hu.elte.txtuml.api.model.Model;
		''')
	}
}
