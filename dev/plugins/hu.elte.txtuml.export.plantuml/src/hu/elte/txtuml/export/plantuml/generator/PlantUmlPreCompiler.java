package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

public class PlantUmlPreCompiler extends ASTVisitor {

	protected List<MethodDeclaration> fragments;
	private String currentClassName;

	public PlantUmlPreCompiler() {
		super();
		fragments = new ArrayList<MethodDeclaration>();
	}

	public boolean visit(TypeDeclaration decl) {
		currentClassName = decl.resolveBinding().getQualifiedName().toString();
		return true;
	}

	public boolean visit(MethodDeclaration decl) {
		if (decl.resolveBinding().getDeclaringClass().getQualifiedName().toString().equals(currentClassName)
				&& !decl.getName().toString().equals("run") && !decl.getName().toString().equals("initialize")
				&& !decl.isConstructor()) {
			fragments.add(decl);
		}

		return false;
	}
}
