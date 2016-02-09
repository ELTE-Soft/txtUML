package hu.elte.txtuml.export.uml2.transform.exporters.expressions;

import static org.eclipse.uml2.uml.ParameterDirectionKind.INOUT_LITERAL;
import static org.eclipse.uml2.uml.ParameterDirectionKind.IN_LITERAL;
import static org.eclipse.uml2.uml.ParameterDirectionKind.OUT_LITERAL;
import static org.eclipse.uml2.uml.ParameterDirectionKind.RETURN_LITERAL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.eclipse.uml2.uml.Variable;

import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.transform.backend.ParameterMap;
import hu.elte.txtuml.export.uml2.transform.backend.VariableMap;
import hu.elte.txtuml.export.uml2.transform.exporters.BlockExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.TypeExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.actions.CreateObjectActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.actions.DeleteObjectActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.actions.LinkActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.actions.SendActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.actions.StartActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.actions.UnlinkActionExporter;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.ParameterExpr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.TypeExpr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.VariableExpr;
import hu.elte.txtuml.export.uml2.utils.ControlStructureEditor;
import hu.elte.txtuml.utils.Pair;

public class ExpressionExporter<ElemType extends ActivityNode> extends ControlStructureEditor<ElemType> {

	private final ParameterMap params;
	private final VariableMap vars;
	private final TypeExporter typeExporter;
	private final ExpressionVisitor visitor;

	/**
	 * @param controlStructure
	 *            the control structure to add the edges and variables to
	 * @param nodeList
	 *            the list to add the exported nodes to
	 * @param params
	 *            parameters in scope
	 * @param vars
	 *            variables in scope
	 * @param typeExporter
	 *            a type exporter
	 */
	public ExpressionExporter(StructuredActivityNode controlStructure, EList<ElemType> nodeList,
			ParameterMap params, VariableMap vars, TypeExporter typeExporter) {
		super(controlStructure, nodeList);

		this.params = params;
		this.vars = vars;
		this.typeExporter = typeExporter;
		this.visitor = new ExpressionVisitor(this);
	}

	public ExpressionExporter(BlockExporter<ElemType> blockExporter) {
		super(blockExporter);

		this.params = blockExporter.getParameters();
		this.vars = blockExporter.getVariables();
		this.typeExporter = blockExporter.getTypeExporter();
		this.visitor = new ExpressionVisitor(this);
	}

	public Expr export(Expression expression) {
		expression.accept(visitor);
		Expr result = visitor.getResult();

		if (result == null) {
			if (!TypeExporter.isVoid(expression.resolveTypeBinding())) {
				// TODO unexported expression
				return createOpaqueAction(expression.toString(), expression.resolveTypeBinding(), null, null);
			}
		}
		return result;
	}

	public void exportReturnStatement(Expression expression) {
		if (expression != null) {
			params.getReturnParam(this).setValue(export(expression));
		}
	}

	public void exportVariableDeclaration(ITypeBinding type, ASTNode node) {
		final List<Pair<String, Expression>> vars = new ArrayList<>();
		node.accept(new ASTVisitor() {
			@Override
			public boolean visit(VariableDeclarationFragment node) {
				vars.add(Pair.of(node.getName().getIdentifier(), node.getInitializer()));
				return false;
			}
		});
		vars.forEach(pair -> exportVariable(type, pair.getFirst(), pair.getSecond()));
	}

	private void exportVariable(ITypeBinding type, String name, Expression initializer) {
		Type exportedType = typeExporter.exportType(type);

		vars.addVariable(createVariable(name, exportedType));

		if (initializer != null) {
			vars.get(name, this).setValue(export(initializer));
		}
	}

	public Expr exportAction(IMethodBinding binding, List<Expr> args) throws ExportException {
		String actionName = binding.getName();

		if (actionName.equals("create")) {
			return new CreateObjectActionExporter(this).export(binding, args);
		} else if (actionName.equals("delete")) {
			new DeleteObjectActionExporter(this).export(args);
		} else if (actionName.equals("link")) {
			new LinkActionExporter(this).export(binding, args);
		} else if (actionName.equals("unlink")) {
			new UnlinkActionExporter(this).export(binding, args);
		} else if (actionName.equals("start")) {
			new StartActionExporter(this).export(args);
		} else if (actionName.equals("send")) {
			new SendActionExporter(this).export(args);
		}

		return null;
	}

