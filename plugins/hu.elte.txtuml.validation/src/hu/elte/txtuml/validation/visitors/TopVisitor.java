package hu.elte.txtuml.validation.visitors;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.validation.ProblemCollector;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TopVisitor extends VisitorBase {

	public TopVisitor(ProblemCollector collector) {
		super(collector);
	}
	
	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		Type superType = typeDeclaration.getSuperclassType();
		if(superType == null) {
			return false;
		}
		ITypeBinding binding = superType.resolveBinding(); 
		if(binding == null) {
			return false;
		}
		if(binding.getQualifiedName().equals(Model.class.getCanonicalName())) {
			Utils.checkTemplate(collector, typeDeclaration);
			for(Object decl : typeDeclaration.bodyDeclarations()) {
				((BodyDeclaration)decl).accept(new ModelVisitor(collector));
			}
		}
		return false;
	}
}
