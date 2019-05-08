package hu.elte.txtuml.seqdiag.export.plantuml.exporters.fragments;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.seqdiag.export.plantuml.exporters.ExporterUtils;
import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;

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
		List<Expression> params = (List<Expression>) curElement.arguments();
		compiler.println("par");
		boolean isFirst = true;
		for (Expression interaction : params) {
			if (isFirst) {
				isFirst = false;
			} else {
				compiler.println("else");
			}
			switch (interaction.getNodeType()){
			case ASTNode.LAMBDA_EXPRESSION:
				((LambdaExpression)(interaction)).getBody().accept(compiler);
				break;
			case ASTNode.CLASS_INSTANCE_CREATION:
				List<BodyDeclaration> bodyDecls = ((ClassInstanceCreation)(interaction)).getAnonymousClassDeclaration().bodyDeclarations();
				for (BodyDeclaration bodyDecl : bodyDecls){
					if (bodyDecl instanceof MethodDeclaration){
						if (((MethodDeclaration)(bodyDecl)).getName().toString().equals("run")){
							((MethodDeclaration)(bodyDecl)).getBody().accept(compiler);
						}
					}
				}
				break;
			}	
		}
		return false;
	}

}
