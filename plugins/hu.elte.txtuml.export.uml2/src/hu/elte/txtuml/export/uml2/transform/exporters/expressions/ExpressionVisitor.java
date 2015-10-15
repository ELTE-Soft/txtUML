package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import hu.elte.txtuml.export.uml2.transform.exporters.TypeExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.actions.CreateObjectActionExporter;
import hu.elte.txtuml.utils.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralNull;
import org.eclipse.uml2.uml.LiteralReal;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * TODO ExpressionVisitor
 * <p>
 * {@link org.eclipse.jdt.core.dom.Annotation},
 * {@link org.eclipse.jdt.core.dom.ArrayAccess},
 * {@link org.eclipse.jdt.core.dom.ArrayCreation},
 * {@link org.eclipse.jdt.core.dom.ArrayInitializer},
 * {@link org.eclipse.jdt.core.dom.CastExpression},
 * {@link org.eclipse.jdt.core.dom.ConditionalExpression},
 * {@link org.eclipse.jdt.core.dom.CreationReference},
 * {@link org.eclipse.jdt.core.dom.ExpressionMethodReference},
 * {@link org.eclipse.jdt.core.dom.InstanceofExpression},
 * {@link org.eclipse.jdt.core.dom.LambdaExpression},
 * {@link org.eclipse.jdt.core.dom.MethodReference},
 * {@link org.eclipse.jdt.core.dom.SuperFieldAccess},
 * {@link org.eclipse.jdt.core.dom.SuperMethodInvocation},
 * {@link org.eclipse.jdt.core.dom.SuperMethodReference},
 * {@link org.eclipse.jdt.core.dom.TypeMethodReference},
 */
class ExpressionVisitor extends ASTVisitor {

	private final ExpressionExporter expressionExporter;
	private final TypeExporter typeExporter;
	private final OperatorExporter operatorExporter;
	private Expr result;

	public ExpressionVisitor(ExpressionExporter expressionExporter) {
		this.expressionExporter = expressionExporter;
		this.typeExporter = expressionExporter.getTypeExporter();
		this.operatorExporter = new OperatorExporter(expressionExporter);
	}

	public Expr getResult() {
		return result;
	}

