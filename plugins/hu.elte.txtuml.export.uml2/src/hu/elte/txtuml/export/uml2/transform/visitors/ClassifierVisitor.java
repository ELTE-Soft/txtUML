package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.importers.ClassifierImporter;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Classifier;

public class ClassifierVisitor extends ASTVisitor {
	
	private ClassifierImporter classifierImporter;
	private HashMap<TypeDeclaration, Classifier> visitedClassifiers;
	private boolean visitNested;
	
	public ClassifierVisitor(ClassifierImporter classifierImporter, boolean visitNested) {
		super();
		this.classifierImporter = classifierImporter;
		this.visitedClassifiers = new HashMap<>();
		this.visitNested = visitNested;
	}
	
	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {

		Classifier importedClassifier = null;
		if(this.visitNested && ElementTypeTeller.isModelClass(typeDeclaration)) {
			importedClassifier = this.classifierImporter.importClass(typeDeclaration);
			for(Object elem : typeDeclaration.bodyDeclarations()) {
				BodyDeclaration bodyDeclaration = (BodyDeclaration) elem;
				bodyDeclaration.accept(new ClassifierVisitor(this.classifierImporter, false));
			}
			
		} else if(ElementTypeTeller.isSignal(typeDeclaration)) {
			importedClassifier = this.classifierImporter.importSignal(typeDeclaration);
		} else {
			return true;
		}
		
		this.visitedClassifiers.put(typeDeclaration, importedClassifier);
		return false;
	}
	
	public Map<TypeDeclaration, Classifier> getVisitedClassifiers() {
		return this.visitedClassifiers;
	}

}
