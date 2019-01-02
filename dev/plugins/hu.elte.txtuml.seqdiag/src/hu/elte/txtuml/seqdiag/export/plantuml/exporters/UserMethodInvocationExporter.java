package hu.elte.txtuml.seqdiag.export.plantuml.exporters;


import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;
import hu.elte.txtuml.utils.Logger;

public class UserMethodInvocationExporter extends MethodInvocationExporter {

    // private ActualParameterCache cache;

    public UserMethodInvocationExporter(final PlantUmlCompiler compiler) {
        super(compiler);
    }

    @Override
    public boolean preNext(MethodInvocation curElement) {
        if (ExporterUtils.isActionMethodInvocation(curElement)) {
            return false;
        }

        IMethodBinding binding = (IMethodBinding) curElement.getName().resolveBinding();
        ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement().getAncestor(IJavaElement.COMPILATION_UNIT);
        if (unit == null) {
            Logger.user.info("Cannot visualize " + curElement.getName().getFullyQualifiedName());
            return false;
        }
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        MethodDeclaration decl = (MethodDeclaration) cu.findDeclaringNode(binding.getKey());

        /*
         * compiler.setActualInvocationParameters((List<Expression>)curElement.
         * arguments());
         * curElement.resolveMethodBinding().getParameterTypes()[0].;
         */
        
        compiler.updateLifeLineNames(curElement.getName().getFullyQualifiedName(), curElement.arguments(), decl.parameters());
        
        decl.getBody().accept(compiler);
        
        //TODO: compiler.lifelineNamesInContexts.remove(curElement.getName().getFullyQualifiedName()); //here or someplace else???
        
        return true;
    }

    @Override
    public void afterNext(MethodInvocation curElement) {
    }

    /*
     * class ActualParameterCache { int a; }
     */

}