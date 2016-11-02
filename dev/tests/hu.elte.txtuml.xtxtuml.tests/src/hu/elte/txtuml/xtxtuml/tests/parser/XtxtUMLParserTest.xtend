package hu.elte.txtuml.xtxtuml.tests.parser;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLParserTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;

	@Test
	def void testModelDeclarationWithoutName() {
		'''model-package model.test;'''.parseAsModelInfo.assertNoErrors
	}

	@Test
	def void testModelDeclarationWithName() {
		'''model-package model.test as "Test";'''.parseAsModelInfo.assertNoErrors
	}

	private def parseAsModelInfo(CharSequence text) {
		text.parse(URI.createURI("model-info.xtxtuml"), new ResourceSetImpl)
	}
}
