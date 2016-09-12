package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

public class PlantUmlPreCompiler extends ASTVisitor {

	protected List<MethodDeclaration> fragments;
	protected ArrayList<FieldDeclaration> superFields;
	private String currentClassName;
	private Type superClass;
	boolean isSuper;

	public PlantUmlPreCompiler() {
		super();
		fragments = new ArrayList<MethodDeclaration>();
		superFields = new ArrayList<FieldDeclaration>();
	}

	public boolean visit(TypeDeclaration decl) {
		currentClassName = decl.resolveBinding().getQualifiedName().toString();
		Type sc = decl.getSuperclassType();

		if (sc != null) {
			String scName = sc.resolveBinding().getQualifiedName().toString();

			if (!scName.equals("hu.elte.txtuml.api.model.seqdiag.Interaction")
					&& !scName.equals("hu.elte.txtuml.api.model.seqdiag.SequenceDiagram")) {
				superClass = sc;
			}
		}

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

	public void setIsSuper(boolean base) {
		isSuper = base;
	}

	public boolean visit(FieldDeclaration decl) {
		if (isSuper) {
			superFields.add(decl);
		}
		return true;
	}

	public Type getSuperClass() {
		return this.superClass;
	}
}
