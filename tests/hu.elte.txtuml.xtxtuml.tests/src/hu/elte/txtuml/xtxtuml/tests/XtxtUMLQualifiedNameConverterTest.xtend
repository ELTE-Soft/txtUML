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

	@Rule
	public Timeout globalTimeout = new Timeout(250, TimeUnit.MILLISECONDS);

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

	@Test
	def void testPerformanceWithLongMixedNames() {
		// 1e5 calls under 250ms => 25us for each call at maximum
		for (i : 0 ..< 50000) {
			toQualifiedName("hu::elte.txtuml::xtxtulm.tests")
			toQualifiedName("hu.elte::txtuml.xtxtulm::tests")
		}
	}
}
