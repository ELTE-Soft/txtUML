package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.transition.MissingTransitionSource;
import hu.elte.txtuml.validation.problems.transition.MissingTransitionTarget;
import hu.elte.txtuml.validation.problems.transition.MissingTransitionTrigger;
import hu.elte.txtuml.validation.problems.transition.TransitionMethodNonVoidReturn;
import hu.elte.txtuml.validation.problems.transition.TransitionMethodParameters;
import hu.elte.txtuml.validation.problems.transition.TriggerOnInitialTransition;
import hu.elte.txtuml.validation.problems.transition.UnknownTransitionMethod;

public class TransitionVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_TRANSITION_DECLARATIONS = new Class<?>[] { MethodDeclaration.class,
			SimpleName.class, SimpleType.class, Modifier.class, Annotation.class, Javadoc.class };

	private TypeDeclaration transition;

	public TransitionVisitor(TypeDeclaration transition, ProblemCollector collector) {
		super(collector);
		this.transition = transition;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (!ElementTypeTeller.isEffect(node) && !ElementTypeTeller.isGuard(node)) {
			collector.report(new UnknownTransitionMethod(collector.getSourceInfo(), node));
			return false;
		}
		if (ElementTypeTeller.isEffect(node)) {
			if (!Utils.isVoid(node.getReturnType2())) {
				collector.report(new TransitionMethodNonVoidReturn(collector.getSourceInfo(), node.getReturnType2()));
			}
		} else {
			if (!Utils.isBoolean(node.getReturnType2())) {
				collector.report(new TransitionMethodNonVoidReturn(collector.getSourceInfo(), node.getReturnType2()));
			}
		}
		if (!node.parameters().isEmpty()) {
			collector.report(new TransitionMethodParameters(collector.getSourceInfo(), node));
		}

		// TODO: validate body
		return false;

	}

	public void check() {
		checkDirections();
	};

	/**
	 * Checks the {@link @To} {@link @From} and {@link @Trigger} annotations.
	 * Only reports error on {@link @Trigger} if the others are OK.
	 */
	private void checkDirections() {
		ITypeBinding triggerValue = null;
		Annotation triggerAnnot = null;
		ITypeBinding fromValue = null;
		ITypeBinding toValue = null;
		for (Object mod : transition.modifiers()) {
			if (mod instanceof Annotation) {
				Annotation annot = (Annotation) mod;
				IAnnotationBinding annBinding = annot.resolveAnnotationBinding();
				ITypeBinding value = null;
				for (IMemberValuePairBinding binding : annBinding.getAllMemberValuePairs()) {
					if (binding.getKey() == null && binding.getValue() instanceof ITypeBinding) {
						value = (ITypeBinding) binding.getValue();
					}
				}
				String bindedAnnotationName = annBinding.getAnnotationType().getQualifiedName();
				if (From.class.getCanonicalName().equals(bindedAnnotationName)) {
					fromValue = value;
				} else if (To.class.getCanonicalName().equals(bindedAnnotationName)) {
					toValue = value;
				} else if (Trigger.class.getCanonicalName().equals(bindedAnnotationName)) {
					triggerValue = value;
					triggerAnnot = annot;
				}
			}
		}
		if (fromValue == null) {
			collector.report(new MissingTransitionSource(collector.getSourceInfo(), transition));
		}
		if (toValue == null) {
			collector.report(new MissingTransitionTarget(collector.getSourceInfo(), transition));
		}
		if (fromValue != null && toValue != null) {
			checkTrigger(triggerAnnot, triggerValue, fromValue);
		}
	}

	protected void checkTrigger(Annotation signal, ITypeBinding value, ITypeBinding fromValue) {
		if (value == null && !ElementTypeTeller.isInitialPseudoState(fromValue)
				&& !ElementTypeTeller.isChoicePseudoState(fromValue)) {
			collector.report(new MissingTransitionTrigger(collector.getSourceInfo(), transition));
		}
		if (value != null && (ElementTypeTeller.isInitialPseudoState(fromValue)
				|| ElementTypeTeller.isChoicePseudoState(fromValue))) {
			collector.report(
					new TriggerOnInitialTransition(collector.getSourceInfo(), signal != null ? signal : transition));
		}
	}

}