	public Expr autoFillTarget(IBinding binding, String qualifiedName) {
		if (Modifier.isStatic(binding.getModifiers())) {
			return null;
		}
		ITypeBinding type;

		if (binding.getKind() == IBinding.VARIABLE) {
			type = ((IVariableBinding) binding).getDeclaringClass();
		} else if (binding.getKind() == IBinding.METHOD) {
			type = ((IMethodBinding) binding).getDeclaringClass();
		} else {
			return null;
		}

		String simpleName = binding.getName();
		if (simpleName.equals(qualifiedName)) {
			return Expr.thisExpression(typeExporter.exportType(type), this);
		} else {
			String ownerName = qualifiedName.substring(0, qualifiedName.length() - simpleName.length() - 1);

			ParameterExpr paramExpr = params.get(ownerName, this);
			// owner is a parameter
			if (paramExpr != null) {
				return paramExpr;
			}

			VariableExpr varExpr = vars.get(ownerName, this);
			// owner is a variable
			if (varExpr != null) {
				return varExpr;
			}

			return Expr.thisExpression(typeExporter.exportType(type), this);
		}
	}

	public OutputPin createReadVariableAction(Variable var) {
		ReadVariableAction action = (ReadVariableAction) createAndAddNode(var.getName(),
				UMLPackage.Literals.READ_VARIABLE_ACTION);

		action.setVariable(var);

		return action.createResult(var.getName(), var.getType());
	}

	public void createWriteVariableAction(Variable var, Expr rightHandSide) {
		rightHandSide.evaluate();

		String newValueName = rightHandSide.getName();

		AddVariableValueAction action = (AddVariableValueAction) createAndAddNode(
				var.getName() + "=" + newValueName, UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);

		action.setIsReplaceAll(true);

		InputPin value = action.createValue(newValueName, rightHandSide.getType());
		createObjectFlowBetweenActivityNodes(rightHandSide.getObjectNode(), value);

		action.setVariable(var);
	}

	/**
	 * @param target
	 *            optional; can be <code>null</code> in case of static
	 *            structural features
	 */
	public OutputPin createReadStructuralFeatureAction(StructuralFeature feature, Expr target) {
		if (target != null) {
			target.evaluate();
		}

		ReadStructuralFeatureAction action = (ReadStructuralFeatureAction) createAndAddNode(feature.getName(),
				UMLPackage.Literals.READ_STRUCTURAL_FEATURE_ACTION);

		action.setStructuralFeature(feature);
		if (target != null) {
			InputPin object = action.createObject(target.getName(), target.getType());
			createObjectFlowBetweenActivityNodes(target.getObjectNode(), object);
		}

		OutputPin ret = action.createResult(feature.getName(), feature.getType());

		return ret;
	}

	/**
	 * @param target
	 *            optional; can be <code>null</code> in case of static
	 *            structural features
	 */
	public void createWriteStructuralFeatureAction(StructuralFeature feature, Expr target, Expr newValue) {
		newValue.evaluate();
		if (target != null) {
			target.evaluate();
		}

		String newValueName = newValue.getName();

		StringBuilder builder = new StringBuilder(target == null ? "" : target.getName() + ".");
		builder.append(feature.getName());
		builder.append("=");
		builder.append(newValueName);

		AddStructuralFeatureValueAction action = (AddStructuralFeatureValueAction) createAndAddNode(
				builder.toString(), UMLPackage.Literals.ADD_STRUCTURAL_FEATURE_VALUE_ACTION);

		action.setIsReplaceAll(true);

		InputPin value = action.createValue(newValueName, newValue.getType());
		createObjectFlowBetweenActivityNodes(newValue.getObjectNode(), value);

		action.setStructuralFeature(feature);
		if (target != null) {
			InputPin object = action.createObject(target.getName(), target.getType());
			createObjectFlowBetweenActivityNodes(target.getObjectNode(), object);
		}

	}

