package hu.elte.txtuml.validation.model.visitors;

import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_DATA_TYPE_FIELD;
import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_PARAMETER_TYPE;
import static hu.elte.txtuml.validation.model.ModelErrors.MUTABLE_DATA_TYPE_FIELD;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.ProblemCollector;

public class DataTypeVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_DATA_TYPE_DECLARATIONS = new Class<?>[] { FieldDeclaration.class,
			MethodDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class, Annotation.class,
			Javadoc.class };

	public DataTypeVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		if (!ElementTypeTeller.isFinal(node)) {
			collector.report(MUTABLE_DATA_TYPE_FIELD.create(collector.getSourceInfo(), node));
		}
		if (!Utils.isAllowedAttributeType(node.getType(), false)) {
			collector.report(INVALID_DATA_TYPE_FIELD.create(collector.getSourceInfo(), node));
		}
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (!node.isConstructor()) {
			if (node.getReturnType2() != null && !Utils.isAllowedParameterType(node.getReturnType2(), true)) {
				collector.report(INVALID_PARAMETER_TYPE.create(collector.getSourceInfo(), node.getReturnType2()));
			}
		}

		Utils.checkModifiers(collector, node);
		for (Object obj : node.parameters()) {
			SingleVariableDeclaration param = (SingleVariableDeclaration) obj;
			if (!Utils.isAllowedParameterType(param.getType(), false)) {
				collector.report(INVALID_PARAMETER_TYPE.create(collector.getSourceInfo(), param.getType()));
			}
		}

		// TODO: check body
		return false;
	}

}
