package hu.elte.txtuml.export.uml2

import hu.elte.txtuml.export.uml2.activity.ActivityExporter
import hu.elte.txtuml.export.uml2.activity.MethodActivityExporter
import hu.elte.txtuml.export.uml2.activity.SMActivityExporter
import hu.elte.txtuml.export.uml2.activity.statement.BlockExporter
import hu.elte.txtuml.export.uml2.statemachine.GuardExporter
import hu.elte.txtuml.export.uml2.structural.AssociationEndExporter
import hu.elte.txtuml.export.uml2.structural.AssociationExporter
import hu.elte.txtuml.export.uml2.structural.ClassExporter
import hu.elte.txtuml.export.uml2.structural.ConnectorEndExporter
import hu.elte.txtuml.export.uml2.structural.ConnectorExporter
import hu.elte.txtuml.export.uml2.structural.DataTypeExporter
import hu.elte.txtuml.export.uml2.structural.DefaultConstructorBodyExporter
import hu.elte.txtuml.export.uml2.structural.DefaultConstructorExporter
import hu.elte.txtuml.export.uml2.structural.FieldExporter
import hu.elte.txtuml.export.uml2.structural.InterfaceExporter
import hu.elte.txtuml.export.uml2.structural.OperationExporter
import hu.elte.txtuml.export.uml2.structural.PackageExporter
import hu.elte.txtuml.export.uml2.structural.ParameterExporter
import hu.elte.txtuml.export.uml2.structural.PortExporter
import hu.elte.txtuml.export.uml2.structural.SignalEventExporter
import hu.elte.txtuml.export.uml2.structural.SignalExporter
import hu.elte.txtuml.export.uml2.structural.SignalFactoryExporter
import java.util.function.Consumer
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Connector
import org.eclipse.uml2.uml.ConnectorEnd
import org.eclipse.uml2.uml.Constraint
import org.eclipse.uml2.uml.DataType
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Interface
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.Port
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.SequenceNode
import org.eclipse.uml2.uml.Signal
import org.eclipse.uml2.uml.SignalEvent
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.export.uml2.structural.OutPortExporter
import hu.elte.txtuml.export.uml2.structural.InPortExporter
//import hu.elte.txtuml.api.model.external.ExternalClass
import hu.elte.txtuml.export.uml2.structural.ExternalClassExporter

/**
 * Base class for exporters, methods to export different kinds of elements using specific exporters.
 * 
 * Exporters can be used by general and specific methods. General methods auto-select the exporter
 * for exporting the given element based on the type of the element.
 * 
 * Structural nodes must be stored before the exportContents method is called, because some features are
 * dependent on the position of the node in the model. Actions on the other hand are stored after the 
 * exportContents method call, because the subexpressions must be placed in the sequence nodes first.
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

	def exportClass(TypeDeclaration td, Consumer<Class> store) {
		cache.export(new ClassExporter(this), td, td.resolveBinding, store)
	}

	def exportInterface(TypeDeclaration td, Consumer<Interface> store) {
		cache.export(new InterfaceExporter(this), td, td.resolveBinding, store)
	}
	
	def exportExternalClass(TypeDeclaration td, Consumer<Interface> store) {
		cache.export(new ExternalClassExporter(this), td, td.resolveBinding, store)
	}

	def exportDataType(TypeDeclaration td, Consumer<DataType> store) {
		cache.export(new DataTypeExporter(this), td, td.resolveBinding, store)
	}

	def exportSignal(TypeDeclaration td, Consumer<Signal> store) {
		cache.export(new SignalExporter(this), td, td.resolveBinding, store)
	}

	def exportSignalEvent(TypeDeclaration td, Consumer<SignalEvent> store) {
		cache.export(new SignalEventExporter(this), td, td.resolveBinding, store)
	}

	def exportSignalFactory(TypeDeclaration td, Consumer<Class> store) {
		cache.export(new SignalFactoryExporter(this), td, td.resolveBinding, store)
	}

	def exportAssociation(TypeDeclaration td, Consumer<Association> store) {
		cache.export(new AssociationExporter(this), td, td.resolveBinding, store)
	}

	def exportAssociationEnd(TypeDeclaration td, Consumer<Property> store) {
		cache.export(new AssociationEndExporter(this), td, td.resolveBinding, store)
	}

	def exportPort(TypeDeclaration td, Consumer<Port> store) {
		if (ElementTypeTeller.isInPort(td.resolveBinding)) {
			cache.export(new InPortExporter(this), td, td.resolveBinding, store)
		} else if (ElementTypeTeller.isOutPort(td.resolveBinding)) {
			cache.export(new OutPortExporter(this), td, td.resolveBinding, store)
		} else {
			cache.export(new PortExporter(this), td, td.resolveBinding, store)
		}
	}

	def exportConnector(TypeDeclaration td, Consumer<Connector> store) {
		cache.export(new ConnectorExporter(this), td, td.resolveBinding, store)
	}

	def exportConnectorEnd(TypeDeclaration td, Consumer<ConnectorEnd> store) {
		cache.export(new ConnectorEndExporter(this), td, td.resolveBinding, store)
	}

	def exportField(IVariableBinding td, Consumer<Property> store) {
		cache.export(new FieldExporter(this), td, td, store)
	}

	def exportOperation(MethodDeclaration md, Consumer<Operation> store) {
		cache.export(new OperationExporter(this), md, md.resolveBinding, store)
	}

	def exportDefaultConstructor(IMethodBinding bnd, Consumer<Operation> store) {
		cache.export(new DefaultConstructorExporter(this), bnd, bnd, store)
	}

	def exportDefaultConstructorBody(IMethodBinding bnd, Consumer<Activity> store) {
		cache.export(new DefaultConstructorBodyExporter(this), bnd, bnd, store)
	}

	def exportActivity(MethodDeclaration md, Consumer<Activity> store) {
		cache.export(new MethodActivityExporter(this), md, md.resolveBinding, store)
	}

	def exportSMActivity(MethodDeclaration md, Consumer<Activity> store) {
		cache.export(new SMActivityExporter(this), md, md.resolveBinding, store)
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
