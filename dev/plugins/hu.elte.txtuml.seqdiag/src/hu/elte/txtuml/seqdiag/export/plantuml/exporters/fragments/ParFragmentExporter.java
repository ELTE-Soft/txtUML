package hu.elte.txtuml.seqdiag.export.plantuml.exporters.fragments;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

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
				ExportRun(((ClassInstanceCreation)(interaction)).getAnonymousClassDeclaration().bodyDeclarations());				
				break;
			case ASTNode.SIMPLE_NAME:
				SimpleName simpleName = ((SimpleName)(interaction));
				AbstractTypeDeclaration declarationSimpleName = getTypeDeclaration(simpleName.resolveTypeBinding());
				if (declarationSimpleName!=null) {
					ExportRun(declarationSimpleName.bodyDeclarations());
				}
				break;
			case ASTNode.QUALIFIED_NAME:
				QualifiedName qualifiedName = ((QualifiedName)(interaction));
				AbstractTypeDeclaration declarationQualifiedName = getTypeDeclaration(qualifiedName.resolveTypeBinding());
				if (declarationQualifiedName!=null){
					ExportRun(declarationQualifiedName.bodyDeclarations());
				}
				break;
			}
		}
		return false;
	}
	
	private static AbstractTypeDeclaration getTypeDeclaration(ITypeBinding binding) {
		try {
			CompilationUnit cu = getCompilationUnit(binding);
			AbstractTypeDeclaration declaration = (AbstractTypeDeclaration) cu.findDeclaringNode(binding.getKey());
			return declaration;
		} catch (NullPointerException ex) {
			return null;
		}
	}
	
	private static CompilationUnit getCompilationUnit(IBinding binding) {
		try {
			ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement()
					.getAncestor(IJavaElement.COMPILATION_UNIT);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(unit);
			parser.setResolveBindings(true);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			return cu;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public void ExportRun(List<BodyDeclaration> bodyDeclarations){
		for (BodyDeclaration bodyDecl : bodyDeclarations){
			if (bodyDecl instanceof MethodDeclaration){
				if (((MethodDeclaration)(bodyDecl)).getName().toString().equals("run")){
					((MethodDeclaration)(bodyDecl)).getBody().accept(compiler);
				}
			}
		}
	}
}
