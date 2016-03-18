package hu.elte.txtuml.export.uml2.restructured

import java.util.HashMap
import java.util.List
import java.util.Map
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.UMLFactory

abstract class Exporter<S, R extends Element> {

	protected ExporterRegistry registry

	val Map<S, R> cache = new HashMap();
	protected val factory = UMLFactory.eINSTANCE

	Class<R> cls

	new(ExporterRegistry exporterRegistry, Class<R> cls) {
		this.cls = cls
		this.registry = exporterRegistry
	}

	def R export(List<Element> stack, S source) {
		if (cache.containsKey(source)) {
			return cache.get(source);
		} else {
			val r = create();
			r.exportContents(source)
			store(stack, r);
			return r
		}
	}

	abstract def R create();

	abstract def void exportContents(R r, S s);

	def void store(List<Element> stack, Element contained) {
		val container = stack.last
		val exporter = registry.getSourceExporter(container)
		if (!exporter.tryStoreCast(container, contained)) {
			exporter.store(stack.subList(0, stack.length - 2), contained)
		}
	}

	def tryStoreCast(Element container, Element contained) {
		tryStore(cls.cast(container), contained)
	}

	abstract def boolean tryStore(R container, Element contained);
	
	def getModelExporter() { registry.modelExporter }
	def getPackageExporter() { registry.packageExporter }
	def getClassExporter() { registry.classExporter }

}