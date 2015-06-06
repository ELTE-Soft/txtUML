package hu.elte.txtuml.layout.export.problems;

import java.lang.annotation.Annotation;

public interface WarningMessages {

	static String unknownInnerClassOfDiagram(Class<?> unknownInnerClass) {
		return unknownInnerClass.getName() + " is neither a subclass of Diagram.Layout, neither of Diagram.LayoutGroup. It is omitted during the interpretation of the diagram description.";
	}

	static String unknownStatementOnLayout(Class<?> layoutClass, Annotation annot) {
		return layoutClass.getName() + " has an annotation which is of type " + annot.annotationType().getName() + ". This annotation type is either not a layout description statement or is not applicable for a layout definition. Therefore it is omitted during the interpretation of the diagram description.";
	}

	
	static String unknownStatementOnLayoutGroup(Class<?> groupClass, Annotation annot) {
		return groupClass.getName() + " has an annotation which is of type " + annot.annotationType().getName() + ". This annotation type is either not a layout description statement or is not applicable for a layout group definition. Therefore it is omitted during the interpretation of the diagram description.";
	}
	
}