	/**
	 * @param target
	 *            optional; can be <code>null</code> in case of static
	 *            operations
	 */
	public Expr createCallOperationAction(Operation operation, Expr target, List<Expr> args) {

		// evaluate inputs and target

		Iterator<Expr> it = args.iterator();
		for (Parameter p : operation.getOwnedParameters()) {
			if (p.getDirection() == RETURN_LITERAL) {
				continue;
			}
			Expr arg = it.next();
			if (p.getDirection() == IN_LITERAL || p.getDirection() == INOUT_LITERAL) {
				arg.evaluate();
			}
		}

		if (target != null) {
			target.evaluate();
		}

		// generate name

		StringBuilder builder = new StringBuilder(target == null ? "" : target.getName() + ".");
		builder.append(operation.getName() + "(");
		if (!args.isEmpty()) {
			it = args.iterator();
			builder.append(it.next().getName());
			while (it.hasNext()) {
				builder.append(",");
				builder.append(it.next().getName());
			}
		}
		builder.append(")");

		// create action

		CallOperationAction action = (CallOperationAction) createAndAddNode(builder.toString(),
				UMLPackage.Literals.CALL_OPERATION_ACTION);

		action.setOperation(operation);

		// connect to inputs, write outputs

		it = args.iterator();
		Expr ret = null;
		for (Parameter p : operation.getOwnedParameters()) {
			if (p.getDirection() == RETURN_LITERAL) {
				ret = Expr.ofPin(action.createResult(p.getName(), p.getType()), action.getName());
				continue;
			}
			Expr arg = it.next();
			if (p.getDirection() == IN_LITERAL || p.getDirection() == INOUT_LITERAL) {
				createObjectFlowBetweenActivityNodes(arg.getObjectNode(),
						action.createArgument(p.getName(), p.getType()));
			}
			if (p.getDirection() == OUT_LITERAL || p.getDirection() == INOUT_LITERAL) {
				arg.setValue(Expr.ofPin(action.createResult(p.getName(), p.getType()), "out " + arg.getName()));
			}
		}

		// connect to target

		if (target != null) {
			InputPin t = action.createTarget(target.getName(), target.getType());
			createObjectFlowBetweenActivityNodes(target.getObjectNode(), t);
		}

		return ret;
	}

	Expr createAndSetValueSpecificationAction(ValueSpecification value, String name, Type type) {
		ValueSpecificationAction action = (ValueSpecificationAction) createAndAddNode(name,
				UMLPackage.Literals.VALUE_SPECIFICATION_ACTION);
		value.setName(name);
		value.setType(type);
		action.setValue(value);
		return Expr.ofPin(action.createResult(name, type), name);
	}

	/**
	 * For currently unexported features.
	 * 
	 * @param target
	 */
	Expr createOpaqueAction(String stringValue, ITypeBinding returnType, Expr target, List<Expr> args) {
		OpaqueAction action = (OpaqueAction) createAndAddNode("unknown < " + stringValue + " >",
				UMLPackage.Literals.OPAQUE_ACTION);

		action.getLanguages().add("JtxtUML");
		action.getBodies().add(stringValue);

		if (target != null) {
			action.createInputValue("target " + target.getName(), target.getType());
		}

		if (args != null) {
			args.forEach(arg -> {
				InputPin input = action.createInputValue(arg.getName(), arg.getType());

				if (!(arg instanceof TypeExpr)) {
					createObjectFlowBetweenActivityNodes(arg.evaluate().getObjectNode(), input);
				}
			});
		}

		if (returnType == null || TypeExporter.isVoid(returnType)) {
			return null;
		}
		return Expr.ofPin(action.createOutputValue(stringValue, typeExporter.exportType(returnType)), stringValue);
	}

	public ParameterMap getParams() {
		return params;
	}

	public VariableMap getVars() {
		return vars;
	}

	public TypeExporter getTypeExporter() {
		return typeExporter;
	}

}