	@Override
	public boolean visit(Assignment node) {
		Expression left = node.getLeftHandSide();
		Expression right = node.getRightHandSide();

		right.accept(this);
		LeftHandSideOfAssignmentVisitor visitor = new LeftHandSideOfAssignmentVisitor(
				result, expressionExporter);
		left.accept(visitor);
		return false;
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		LiteralBoolean value = UMLFactory.eINSTANCE.createLiteralBoolean();

		value.setValue(node.booleanValue());

		result = expressionExporter.createAndSetValueSpecificationAction(value,
				Boolean.toString(node.booleanValue()),
				typeExporter.getBoolean());
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(ClassInstanceCreation node) {

		List<Expr> args = new LinkedList<>();
		args.add(Expr.type(node.getType().resolveBinding(), typeExporter));
		node.arguments().forEach(
				o -> args.add(expressionExporter.export((Expression) o)));
		result = new CreateObjectActionExporter(expressionExporter)
				.export(args);
		return false;
	}

	@Override
	public boolean visit(CharacterLiteral literal) {
		LiteralString value = UMLFactory.eINSTANCE.createLiteralString();

		String asString = Character.toString(literal.charValue());
		value.setValue(asString);

		result = expressionExporter.createAndSetValueSpecificationAction(value,
				"\"" + asString + "\"", typeExporter.getString());
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {

		result = Expr.field(expressionExporter.export(node.getExpression()),
				node.resolveFieldBinding(), expressionExporter);

		return false;
	}

	@Override
	public boolean visit(InfixExpression node) {
		OperatorExporter operatorExporter = new OperatorExporter(
				expressionExporter);

		Pair<Operation, Expr> pair = operatorExporter.exportRaw(node
				.getOperator().toString(), Arrays.asList(node.getLeftOperand(),
				node.getRightOperand()));

		result = pair.getValue();

		for (Object o : node.extendedOperands()) {
			Expression operand = (Expression) o;

			result = expressionExporter.createCallOperationAction(
					pair.getKey(), null,
					Arrays.asList(result, expressionExporter.export(operand)));
		}

		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(MethodInvocation node) {
		IMethodBinding binding = node.resolveMethodBinding();

		List<Expr> args = new LinkedList<>();
		node.arguments().forEach(
				o -> args.add(expressionExporter.export((Expression) o)));

		Expr target = null;
		if (!Modifier.isStatic(binding.getModifiers())) {
			Expression expression = node.getExpression();
			if (expression != null) {
				target = expressionExporter.export(expression);
			} else {
				target = expressionExporter.autoFillTarget(binding);
			}
		} else {
			if (TypeExporter.isAction(binding)) {
				result = expressionExporter.exportAction(binding, args);
				return false;
			}
		}

		result = expressionExporter.createCallOperationAction(
				typeExporter.exportMethodAsOperation(binding, args), target,
				args);

		return false;
	}

	@Override
	public boolean visit(NullLiteral node) {
		LiteralNull value = UMLFactory.eINSTANCE.createLiteralNull();
		result = expressionExporter.createAndSetValueSpecificationAction(value,
				"null", typeExporter.exportType(node.resolveTypeBinding()));
		return false;
	}

	@Override
	public boolean visit(NumberLiteral node) {
		String typeName = node.resolveTypeBinding().getQualifiedName();
		String asString = node.getToken();
		if (typeName.equals("int")) {
			int intValue = Integer.parseInt(asString);
			LiteralInteger value = UMLFactory.eINSTANCE.createLiteralInteger();
			value.setValue(intValue);
			result = expressionExporter.createAndSetValueSpecificationAction(
					value, asString, typeExporter.getInteger());
		} else if (typeName.equals("double")) {
			double doubleValue = Double.parseDouble(asString);
			LiteralReal value = UMLFactory.eINSTANCE.createLiteralReal();
			value.setValue(doubleValue);
			result = expressionExporter.createAndSetValueSpecificationAction(
					value, asString, typeExporter.getReal());
		}
		return false;
	}

	@Override
	public boolean visit(ParenthesizedExpression node) {
		Expression expression = node.getExpression();
		expression.accept(this);
		return false;
	}

	@Override
	public boolean visit(PostfixExpression node) {
		String operator = "_" + node.getOperator().toString();
		result = operatorExporter.exportRaw(operator,
				Arrays.asList(node.getOperand())).getValue();

		return false;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		String operator = node.getOperator().toString() + "_";
		result = operatorExporter.exportRaw(operator,
				Arrays.asList(node.getOperand())).getValue();

		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		IVariableBinding var = (IVariableBinding) node.resolveBinding();
		Name qualifier = node.getQualifier();

		if (qualifier.resolveBinding().getKind() == IBinding.VARIABLE) {
			qualifier.accept(this);
		} else {
			result = null;
		}
		result = Expr.field(result, var, expressionExporter);
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		result = Expr.ofName((IVariableBinding) node.resolveBinding(),
				expressionExporter);
		return false;
	}

	@Override
	public boolean visit(StringLiteral node) {
		LiteralString value = UMLFactory.eINSTANCE.createLiteralString();

		String asString = node.getLiteralValue();
		value.setValue(asString);

		result = expressionExporter.createAndSetValueSpecificationAction(value,
				"\"" + asString + "\"", typeExporter.getString());
		return false;
	}

	@Override
	public boolean visit(ThisExpression node) {
		result = Expr.thisExpression(
				typeExporter.exportType(node.resolveTypeBinding()),
				expressionExporter);
		return false;
	}

	@Override
	public boolean visit(TypeLiteral node) {
		result = Expr.type(node.getType().resolveBinding(), typeExporter);
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		expressionExporter.exportVariableDeclaration(node.getType(), node);
		return false;
	}

}
