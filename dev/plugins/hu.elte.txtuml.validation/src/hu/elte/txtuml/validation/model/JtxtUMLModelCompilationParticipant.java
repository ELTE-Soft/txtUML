package hu.elte.txtuml.validation.model;

import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.AbstractJtxtUMLCompilationParticipant;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.model.visitors.ModelVisitor;

/**
 * Compilation participant for JtxtUML model validation. Performs validation on
 * reconcile events (not typing for a few seconds) and build events
 */
public class JtxtUMLModelCompilationParticipant extends AbstractJtxtUMLCompilationParticipant {

	public static final String JTXTUML_MODEL_MARKER_TYPE = "hu.elte.txtuml.validation.jtxtumlmodelmarker"; //$NON-NLS-1$

	@Override
	protected String getMarkerType() {
		return JTXTUML_MODEL_MARKER_TYPE;
	}

	@Override
	protected void validate(CompilationUnit unit, ProblemCollector collector) {
		if (ElementTypeTeller.isModelElement(unit)) {
			unit.accept(new ModelVisitor(collector));
		}
	}

	
}
