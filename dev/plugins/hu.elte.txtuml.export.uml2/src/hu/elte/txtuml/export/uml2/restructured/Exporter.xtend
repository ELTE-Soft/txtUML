package hu.elte.txtuml.export.uml2.restructured

import org.eclipse.uml2.uml.Element
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.TypeDeclaration

abstract class Exporter<S, R extends Element> {

	Exporter<?, ?> parent
	ExporterCache cache
	protected R result

	new() {
		cache = new ExporterCache
		this.result = create
	}

	new(Exporter<?,?> parent) {
		this.parent = parent
		this.cache = parent.cache
		this.result = create
		parent.store(result)
	}

	abstract def R create()

	abstract def void exportContents(S s)

	def R export(S source) {
		exportContents(source)
		return result
	}

	def void store(Element contained) {
		if (!tryStore(contained)) {
			parent.store(contained)
		}
	}

	abstract def boolean tryStore(Element contained)
	
	def getFactory() { cache.factory }
	
	def exportPackage(IPackageFragment pf) { cache.export(new PackageExporter(this), pf) }
	def exportClass(TypeDeclaration td) { cache.export(new ClassExporter(this), td) }
	
}