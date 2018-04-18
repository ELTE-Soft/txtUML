package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * Class handling the fragment invocations( asking the compiler to be as kind as
 * to compile the needed fragment)
 */
public class FragmentInvocationExporter extends MethodInvocationExporter {

	public FragmentInvocationExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (super.validElement(curElement)) {
			MethodInvocation curInvoc = (MethodInvocation) curElement;
			String QualifiedName = curInvoc.resolveMethodBinding().getDeclaringClass().getQualifiedName().toString();
			if (QualifiedName.equals(compiler.getCurrentClassName())) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	@Override
	public boolean preNext(MethodInvocation curElement) {
		IAnnotationBinding[] annotations = curElement.resolveMethodBinding().getMethodDeclaration().getAnnotations();
		if (annotations.length == 0) {
			return false;
		}

		CombinedFragmentType type = null;
		for (IAnnotationBinding annotation : annotations) {
			if (annotation.getAnnotationType().getQualifiedName().toString()
					.equals("hu.elte.txtuml.api.model.seqdiag.FragmentType")) {
				String constantName = ((IVariableBinding) annotation.getDeclaredMemberValuePairs()[0].getValue())
						.getName();
				type = CombinedFragmentType.valueOf(constantName);
			}
		}

		if (type.equals(CombinedFragmentType.SEQ) || type.equals(CombinedFragmentType.STRICT)) {
			compiler.println("group " + type.name());
		} else {
			compiler.println(type.name());
		}

		String fullyQualifiedName = PlantUmlCompiler.getFullyQualifiedName(curElement);
		String output = compiler.compile(fullyQualifiedName);
		compiler.println(output);
		return false;
	}

	@Override
	public void afterNext(MethodInvocation curElement) {
		compiler.println("end");
	}

}
