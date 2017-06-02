package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.modelclass.InvalidAttributeType;
import hu.elte.txtuml.validation.problems.signal.InvalidSignalContent;

public class SignalVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_SIGNAL_DECLARATIONS = new Class<?>[] { FieldDeclaration.class,
			MethodDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class, Annotation.class,
			Javadoc.class };

	public SignalVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(FieldDeclaration elem) {
		if (!Utils.isAllowedAttributeType(elem.getType(), false)) {
			collector.report(new InvalidAttributeType(collector.getSourceInfo(), elem.getType()));
		} else {
			Utils.checkModifiers(collector, elem);
		}
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration elem) {
		if (!elem.isConstructor()) {
			collector.report(new InvalidSignalContent(collector.getSourceInfo(), elem.getName()));
		} else {
			checkConstructor(elem);
			Utils.checkModifiers(collector, elem);
		}
		return false;
	}

	private void checkConstructor(MethodDeclaration elem) {
		for (Object obj : elem.parameters()) {
			SingleVariableDeclaration param = (SingleVariableDeclaration) obj;
			if (!Utils.isAllowedAttributeType(param.getType(), false)) {
				collector.report(new InvalidAttributeType(collector.getSourceInfo(), param.getType()));
			}
		}
		// TODO: check constructor body
	}
}
