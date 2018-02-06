package hu.elte.txtuml.export.plantuml.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.utils.jdt.SharedUtils;

/**
 * The primary role of the precompiler class is to gather preliminary data for
 * the compilation process.<br>
 * This data includes:
 * <ul>
 * <li>Lifelines declared in the class and in its superclasses.</li>
 * <li>The superclass of the currently processed user-written sequence diagram.
 * </li>
 * </ul>
 * The generation stops if an error occurs.
 */
public class PlantUmlPreCompiler extends ASTVisitor {

	private List<FieldDeclaration> lifelines;
	private Type superClass;
	private List<Exception> errorList;

	public PlantUmlPreCompiler() {
		super();
		lifelines = new ArrayList<FieldDeclaration>();
		errorList = new ArrayList<Exception>();
	}

	@Override
	public boolean visit(TypeDeclaration decl) {
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

	@Override
	public boolean visit(FieldDeclaration decl) {
		if (SharedUtils.typeIsAssignableFrom(decl.getType().resolveBinding(), ModelClass.class)) {
			lifelines.add(decl);
		}
		return true;
	}

	public List<FieldDeclaration> getLifelines() {
		return lifelines;
	}

	public Type getSuperClass() {
		return superClass;
	}

	public List<Exception> getErrors() {
		return errorList;
	}

}
