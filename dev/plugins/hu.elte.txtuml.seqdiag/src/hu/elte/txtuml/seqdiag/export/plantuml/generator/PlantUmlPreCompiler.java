package hu.elte.txtuml.seqdiag.export.plantuml.generator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.seqdiag.Proxy;
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

	private List<Lifeline> lifelines;
	private Type superClass;
	private List<Exception> errorList;
	private String seqDiagramName;

	public PlantUmlPreCompiler(String seqDiagramName) {
		super();
		errorList = new ArrayList<Exception>();
		lifelines = new ArrayList<>();
		this.seqDiagramName = seqDiagramName;
	}

	@Override
	public boolean visit(TypeDeclaration decl) {
		if (decl.getName().toString().equals(seqDiagramName)) {
			superClass = null;
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
		return false;
	}

	@Override
	public boolean visit(FieldDeclaration decl) {
		boolean isLifeline = SharedUtils.typeIsAssignableFrom(decl.getType().resolveBinding(), Proxy.class)
				|| SharedUtils.typeIsAssignableFrom(decl.getType().resolveBinding(),
						hu.elte.txtuml.api.model.seqdiag.Lifeline.class);

		if (isLifeline) {
			List<?> modifiers = decl.modifiers();
			Optional<Integer> position = modifiers.stream().filter(modifier -> modifier instanceof Annotation)
					.map(modifier -> (Annotation) modifier)
					.filter(annot -> annot.getTypeName().getFullyQualifiedName().equals("Position"))
					.map(annot -> (int) ((SingleMemberAnnotation) annot).getValue().resolveConstantExpressionValue())
					.findFirst();

			if (position.isPresent()) {
				addLifeline(position.get(), decl);
			} else {
				addLifeline(-1, decl);
			}
		}
		return true;
	}

	public List<Lifeline> getOrderedLifelines() {
		lifelines.sort(Comparator.comparing(Lifeline::getPriority));
		return lifelines;
	}

	public Type getSuperClass() {
		return superClass;
	}

	public List<Exception> getErrors() {
		return errorList;
	}

	private void addLifeline(int position, FieldDeclaration lifeline) {
		lifelines.add(new Lifeline(new LifelineDeclaration(lifeline, position)));
	}

	public void setSeqDiagramName(String seqDiagramName) {
		this.seqDiagramName = seqDiagramName;
	}

}
