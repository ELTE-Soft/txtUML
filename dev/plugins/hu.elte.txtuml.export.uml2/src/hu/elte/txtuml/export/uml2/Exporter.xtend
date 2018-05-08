package hu.elte.txtuml.export.uml2

import hu.elte.txtuml.export.uml2.activity.MethodActivityExporter
import hu.elte.txtuml.export.uml2.activity.SMActivityExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.AutoboxExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.CountExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.CreateActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.CreateLinkActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.DeleteActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.EqualsCallExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.GetSignalExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.IgnoredAPICallExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.LogActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.PrimitiveToStringExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.ReadLinkActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.SelectionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.SendActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.StartActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.ToStringExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.UnlinkActionExporter
import hu.elte.txtuml.export.uml2.activity.expression.APISuperCtorCallExporter
import hu.elte.txtuml.export.uml2.activity.expression.AssignExporter
import hu.elte.txtuml.export.uml2.activity.expression.BinaryOperatorExporter
import hu.elte.txtuml.export.uml2.activity.expression.BooleanLiteralExporter
import hu.elte.txtuml.export.uml2.activity.expression.CharacterLiteralExporter
import hu.elte.txtuml.export.uml2.activity.expression.ConstructorCallExporter
import hu.elte.txtuml.export.uml2.activity.expression.EqualityExporter
import hu.elte.txtuml.export.uml2.activity.expression.MethodCallExporter
import hu.elte.txtuml.export.uml2.activity.expression.NameFieldAccessExporter
import hu.elte.txtuml.export.uml2.activity.expression.NullLiteralExporter
import hu.elte.txtuml.export.uml2.activity.expression.NumberLiteralExporter
import hu.elte.txtuml.export.uml2.activity.expression.OtherCtorCallExporter
import hu.elte.txtuml.export.uml2.activity.expression.ParenExpressionExporter
import hu.elte.txtuml.export.uml2.activity.expression.PostfixOperatorExporter
import hu.elte.txtuml.export.uml2.activity.expression.PrefixOperatorExporter
import hu.elte.txtuml.export.uml2.activity.expression.PrefixPlusExporter
import hu.elte.txtuml.export.uml2.activity.expression.SimpleFieldAccessExporter
import hu.elte.txtuml.export.uml2.activity.expression.StringLiteralExporter
import hu.elte.txtuml.export.uml2.activity.expression.SuperCallExporter
import hu.elte.txtuml.export.uml2.activity.expression.SuperCtorCallExporter
import hu.elte.txtuml.export.uml2.activity.expression.ThisExporter
import hu.elte.txtuml.export.uml2.activity.expression.UnequalityExporter
import hu.elte.txtuml.export.uml2.activity.expression.VariableDeclarationExpressionExporter
import hu.elte.txtuml.export.uml2.activity.expression.VariableExpressionExporter
import hu.elte.txtuml.export.uml2.activity.statement.BlockExporter
import hu.elte.txtuml.export.uml2.activity.statement.DoWhileExporter
import hu.elte.txtuml.export.uml2.activity.statement.EmptyStmtExporter
import hu.elte.txtuml.export.uml2.activity.statement.ExpressionStatementExporter
import hu.elte.txtuml.export.uml2.activity.statement.ForEachExporter
import hu.elte.txtuml.export.uml2.activity.statement.ForLoopExporter
import hu.elte.txtuml.export.uml2.activity.statement.IfExporter
import hu.elte.txtuml.export.uml2.activity.statement.ReturnStatementExporter
import hu.elte.txtuml.export.uml2.activity.statement.VariableDeclarationExporter
import hu.elte.txtuml.export.uml2.activity.statement.WhileExporter
import hu.elte.txtuml.export.uml2.statemachine.ChoiceStateExporter
import hu.elte.txtuml.export.uml2.statemachine.InitStateExporter
import hu.elte.txtuml.export.uml2.statemachine.StateExporter
import hu.elte.txtuml.export.uml2.statemachine.TransitionExporter
import hu.elte.txtuml.export.uml2.stdlib.StdlibCallExporter
import hu.elte.txtuml.export.uml2.stdlib.StdlibClassExporter
import hu.elte.txtuml.export.uml2.structural.AssociationEndExporter
import hu.elte.txtuml.export.uml2.structural.AssociationExporter
import hu.elte.txtuml.export.uml2.structural.ClassExporter
import hu.elte.txtuml.export.uml2.structural.ConnectorEndExporter
import hu.elte.txtuml.export.uml2.structural.ConnectorExporter
import hu.elte.txtuml.export.uml2.structural.DataTypeExporter
import hu.elte.txtuml.export.uml2.structural.DefaultConstructorBodyExporter
import hu.elte.txtuml.export.uml2.structural.DefaultConstructorExporter
import hu.elte.txtuml.export.uml2.structural.FieldExporter
import hu.elte.txtuml.export.uml2.structural.InPortExporter
import hu.elte.txtuml.export.uml2.structural.InterfaceExporter
import hu.elte.txtuml.export.uml2.structural.OperationExporter
import hu.elte.txtuml.export.uml2.structural.OutPortExporter
import hu.elte.txtuml.export.uml2.structural.PackageExporter
import hu.elte.txtuml.export.uml2.structural.ParameterExporter
import hu.elte.txtuml.export.uml2.structural.PortExporter
import hu.elte.txtuml.export.uml2.structural.SignalExporter
import java.util.List
import java.util.function.Consumer
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.BooleanLiteral
import org.eclipse.jdt.core.dom.CharacterLiteral
import org.eclipse.jdt.core.dom.ClassInstanceCreation
import org.eclipse.jdt.core.dom.ConstructorInvocation
import org.eclipse.jdt.core.dom.DoStatement
import org.eclipse.jdt.core.dom.EmptyStatement
import org.eclipse.jdt.core.dom.EnhancedForStatement
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.jdt.core.dom.IBinding
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.Name
import org.eclipse.jdt.core.dom.NullLiteral
import org.eclipse.jdt.core.dom.NumberLiteral
import org.eclipse.jdt.core.dom.ParenthesizedExpression
import org.eclipse.jdt.core.dom.PostfixExpression
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.jdt.core.dom.SuperConstructorInvocation
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.jdt.core.dom.ThisExpression
import org.eclipse.jdt.core.dom.VariableDeclarationExpression
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.WhileStatement
import org.eclipse.uml2.uml.Action
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.ExecutableNode
import org.eclipse.uml2.uml.PackageableElement
import org.eclipse.uml2.uml.PrimitiveType
import org.eclipse.uml2.uml.Type
import org.eclipse.uml2.uml.VisibilityKind
import hu.elte.txtuml.export.uml2.activity.expression.PortReferenceExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.SendToPortActionExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.AssemblyConnectExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.DelegationConnectExporter
import hu.elte.txtuml.export.uml2.activity.apicalls.AddToMultipliedElementExporter

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

	/** The parent exporter. Exporters form a tree to be able to place generated object in one of their parent exporter. */
	protected BaseExporter<?, ?, ?> parent

	/** The UML object resulting from export */
	public R result

	/** Creates a root exporter with a new cache */
	new(ExportMode mode) {
		super(new ExporterCache(mode))
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

	def boolean exportActions() {
		cache.exportMode.exportActions();
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
		cache.exportMode.handleErrors[throw new IllegalArgumentException(access.toString)]
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
				#[new StdlibClassExporter(this), new ClassExporter(this), new AssociationExporter(this),
					new AssociationEndExporter(this), new StateExporter(this), new InitStateExporter(this),
					new ChoiceStateExporter(this), new DataTypeExporter(this), new TransitionExporter(this),
					new SignalExporter(this), new InPortExporter(this), new OutPortExporter(this),
					new PortExporter(this), new InterfaceExporter(this), new ConnectorExporter(this),
					new ConnectorEndExporter(this)]
			IMethodBinding:
				#[new DefaultConstructorExporter(this), new DefaultConstructorBodyExporter(this),
					new StdlibCallExporter(this), new OperationExporter(this), new MethodActivityExporter(this),
					new SMActivityExporter(this)]
			IVariableBinding:
				#[new FieldExporter(this), new ParameterExporter(this)]
			Block:
				#[new BlockExporter(this)]
			MethodInvocation:
				#[
					new ToStringExporter(this),
					new EqualsCallExporter(this),
					new PrimitiveToStringExporter(this),
					new AutoboxExporter(this),
					new MethodCallExporter(this),
					new ReadLinkActionExporter(this),
					new LogActionExporter(this),
					new CreateLinkActionExporter(this),
					new SendActionExporter(this),
					new SendToPortActionExporter(this),
					new UnlinkActionExporter(this),
					new CreateActionExporter(this),
					new DeleteActionExporter(this),
					new StartActionExporter(this),
					new SelectionExporter(this),
					new CountExporter(this),
					new GetSignalExporter(this),
					new PortReferenceExporter(this),
					new AssemblyConnectExporter(this),
					new DelegationConnectExporter(this),
					new AddToMultipliedElementExporter(this),
					new IgnoredAPICallExporter(this)
				]
			ConstructorInvocation:
				#[new OtherCtorCallExporter(this)]
			SuperConstructorInvocation:
				#[new SuperCtorCallExporter(this), new APISuperCtorCallExporter(this)]
			ClassInstanceCreation:
				#[new ConstructorCallExporter(this)]
			SuperMethodInvocation:
				#[new SuperCallExporter(this)]
			StringLiteral:
				#[new StringLiteralExporter(this)]
			BooleanLiteral:
				#[new BooleanLiteralExporter(this)]
			CharacterLiteral:
				#[new CharacterLiteralExporter(this)]
			VariableDeclarationExpression:
				#[new VariableDeclarationExpressionExporter(this)]
			Name:
				#[new VariableExpressionExporter(this), new NameFieldAccessExporter(this)]
			FieldAccess:
				#[new SimpleFieldAccessExporter(this)]
			NullLiteral:
				#[new NullLiteralExporter(this)]
			ThisExpression:
				#[new ThisExporter(this)]
			InfixExpression:
				#[new EqualityExporter(this), new UnequalityExporter(this), new BinaryOperatorExporter(this)]
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
			ForStatement:
				#[new ForLoopExporter(this)]
			DoStatement:
				#[new DoWhileExporter(this)]
			EnhancedForStatement:
				#[new ForEachExporter(this)]
			Assignment:
				#[new AssignExporter(this)]
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

	def void storePackaged(PackageableElement pkg) {
		if(parent instanceof Exporter<?, ?, ?>) parent.storePackaged(pkg)
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
		cache.exportMode.handleErrors[throw new IllegalArgumentException(access.toString)]
	}

	def getBooleanType() { getImportedElement("Boolean") as PrimitiveType }

	def getIntegerType() { getImportedElement("Integer") as PrimitiveType }

	def getStringType() { getImportedElement("String") as PrimitiveType }

	def getRealType() { getImportedElement("Real") as PrimitiveType }

	def getUnlimitedNaturalType() { getImportedElement("UnlimitedNatural") as PrimitiveType }

	def getCollectionType() { getImportedElement("GeneralCollection") as Class }

	override def Element getImportedElement(String name) { parent.getImportedElement(name) }

	def Element getImportedElement(IBinding binding) {
		switch binding {
			ITypeBinding: getImportedElement(binding.erasure.qualifiedName)
		}
	}

	def getImportedOperation(String clsName, String opName) {
		(getImportedElement(clsName) as Class).ownedOperations.findFirst[name == opName]
	}
	
	def VisibilityKind getVisibility(int modifier) {
		if(Modifier.isPublic(modifier)) {
			VisibilityKind.PUBLIC_LITERAL
		} else if(Modifier.isProtected(modifier)) {
			VisibilityKind.PROTECTED_LITERAL
		} else if(Modifier.isPrivate(modifier)) {
			VisibilityKind.PRIVATE_LITERAL
		}
	}

}