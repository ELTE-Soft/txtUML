package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.InvalidModelClassElement;
import hu.elte.txtuml.validation.problems.InvalidTypeWithClassAllowed;
import hu.elte.txtuml.validation.problems.InvalidTypeWithClassNotAllowed;

public class ModelClassVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_MODEL_CLASS_DECLARATIONS = new Class<?>[] { TypeDeclaration.class,
			FieldDeclaration.class, MethodDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class,
			Annotation.class };

	public ModelClassVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(TypeDeclaration elem) {
		boolean valid = ElementTypeTeller.isState(elem) || ElementTypeTeller.isInitialPseudoState(elem)
				|| ElementTypeTeller.isTransition(elem) || ElementTypeTeller.isChoicePseudoState(elem);
		collector.setProblemStatus(!valid, new InvalidModelClassElement(collector.getSourceInfo(), elem.getName()));
		if (valid) {
			handleStateMachineElements(elem);
		}
		return false;
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
		if (!elem.isConstructor()) {
			boolean hasValidReturnType = Utils.isAllowedBasicTypeOrModelClass(elem.getReturnType2(), true);
			collector.setProblemStatus(!hasValidReturnType,
					new InvalidTypeWithClassAllowed(collector.getSourceInfo(), elem.getReturnType2()));
		}

		Utils.checkModifiers(collector, elem);
		for (Object obj : elem.parameters()) {
			SingleVariableDeclaration param = (SingleVariableDeclaration) obj;
			boolean valid = Utils.isAllowedBasicTypeOrModelClass(param.getType(), false);
			collector.setProblemStatus(!valid,
					new InvalidTypeWithClassAllowed(collector.getSourceInfo(), param.getType()));
		}
		// TODO: check body
		return false;
	}
}
