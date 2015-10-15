package hu.elte.txtuml.export.uml2.transform.backend;

import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr.VariableExpr;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.ExpressionExporter;

import java.util.HashMap;

import org.eclipse.uml2.uml.Variable;

public interface VariableMap {

	static VariableMap create() {
		@SuppressWarnings("serial")
		class VariableMapImpl extends HashMap<String, Variable> implements
				VariableMap {

			@Override
			public void addVariable(Variable var) {
				this.put(var.getName(), var);
			}
			
			@Override
			public VariableExpr get(String name,
					ExpressionExporter expressionExporter) {
				return Expr.var(super.get(name), expressionExporter);
			}
			
			@Override
			public VariableMap copy() {
				return (VariableMap) clone();
			}

		}

		return new VariableMapImpl();
	}
	
	void addVariable(Variable var);

	VariableExpr get(String name, ExpressionExporter expressionExporter);

	VariableMap copy();

}
