package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.api.model.Collection
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.export.uml2.restructured.activity.apicalls.AssocNavigationExporter
import hu.elte.txtuml.export.uml2.restructured.activity.apicalls.IgnoredAPICallExporter
import hu.elte.txtuml.export.uml2.restructured.activity.apicalls.LogActionExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.BinaryOperatorExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.BooleanLiteralExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.CharacterLiteralExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.MethodCallExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.NameFieldAccessExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.NullLiteralExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.NumberLiteralExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.ParenExpressionExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.PostfixOperatorExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.PrefixOperatorExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.PrefixPlusExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.SimpleFieldAccessExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.StringLiteralExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.SuperCallExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.ThisExporter
import hu.elte.txtuml.export.uml2.restructured.activity.expression.VariableExpressionExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.BlockExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.DoWhileExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.EmptyStmtExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ExpressionStatementExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.IfExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ReturnStatementExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.VariableDeclarationExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.WhileExporter
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
import java.util.List
import java.util.function.Consumer
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.BooleanLiteral
import org.eclipse.jdt.core.dom.CharacterLiteral
import org.eclipse.jdt.core.dom.DoStatement
import org.eclipse.jdt.core.dom.EmptyStatement
import org.eclipse.jdt.core.dom.EnhancedForStatement
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.IBinding
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Name
import org.eclipse.jdt.core.dom.NullLiteral
import org.eclipse.jdt.core.dom.NumberLiteral
import org.eclipse.jdt.core.dom.ParenthesizedExpression
import org.eclipse.jdt.core.dom.PostfixExpression
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.jdt.core.dom.ThisExpression
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.WhileStatement
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.ExecutableNode
import org.eclipse.uml2.uml.PrimitiveType
import org.eclipse.uml2.uml.Type
import hu.elte.txtuml.export.uml2.restructured.activity.expression.ParameterExpressionExporter
import hu.elte.txtuml.export.uml2.restructured.activity.statement.ForEachExporter

/** An exporter is able to fully or partially export a given element. 
 * Partial export only creates the UML object itself, while full export also creates its contents.
 * For partial generation we need the access key type, for partial generation we need the source type.
 * 
 * Exporting can return null if the exporter is not capable of exporting the given element (even if the 
 * type is correct)
 * 
 * @param S Source type
 * @param A Access key type
 * @param R Result type
 */
abstract class Exporter<S, A, R extends Element> extends BaseExporter<S, A, R> {

	/** Method calls to these classes will be treated specially */
	protected static val API_CLASSES = #{ModelClass.canonicalName, hu.elte.txtuml.api.model.Action.canonicalName,
		Collection.canonicalName}

	/** The parent exporter. Exporters form a tree to be able to place generated object in one of their parent exporter. */
	protected BaseExporter<?, ?, ?> parent

	/** The UML object resulting from export */
	protected R result

	/** Creates a root exporter with a new cache */
	new() {
		super(new ExporterCache)
	}

	/** Creates an exporter as a child of an existing one */
	new(BaseExporter<?, ?, ?> parent) {
		super(parent.cache)
		this.parent = parent
	}

	/** 
	 * Partially exports the element if the exporter is able to export the given element. 
	 * Returns null otherwise.
	 */
	abstract def R create(A access)

	/** Partially exports the element and sets the result. */
	def R createResult(A access) { result = create(access) }

	/** Perform full export for an element that was partially exported. */
	abstract def void exportContents(S source)

	/** Returns the UML factory for creating UML elements. */
	def getFactory() { cache.factory }

	/** This method should be called when the partially exported element is already found in the cache. */
	def void alreadyExists(R result) {
		this.result = result
	}

	/** 
	 * Gets the element if it was already exported (at least partially) or export the element partially, 
	 * automatically selecting the exporter that is able to export the element.
	 */
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

	/**
	 * Fetch an element with a specific exporter. This must be used when multiple exporters can export
	 * the given element.
	 */
	def <CA, CR extends Element> fetchElement(CA access, Exporter<?, CA, CR> exporter) {
		cache.fetch(exporter, access)
	}

	def fetchType(ITypeBinding typ) { fetchElement(typ) as Type }

	/** 
	 * Gets the possible exporters for a given access object. This method creates the possibility to
	 * automatically export a given element, by checking its type.
	 */
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
				#[new MethodCallExporter(this), new AssocNavigationExporter(this), new LogActionExporter(this),
					new IgnoredAPICallExporter(this)]
			SuperMethodInvocation:
				#[new SuperCallExporter(this)]
			StringLiteral:
				#[new StringLiteralExporter(this)]
			BooleanLiteral:
				#[new BooleanLiteralExporter(this)]
			CharacterLiteral:
				#[new CharacterLiteralExporter(this)]
			Name:
				#[new VariableExpressionExporter(this), new NameFieldAccessExporter(this),
					new ParameterExpressionExporter(this)]
			FieldAccess:
				#[new SimpleFieldAccessExporter(this)]
			NullLiteral:
				#[new NullLiteralExporter(this)]
			ThisExpression:
				#[new ThisExporter(this)]
			InfixExpression:
				#[new BinaryOperatorExporter(this)]
			PrefixExpression:
				#[new PrefixPlusExporter(this), new PrefixOperatorExporter(this)]
			PostfixExpression:
				#[new PostfixOperatorExporter(this)]
			NumberLiteral:
				#[new NumberLiteralExporter(this)]
			ParenthesizedExpression:
				#[new ParenExpressionExporter(this)]
			ExpressionStatement:
				#[new ExpressionStatementExporter(this)]
			ReturnStatement:
				#[new ReturnStatementExporter(this)]
			EmptyStatement:
				#[new EmptyStmtExporter(this)]
			IfStatement:
				#[new IfExporter(this)]
			WhileStatement:
				#[new WhileExporter(this)]
			DoStatement:
				#[new DoWhileExporter(this)]
			EnhancedForStatement:
				#[new ForEachExporter(this)]
			VariableDeclarationStatement:
				#[new VariableDeclarationExporter(this)]
			default:
				#[]
		}
	}

	/** Auto-exports the element as a statement (no pre-store) */
	def exportStatement(Statement source) {
		exportElement(source, source, []) as ExecutableNode
	}

	/** Auto-exports the element as an expression (no pre-store) */
	def exportExpression(Expression source) {
		exportElement(source, source, []) as Action
	}

	/**
	 * Fully exports the given element by selecting the exporter that is able to export it.
	 */
	def <CS, CA, CR extends Element> exportElement(CS source, CA access, Consumer<CR> store) {
		if(source == null) return null
		val exporters = getExporters(access);
		// if getExporters returns an exporter that doesn't have the correct type it will
		// be a cast exception here
		for (exporter : exporters) {
			val res = cache.export(exporter as Exporter<CS, CA, CR>, source, access, store)
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

	def getCollectionType() { getImportedElement("Collection") as Class }

	override def Element getImportedElement(String name) { parent.getImportedElement(name) }

	def Element getImportedElement(IBinding binding) {
		switch binding {
			ITypeBinding: getImportedElement(binding.erasure.name)
		}
	}

	def getImportedOperation(String clsName, String opName) {
		(getImportedElement(clsName) as Class).ownedOperations.findFirst[name == opName]
	}

}