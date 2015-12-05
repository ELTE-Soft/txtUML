package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.modelclass.InvalidTypeWithClassNotAllowed;
import hu.elte.txtuml.validation.problems.signal.InvalidSignalContent;

public class SignalVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_SIGNAL_DECLARATIONS = new Class<?>[] { FieldDeclaration.class,
			MethodDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class, Annotation.class };

	public SignalVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(FieldDeclaration elem) {
		boolean valid = Utils.isAllowedBasicType(elem.getType(), false);
		collector.setProblemStatus(!valid,
				new InvalidTypeWithClassNotAllowed(collector.getSourceInfo(), elem.getType()));

		if (valid) {
			Utils.checkModifiers(collector, elem);
		}
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration elem) {
		boolean valid = elem.isConstructor();
		collector.setProblemStatus(!valid, new InvalidSignalContent(collector.getSourceInfo(), elem.getName()));
		if (valid) {
			checkConstructor(elem);
			Utils.checkModifiers(collector, elem);
		}
		return false;
	}

	private void checkConstructor(MethodDeclaration elem) {
		for (Object obj : elem.parameters()) {
			SingleVariableDeclaration param = (SingleVariableDeclaration) obj;
			boolean valid = Utils.isAllowedBasicType(param.getType(), false);
			collector.setProblemStatus(!valid,
					new InvalidTypeWithClassNotAllowed(collector.getSourceInfo(), param.getType()));
		}
		// TODO: check constructor body
	}
}
