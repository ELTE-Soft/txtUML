package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.utils.jdt.SharedUtils;

/**
 * 
 * 
 * 
 * The PreCompiler classes primary role is to gather preliminary data for the
 * compilation process.<br>
 * This data includes:
 * <ul>
 * <li>Combined fragments declared in the class and it's superClasses</li>
 * <li>Lifelines declared in the class and it's superClasses</li>
 * <li>The name of the superClass of the current class</li>
 * </ul>
 * <br>
 * The generation stops if an error occurs!
 * 
 * @author Zoli
 */
public class PlantUmlPreCompiler extends ASTVisitor {

	protected List<MethodDeclaration> fragments;
	protected ArrayList<FieldDeclaration> lifelines;
	private String currentClassName;
	private Type superClass;
	private ArrayList<Exception> errorList;

	public PlantUmlPreCompiler() {
		super();
		fragments = new ArrayList<MethodDeclaration>();
		lifelines = new ArrayList<FieldDeclaration>();
		errorList = new ArrayList<Exception>();
	}

	public boolean visit(TypeDeclaration decl) {
		superClass = null;
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

	public boolean visit(FieldDeclaration decl) {

		if (SharedUtils.typeIsAssignableFrom(decl.getType().resolveBinding(), ModelClass.class)) {
			lifelines.add(decl);
		}

		return true;
	}

	/**
	 * Get the superClass of the current Type
	 * 
	 * @return
	 */
	public Type getSuperClass() {
		return superClass;
	}

	public List<Exception> getErrors() {
		return errorList;

	}
}
