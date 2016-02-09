package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import hu.elte.txtuml.export.uml2.transform.exporters.TypeExporter;

import java.util.Arrays;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.ReadSelfAction;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;

public interface Expr {

	Type getType();

	String getName();

	/**
	 * Exports the evaluation of this expression (writes it into the model).
	 * 
	 * @return this expression
	 */
	default Expr evaluate() {
		return this;
	}

	/**
	 * Call only after {@link #evaluate} is called.
	 */
	default ObjectNode getObjectNode() {
		return getOutputPin();
	}

	/**
	 * Call only after {@link #evaluate} is called.
	 * <p>
	 * If it is not necessary to return an {@link OutputPin}, use
	 * {@link #getObjectNode() instead}.
	 * 
	 * @throw UnsupportedOperationException
	 */
	OutputPin getOutputPin();

	/**
	 * If this expression can be the left-hand side of an assignment, this
	 * method assigns the given expression to it.
	 * 
	 * @throw UnsupportedOperationException
	 */
	default void setValue(Expr newValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@link setValue} does not throw an {@link UnsupportedOperationException}
	 * on this expression type.
	 */
	public interface AssignableExpr extends Expr {
		@Override
		void setValue(Expr newValue);
	}

	public interface ParameterExpr extends AssignableExpr {
	}

	public interface VariableExpr extends AssignableExpr {
	}

	public interface StructuralFeatureExpr extends AssignableExpr {
	}

	interface TypeExpr extends Expr {
	}

	static Expr ofPin(OutputPin pin, String byName) {
		return new Expr() {
			@Override
			public Type getType() {
				return pin.getType();
			}

			@Override
			public String getName() {
				return byName;
			}

			@Override
			public OutputPin getOutputPin() {
				return pin;
			}

		};
	}

	static ParameterExpr param(ActivityParameterNode node,
			ExpressionExporter<? extends ActivityNode> expressionExporter) {
		return new ParameterExpr() {

			@Override
			public String getName() {
				return node.getParameter().getName();
			}

			@Override
			public Type getType() {
				return node.getType();
			}

			@Override
			public ObjectNode getObjectNode() {
				return node;
			}

			@Override
			public OutputPin getOutputPin() {
				if (expressionExporter.getTypeExporter().isBoolean(getType())) {
					OperatorExporter operatorExporter = new OperatorExporter(expressionExporter);
					return operatorExporter.export("id", Arrays.asList(this)).getSecond().evaluate().getOutputPin();
				}
				throw new UnsupportedOperationException();
			}

			@Override
			public void setValue(Expr newValue) {
				expressionExporter.createObjectFlowBetweenActivityNodes(newValue.evaluate().getObjectNode(), node);
			}
		};

	}

	static VariableExpr var(Variable var, ExpressionExporter<? extends ActivityNode> expressionExporter) {
		return new Expr.VariableExpr() {

			private OutputPin value = null;

			@Override
			public Type getType() {
				return var.getType();
			}

			@Override
			public String getName() {
				return var.getName();
			}

			@Override
			public Expr evaluate() {
				value = expressionExporter.createReadVariableAction(var);
				return this;
			}

			@Override
			public OutputPin getOutputPin() {
				return value;
			}

			@Override
			public void setValue(Expr newValue) {
				expressionExporter.createWriteVariableAction(var, newValue);
			}

		};
	}

	/**
	 * @param target
	 *            optional; can be <code>null</code> in case of static
	 *            structural features
	 */
	static StructuralFeatureExpr field(Expr target, IVariableBinding field,
			ExpressionExporter<? extends ActivityNode> expressionExporter) {
		StructuralFeature feature = expressionExporter.getTypeExporter().exportFieldAsStructuralFeature(field);

		return new StructuralFeatureExpr() {

			private OutputPin value = null;

			@Override
			public String getName() {
				return (target == null ? "" : target.getName() + ".") + feature.getName();
			}

			@Override
			public Type getType() {
				return feature.getType();
			}

			@Override
			public Expr evaluate() {
				value = expressionExporter.createReadStructuralFeatureAction(feature, target);
				return this;
			}

			@Override
			public OutputPin getOutputPin() {
				return value;
			}

			@Override
			public void setValue(Expr newValue) {
				expressionExporter.createWriteStructuralFeatureAction(feature, target, newValue);
			}

		};

	}

	static Expr thisExpression(Type type, ExpressionExporter<? extends ActivityNode> expressionExporter) {
		return new Expr() {

			private OutputPin value;

			@Override
			public String getName() {
				return "self";
			};

			@Override
			public Type getType() {
				return type;
			};

			@Override
			public Expr evaluate() {
				ReadSelfAction action = (ReadSelfAction) expressionExporter.createAndAddNode("self",
						UMLPackage.Literals.READ_SELF_ACTION);
				value = action.createResult("self", type);
				return this;
			}

			@Override
			public OutputPin getOutputPin() {
				return value;
			};

		};

	}

	static Expr trueExpression(ExpressionExporter<? extends ActivityNode> expressionExporter) {
		return new Expr() {

			private OutputPin value;

			@Override
			public String getName() {
				return "true";
			};

			@Override
			public Type getType() {
				return expressionExporter.getTypeExporter().getBoolean();
			};

			@Override
			public Expr evaluate() {
				LiteralBoolean value = UMLFactory.eINSTANCE.createLiteralBoolean();

				value.setValue(true);

				return expressionExporter.createAndSetValueSpecificationAction(value, getName(), getType());
			}

			@Override
			public OutputPin getOutputPin() {
				return value;
			};

		};

	}

	static TypeExpr type(ITypeBinding binding, TypeExporter typeExporter) {
		return new TypeExpr() {

			@Override
			public Type getType() {
				return typeExporter.exportType(binding);
			}

			@Override
			public String getName() {
				return binding.getName();
			}

			@Override
			public OutputPin getOutputPin() {
				throw new UnsupportedOperationException();
			}

		};
	}

	static Expr ofName(IVariableBinding binding, ExpressionExporter<? extends ActivityNode> expressionExporter) {
		return ofName(binding, binding.getName(), expressionExporter);
	}

	static Expr ofName(IVariableBinding binding, String qualifiedName,
			ExpressionExporter<? extends ActivityNode> expressionExporter) {
		if (binding.isField()) {
			return Expr.field(expressionExporter.autoFillTarget(binding, qualifiedName), binding, expressionExporter);
		} else if (binding.isParameter()) {
			return expressionExporter.getParams().get(binding.getName(), expressionExporter);
		} else if (binding.isEnumConstant()) {
			// TODO enum constants
			return null;
		} else {
			return expressionExporter.getVars().get(binding.getName(), expressionExporter);
		}
	}

}
