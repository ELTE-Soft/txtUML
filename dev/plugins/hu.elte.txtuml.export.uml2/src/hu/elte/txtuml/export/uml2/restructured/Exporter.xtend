package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.api.model.Collection
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.export.uml2.restructured.activity.ActivityExporter
import hu.elte.txtuml.export.uml2.restructured.activity.apicalls.AssocNavigationExporter
import hu.elte.txtuml.export.uml2.restructured.activity.apicalls.IgnoredAPICallExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.CallExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.StringLiteralExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.BlockExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ExpressionStatementExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.VariableDeclarationExporter
import hu.elte.txtuml.export.uml2.restructured.statemachine.InitStateExporter
import hu.elte.txtuml.export.uml2.restructured.statemachine.StateExporter
import hu.elte.txtuml.export.uml2.restructured.statemachine.TransitionExporter
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
import java.util.List
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.IBinding
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.ExecutableNode
import org.eclipse.uml2.uml.PrimitiveType
import org.eclipse.uml2.uml.Type

abstract class Exporter<S, A, R extends Element> {

	protected static val API_CLASSES = #{ModelClass.canonicalName, hu.elte.txtuml.api.model.Action.canonicalName,
		Collection.canonicalName}

	protected Exporter<?, ?, ?> parent
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

	def getFactory() { cache.factory }

	def void alreadyExists(R result) {
		this.result = result
	}

	def <CA, CR extends Element> fetchElement(CA access) {
		val imported = if(access instanceof IBinding) getImportedElement(access)
		if (imported != null) {
			return imported
		}
		val exporters = getExporters(access);
		for (exporter : exporters) {
			val res = fetchElement(access, exporter as Exporter<?, CA, CR>)
			if (res != null) {
				return res
			}
		}
		throw new IllegalArgumentException(access.toString)
	}

	def <CA, CR extends Element> fetchElement(CA access, Exporter<?, CA, CR> exporter) {
		cache.fetch(exporter, access)
	}

	def fetchType(ITypeBinding typ) { fetchElement(typ) as Type }

	def List<Exporter<?, ?, ?>> getExporters(Object obj) {
		switch obj {
			IPackageFragment:
				#[new PackageExporter(this)]
			ITypeBinding:
				#[new ClassExporter(this), new AssociationExporter(this), new AssociationEndExporter(this),
					new StateExporter(this), new InitStateExporter(this), new TransitionExporter(this)]
			IMethodBinding:
				#[new OperationExporter(this), new MethodActivityExporter(this)]
			IVariableBinding:
				#[new FieldExporter(this), new ParameterExporter(this)]
			Block:
				#[new BlockExporter(this)]
			MethodInvocation:
				#[new CallExporter(this), new AssocNavigationExporter(this), new IgnoredAPICallExporter(this)]
			StringLiteral:
				#[new StringLiteralExporter(this)]
			ExpressionStatement:
				#[new ExpressionStatementExporter(this)]
			VariableDeclarationStatement:
				#[new VariableDeclarationExporter(this)]
			default:
				#[]
		}
	}

	def exportStatement(Statement source) {
		exportElement(source, source) as ExecutableNode
	}

	def exportExpression(Expression source) {
		exportElement(source, source) as Action
	}

	def <CS, CA, CR extends Element> exportElement(CS source, CA access) {
		if(source == null) return null
		val exporters = getExporters(access);
		for (exporter : exporters) {
			val res = cache.export(exporter as Exporter<CS, CA, CR>, source, access)
			if (res != null) {
				return res;
			}
		}
		throw new IllegalArgumentException(access.toString)
	}

	def getBooleanType() { getImportedElement("Boolean") as PrimitiveType }

	def getIntegerType() { getImportedElement("Integer") as PrimitiveType }

	def getStringType() { getImportedElement("String") as PrimitiveType }

	def getRealType() { getImportedElement("Real") as PrimitiveType }

	def getUnlimitedNaturalType() { getImportedElement("UnlimitedNatural") as PrimitiveType }

	def Element getImportedElement(String name) { parent.getImportedElement(name) }

	def Element getImportedElement(IBinding binding) {
		switch binding {
			ITypeBinding: getImportedElement(binding.qualifiedName)
		}
	}

	def exportPackage(IPackageFragment pf) { cache.export(new PackageExporter(this), pf, pf) }

	def exportClass(TypeDeclaration td) { cache.export(new ClassExporter(this), td, td.resolveBinding) }

	def exportSignal(TypeDeclaration td) { cache.export(new SignalExporter(this), td, td.resolveBinding) }

	def exportSignalEvent(TypeDeclaration td) { cache.export(new SignalEventExporter(this), td, td.resolveBinding) }

	def exportAssociation(TypeDeclaration td) { cache.export(new AssociationExporter(this), td, td.resolveBinding) }

	def exportAssociationEnd(TypeDeclaration td) {
		cache.export(new AssociationEndExporter(this), td, td.resolveBinding)
	}

	def exportField(IVariableBinding td) { cache.export(new FieldExporter(this), td, td) }

	def exportOperation(MethodDeclaration md) { cache.export(new OperationExporter(this), md, md.resolveBinding) }

	def exportActivity(MethodDeclaration md) { cache.export(new MethodActivityExporter(this), md, md.resolveBinding) }

	def exportActivity(Block blk, Activity act) {
		val exporter = new ActivityExporter(this)
		exporter.alreadyExists(act)
		exporter.exportContents(blk)
	}

	def exportBlock(Block blk) { cache.export(new BlockExporter(this), blk, blk) }

	def exportParameter(IVariableBinding vb) { cache.export(new ParameterExporter(this), vb, vb) }

}