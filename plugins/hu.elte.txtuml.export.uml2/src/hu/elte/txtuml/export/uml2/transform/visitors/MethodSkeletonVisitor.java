package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.importers.MethodSkeletonImporter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Operation;

public class MethodSkeletonVisitor extends ASTVisitor {
	private MethodSkeletonImporter methodImporter;
	private TypeDeclaration classifierDeclaration;
	private Map<MethodDeclaration, Operation> visitedMethods;
	
	public MethodSkeletonVisitor(MethodSkeletonImporter methodImporter, TypeDeclaration classifierDeclaration) {
		super();
		this.methodImporter = methodImporter;
		this.classifierDeclaration = classifierDeclaration;
		this.visitedMethods = new HashMap<>();
	}
	
	@Override
	public boolean visit(MethodDeclaration methodDeclaration) {
		if(isMemberFunction(methodDeclaration)) {
			Operation operation = this.methodImporter.importMethodSkeleton(methodDeclaration);
			Block methodBody = methodDeclaration.getBody();
			String methodBodyText = methodBody != null ? methodBody.toString() : "";
			this.methodImporter.createOwnedBehavior(operation, methodBodyText);
			visitedMethods.put(methodDeclaration, operation);
		}
		return false;
	}

	private boolean isMemberFunction(MethodDeclaration methodDeclaration) {
		return methodDeclaration.getParent().equals(this.classifierDeclaration);
	}
	
	public Map<MethodDeclaration, Operation> getVisitedMethods() {
		return this.visitedMethods;
	}
}
