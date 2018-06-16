package hu.elte.txtuml.export.plantuml.seqdiag.fragments;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;
import hu.elte.txtuml.export.plantuml.seqdiag.ExporterUtils;

/**
 * Exporter implementation, which is responsible for exporting PAR combined
 * fragment.
 */
public class ParFragmentExporter extends CombinedFragmentExporter<MethodInvocation> {

	public ParFragmentExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (super.validElement(curElement)) {
			String fullName = ExporterUtils.getFullyQualifiedName((MethodInvocation) curElement);
			return fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.par");
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean preNext(MethodInvocation curElement) {
		List<LambdaExpression> params = (List<LambdaExpression>) curElement.arguments();
		compiler.println("par");
		boolean isFirst = true;
		for (LambdaExpression interaction : params) {
			if (isFirst) {
				isFirst = false;
			} else {
				compiler.println("else");
			}
			interaction.getBody().accept(compiler);
		}
		return false;
	}

}
