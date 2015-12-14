package hu.elte.txtuml.export.uml2.transform.visitors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Classifier;

import hu.elte.txtuml.export.uml2.transform.exporters.ClassifierExporter;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;

public class ClassifierVisitor extends ASTVisitor {

	private final ClassifierExporter classifierExporter;
	private final HashMap<TypeDeclaration, Classifier> visitedClassifiers;
	private final boolean visitNested;

	public ClassifierVisitor(ClassifierExporter classifierExporter,
			boolean visitNested) {
		this.classifierExporter = classifierExporter;
		this.visitedClassifiers = new HashMap<>();
		this.visitNested = visitNested;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {

		Classifier exportedClassifier = null;
		if (this.visitNested && ElementTypeTeller.isModelClass(typeDeclaration)) {
			exportedClassifier = classifierExporter
					.exportClass(typeDeclaration);
			for (Object elem : typeDeclaration.bodyDeclarations()) {
				BodyDeclaration bodyDeclaration = (BodyDeclaration) elem;
				bodyDeclaration.accept(new ClassifierVisitor(
						this.classifierExporter, false));
			}

		} else if (ElementTypeTeller.isSignal(typeDeclaration)) {
			exportedClassifier = classifierExporter
					.exportSignal(typeDeclaration);
		} else {
			return true;
		}

		this.visitedClassifiers.put(typeDeclaration, exportedClassifier);
		return false;
	}

	public Map<TypeDeclaration, Classifier> getVisitedClassifiers() {
		return this.visitedClassifiers;
	}

}
