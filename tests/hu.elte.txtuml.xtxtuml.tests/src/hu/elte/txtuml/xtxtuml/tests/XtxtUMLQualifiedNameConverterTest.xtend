package hu.elte.txtuml.xtxtuml.tests

import com.google.inject.Inject
import java.util.concurrent.TimeUnit
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.QualifiedName
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(CustomXtxtUMLInjectorProvider)
class XtxtUMLQualifiedNameConverterTest {

	@Inject extension IQualifiedNameConverter

	@Test
	def void testSingleSegmentName() {
		assertEquals(QualifiedName.create("abc"), toQualifiedName("abc"))
	}

	@Test
	def void testDotSeparatedName() {
		assertEquals(QualifiedName.create("a", "b", "c"), toQualifiedName("a.b.c"))
	}

	@Test
	def void testDoubleColonSeparatedName() {
		assertEquals(QualifiedName.create("a", "b", "c"), toQualifiedName("a::b::c"))
	}

	@Test
	def void testMixedNames() {
		assertEquals(QualifiedName.create("a", "b", "c"), toQualifiedName("a::b.c"))
		assertEquals(QualifiedName.create("a", "b", "c"), toQualifiedName("a.b::c"))
	}
}
