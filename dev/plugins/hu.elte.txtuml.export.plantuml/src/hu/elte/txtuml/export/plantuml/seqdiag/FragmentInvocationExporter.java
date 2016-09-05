package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

public class FragmentInvocationExporter extends MethodInvocationExporter {

	public FragmentInvocationExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean preNext(MethodInvocation curElement) {
		curElement.resolveMethodBinding().getMethodDeclaration();

		return true;
	}

	@Override
	public void afterNext(MethodInvocation curElement) {

	}

}
