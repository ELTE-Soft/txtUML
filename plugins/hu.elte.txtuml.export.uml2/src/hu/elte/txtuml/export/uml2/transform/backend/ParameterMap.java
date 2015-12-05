package hu.elte.txtuml.export.uml2.transform.backend;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.ParameterExpr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;
import hu.elte.txtuml.export.uml2.utils.ActivityEditor;

import java.util.HashMap;

import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.UMLFactory;

public interface ParameterMap {

	static ParameterMap create(ActivityEditor editor) {
		@SuppressWarnings("serial")
		class ParameterMapImpl extends HashMap<String, ActivityParameterNode>
				implements ParameterMap {

			private ActivityParameterNode ret;

			@Override
			public void copyParameter(Parameter paramToCopy) {
				String name = paramToCopy.getName();
				ParameterDirectionKind direction = paramToCopy.getDirection();
				Parameter param = UMLFactory.eINSTANCE.createParameter();

				param.setName(name);
				param.setDirection(direction);
				param.setType(paramToCopy.getType());

				editor.addOwnedParameter(param);
				ActivityParameterNode paramNode = editor
						.createParameterNode(param);
				this.put(name, paramNode);

				if (direction == ParameterDirectionKind.RETURN_LITERAL) {
					ret = paramNode;
				}
			}

			private ParameterExpr createExpr(ActivityParameterNode paramNode,
					ExpressionExporter expressionExporter) {
				return Expr.param(paramNode, expressionExporter);
			}

			@Override
			public ParameterExpr get(String name,
					ExpressionExporter expressionExporter) {
				return createExpr(super.get(name), expressionExporter);
			}

			@Override
			public ParameterExpr getReturnParam(
					ExpressionExporter expressionExporter) {
				return createExpr(ret, expressionExporter);
			}
		}

		return new ParameterMapImpl();
	}

	void copyParameter(Parameter paramToCopy);

	ParameterExpr get(String name, ExpressionExporter expressionExporter);

	ParameterExpr getReturnParam(ExpressionExporter expressionExporter);
}
