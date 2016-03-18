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

	abstract def R create(S s)
	abstract def R createFetched(A a)

	abstract def void exportContents(S s)
	
	def R fetch(A source) { createFetched(source) }

	def R export(S source) {
		this.result = create(source)
		if (result != null) {
			exportContents(source)
			if (parent != null) {
				parent.store(result)
			}
		}
		return result
	}
	
	private def R fetchGen(Object obj) { fetch(obj as A) }

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

	def fetchElement(Object obj) {
		val exporters = getExporters(obj);
		for (exporter : exporters) {
			val res = exporter.fetchGen(obj);
			if (res != null) {
				return res;
			}
		}
		throw new IllegalArgumentException(obj.toString)
	}
	
	def fetchType(ITypeBinding typ) {
		fetchElement(typ) as Type
	}

	def List<Exporter<?,?,?>> getExporters(Object obj) {
		switch obj {
			IPackageFragment: #[new PackageExporter(this)]
			ITypeBinding: #[new ClassExporter(this)]
			IMethodBinding: #[new OperationExporter(this)]
			IVariableBinding: #[new FieldExporter(this), new ParameterExporter(this)]
			default: #[]
		}
	}

	def exportPackage(IPackageFragment pf) { cache.export(new PackageExporter(this), pf) }

	def exportClass(TypeDeclaration td) { cache.export(new ClassExporter(this), td) }

	def exportField(IVariableBinding td) { cache.export(new FieldExporter(this), td) }
	
	def exportOperation(MethodDeclaration md) { cache.export(new OperationExporter(this), md) }
	
	def exportParameter(IVariableBinding vb) { cache.export(new ParameterExporter(this), vb) }
}