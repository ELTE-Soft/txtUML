package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.export.uml2.restructured.activity.ActivityExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.BlockExporter
import hu.elte.txtuml.export.uml2.restructured.structural.AssociationEndExporter
import hu.elte.txtuml.export.uml2.restructured.structural.AssociationExporter
import hu.elte.txtuml.export.uml2.restructured.structural.ClassExporter
import hu.elte.txtuml.export.uml2.restructured.structural.FieldExporter
import hu.elte.txtuml.export.uml2.restructured.structural.MethodActivityExporter
import hu.elte.txtuml.export.uml2.restructured.structural.OperationExporter
import hu.elte.txtuml.export.uml2.restructured.structural.PackageExporter
import hu.elte.txtuml.export.uml2.restructured.structural.ParameterExporter
import hu.elte.txtuml.export.uml2.restructured.structural.SignalEventExporter
import hu.elte.txtuml.export.uml2.restructured.structural.SignalExporter
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.Property
import java.util.function.Consumer
import org.eclipse.uml2.uml.Signal
import org.eclipse.uml2.uml.SignalEvent
import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.Constraint
import hu.elte.txtuml.export.uml2.restructured.statemachine.GuardExporter

/**
 * Base class for exporters, methods to export different kinds of elements using specific exporters.
 */
abstract class BaseExporter<S, A, R extends Element> {

	/** Reference to the cache shared between all exporters */
	protected ExporterCache cache

	new(ExporterCache cache) {
		this.cache = cache
	}

	abstract def Element getImportedElement(String name)

	def exportPackage(IPackageFragment pf, Consumer<Package> store) {
		cache.export(new PackageExporter(this), pf, pf, store)
	}

	def exportClass(Package pkg, TypeDeclaration td, Consumer<Class> store) {
		cache.export(new ClassExporter(this), td, td.resolveBinding, store)
	}

	def exportSignal(TypeDeclaration td, Consumer<Signal> store) {
		cache.export(new SignalExporter(this), td, td.resolveBinding, store)
	}

	def exportSignalEvent(TypeDeclaration td, Consumer<SignalEvent> store) {
		cache.export(new SignalEventExporter(this), td, td.resolveBinding, store)
	}

	def exportAssociation(TypeDeclaration td, Consumer<Association> store) {
		cache.export(new AssociationExporter(this), td, td.resolveBinding, store)
	}

	def exportAssociationEnd(TypeDeclaration td, Consumer<Property> store) {
		cache.export(new AssociationEndExporter(this), td, td.resolveBinding, store)
	}

	def exportField(IVariableBinding td, Consumer<Property> store) {
		cache.export(new FieldExporter(this), td, td, store)
	}

	def exportOperation(MethodDeclaration md, Consumer<Operation> store) {
		cache.export(new OperationExporter(this), md, md.resolveBinding, store)
	}

	def exportActivity(MethodDeclaration md, Consumer<Activity> store) {
		cache.export(new MethodActivityExporter(this), md, md.resolveBinding, store)
	}
	
	def exportGuard(MethodDeclaration md, Consumer<Constraint> store) {
		cache.export(new GuardExporter(this), md, md.resolveBinding, store)
	}

	def exportActivity(Block blk, Activity act) {
		val exporter = new ActivityExporter(this)
		exporter.alreadyExists(act)
		exporter.exportContents(blk)
	}

	def exportBlock(Block blk, Consumer<SequenceNode> store) { cache.export(new BlockExporter(this), blk, blk, store) }

	def exportParameter(IVariableBinding vb) { cache.export(new ParameterExporter(this), vb, vb, []) }

}
