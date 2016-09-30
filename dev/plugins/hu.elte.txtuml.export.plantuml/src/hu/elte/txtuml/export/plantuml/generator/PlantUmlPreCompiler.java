package hu.elte.txtuml.export.plantuml.generator;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

import hu.elte.txtuml.api.model.ModelClass;

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
	private URLClassLoader loader;

	public PlantUmlPreCompiler(URLClassLoader loader) {
		super();
		fragments = new ArrayList<MethodDeclaration>();
		lifelines = new ArrayList<FieldDeclaration>();
		errorList = new ArrayList<Exception>();
		this.loader = loader;
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

		Class<?> declCls = null;
		try {
			declCls = loader.loadClass(decl.getType().resolveBinding().getQualifiedName());
			if (ModelClass.class.isAssignableFrom(declCls)) {
				lifelines.add(decl);
			}
		} catch (ClassNotFoundException e) {
			errorList.add(e);
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
