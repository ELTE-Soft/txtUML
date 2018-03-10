package hu.elte.txtuml.validation.sequencediagram.visitors;

import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.sequencediagram.ValidationErrors;

public class Utils {

	public static void checkSequenceDiagram(ProblemCollector collector, TypeDeclaration elem) {
		boolean valid = ElementTypeTeller.hasSuperClass(elem.resolveBinding(),
				SequenceDiagram.class.getCanonicalName());
		if (!valid) {
			collector.report(ValidationErrors.INVALID_SUPERCLASS.create(collector.getSourceInfo(), elem));
		}
	}

	public static void checkField(ProblemCollector collector, FieldDeclaration elem) {

	}

	public static void checkMethod(ProblemCollector collector, MethodDeclaration elem) {

	}

	public static boolean isSequenceDiagram(CompilationUnit unit) {
		List<AbstractTypeDeclaration> a = unit.types();
		return a.stream().anyMatch(
				td -> ElementTypeTeller.hasSuperClass(td.resolveBinding(), SequenceDiagram.class.getCanonicalName()));
	}

}
