package hu.elte.txtuml.validation.sequencediagram;

import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.AbstractJtxtUMLCompilationParticipant;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.sequencediagram.visitors.SequenceDiagramVisitor;

public class SequenceDiagramCompilationParticipant extends AbstractJtxtUMLCompilationParticipant {

	public static final String MARKER_TYPE = "hu.elte.txtuml.validation.sequencediagrammarker"; //$NON-NLS-1$

	@Override
	protected String getMarkerType() {
		return MARKER_TYPE;
	}

	@Override
	protected void validate(CompilationUnit unit, ProblemCollector collector) {
		if (!ElementTypeTeller.isModelElement(unit)) {
			unit.accept(new SequenceDiagramVisitor(collector));
		}
	}

}
