package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.export.uml2.restructured.structural.AssociationEndExporter
import hu.elte.txtuml.export.uml2.restructured.structural.AssociationExporter
import hu.elte.txtuml.export.uml2.restructured.structural.ClassExporter
import hu.elte.txtuml.export.uml2.restructured.structural.FieldExporter
import hu.elte.txtuml.export.uml2.restructured.structural.OperationExporter
import hu.elte.txtuml.export.uml2.restructured.structural.PackageExporter
import hu.elte.txtuml.export.uml2.restructured.structural.ParameterExporter
import java.util.List
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Type
import hu.elte.txtuml.export.uml2.restructured.statemachine.StateExporter
import hu.elte.txtuml.export.uml2.restructured.statemachine.InitStateExporter
import hu.elte.txtuml.export.uml2.restructured.statemachine.TransitionExporter

abstract class Exporter<S, A, R extends Element> {

	Exporter<?, ?, ?> parent
	protected ExporterCache cache
	protected R result

	new() {
		cache = new ExporterCache
	}

	new(Exporter<?, ?, ?> parent) {
		this.parent = parent
		this.cache = parent.cache
	}

	abstract def R create(A access)

	def R createResult(A access) { result = create(access) }

	abstract def void exportContents(S source)

	def store() {
		if (parent != null) {
			parent.store(result)
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
	
	def void alreadyExists(R result) {
		this.result = result
	}

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
			ITypeBinding: #[new ClassExporter(this), new AssociationExporter(this), new AssociationEndExporter(this),
				new StateExporter(this), new InitStateExporter(this), new TransitionExporter(this)]
			IMethodBinding: #[new OperationExporter(this)]
			IVariableBinding: #[new FieldExporter(this), new ParameterExporter(this)]
			default: #[]
		}
	}
	
	def <CS, CA, CR extends Element> exportElement(CS source, CA access) {
		val exporters = getExporters(access);
		for (exporter : exporters) {
			val res = cache.export(exporter as Exporter<CS, CA, CR>, source, access)
			if (res != null) {
				return res;
			}
		}
		throw new IllegalArgumentException(access.toString)
	}

	def exportPackage(IPackageFragment pf) { cache.export(new PackageExporter(this), pf, pf) }

	def exportClass(TypeDeclaration td) { cache.export(new ClassExporter(this), td, td.resolveBinding) }

	def exportAssociation(TypeDeclaration td) { cache.export(new AssociationExporter(this), td, td.resolveBinding) }

	def exportAssociationEnd(TypeDeclaration td) {
		cache.export(new AssociationEndExporter(this), td, td.resolveBinding)
	}

	def exportField(IVariableBinding td) { cache.export(new FieldExporter(this), td, td) }

	def exportOperation(MethodDeclaration md) { cache.export(new OperationExporter(this), md, md.resolveBinding) }

	def exportParameter(IVariableBinding vb) { cache.export(new ParameterExporter(this), vb, vb) }
	
}