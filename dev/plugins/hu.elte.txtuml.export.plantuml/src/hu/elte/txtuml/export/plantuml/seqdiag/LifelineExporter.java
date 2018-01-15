package hu.elte.txtuml.export.plantuml.seqdiag;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * 
 * 
 * 
 * Responsible for exporting the {@link ModelClass} lifelines from the
 * SequenceDiagram
 * 
 * @author Zoli
 *
 */
public class LifelineExporter extends BaseSeqdiagExporter<FieldDeclaration> {

	public LifelineExporter(PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		return true;
	}

	@Override
	public boolean preNext(FieldDeclaration curElement) {

		int annotationVal = -1;

		int i = 0;
		List<?> modifiers = curElement.modifiers();
		for (i = 0; i < modifiers.size(); ++i) {

			if (modifiers.get(i) instanceof Annotation) {
				Annotation ca = (Annotation) modifiers.get(i);
				if (ca.getTypeName().getFullyQualifiedName().equals("Position")) {
					annotationVal = (int) ((SingleMemberAnnotation) ca).getValue().resolveConstantExpressionValue();
				}
			}
		}

		if (annotationVal != -1 && this.compiler.lastDeclaredParticipantID() + 1 != annotationVal) {
			compiler.addToWaitingList(annotationVal, curElement);
		} else if (annotationVal != -1 && (this.compiler.lastDeclaredParticipantID() + 1 == annotationVal
				|| this.compiler.lastDeclaredParticipantID() == annotationVal)) {
			String participantName = curElement.fragments().get(0).toString();

			compiler.println("participant " + participantName);
			compiler.compiledLifeline(annotationVal);
			compiler.compileWaitingLifelines();
		}

		return true;
	}

	@Override
	public void afterNext(FieldDeclaration curElement) {

	}
}
