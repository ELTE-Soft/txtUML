package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.export.uml2.restructured.structural.ClassExporter
import hu.elte.txtuml.export.uml2.restructured.structural.FieldExporter
import hu.elte.txtuml.export.uml2.restructured.structural.PackageExporter
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Type
import hu.elte.txtuml.export.uml2.restructured.structural.OperationExporter
import org.eclipse.jdt.core.dom.IMethodBinding
import hu.elte.txtuml.export.uml2.restructured.structural.ParameterExporter
import java.util.List
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.MethodDeclaration

abstract class Exporter<S, A, R extends Element> {

	Exporter<?, ?, ?> parent
	ExporterCache cache
	protected R result

	new() {
		cache = new ExporterCache
	}

	new(Exporter<?, ?, ?> parent) {
		this.parent = parent
		this.cache = parent.cache
	}

	abstract def R create(A a)

	abstract def void exportContents(S s)

	def R export(S source, A access) {
		this.result = create(access)
		if (result != null) {
			exportContents(source)
			if (parent != null) {
				parent.store(result)
			}
		}
		return result
	}

	def exportExisting(S source, R existing) {
		this.result = existing
		exportContents(source)
		if (parent != null) {
			parent.store(existing)
		}
	}

	private def void store(Element contained) {
		if (!tryStore(contained)) {
			if (parent == null) {
				throw new IllegalStateException("Could not store element: " + contained)
			}
			parent.store(contained)
		}
	}

	abstract def boolean tryStore(Element contained)

	def getFactory() { cache.factory }

	def <CA, CR extends Element> fetchElement(CA access) {
		val exporters = getExporters(access);
		for (exporter : exporters) {
			val res = cache.fetch(exporter as Exporter<?, CA, CR>, access)
			if (res != null) {
				return res;
			}
		}
		throw new IllegalArgumentException(access.toString)
	}

	def fetchType(ITypeBinding typ) { fetchElement(typ) as Type }

	def List<Exporter<?, ?, ?>> getExporters(Object obj) {
		switch obj {
			IPackageFragment: #[new PackageExporter(this)]
			ITypeBinding: #[new ClassExporter(this)]
			IMethodBinding: #[new OperationExporter(this)]
			IVariableBinding: #[new FieldExporter(this), new ParameterExporter(this)]
			default: #[]
		}
	}

	def exportPackage(IPackageFragment pf) { cache.export(new PackageExporter(this), pf, pf) }

	def exportClass(TypeDeclaration td) { cache.export(new ClassExporter(this), td, td.resolveBinding) }

	def exportField(IVariableBinding td) { cache.export(new FieldExporter(this), td, td) }

	def exportOperation(MethodDeclaration md) { cache.export(new OperationExporter(this), md, md.resolveBinding) }

	def exportParameter(IVariableBinding vb) { cache.export(new ParameterExporter(this), vb, vb) }

}