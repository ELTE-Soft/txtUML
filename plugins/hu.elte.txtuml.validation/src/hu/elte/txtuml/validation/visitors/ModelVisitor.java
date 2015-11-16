package hu.elte.txtuml.validation.visitors;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.InvalidModelElement;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ModelVisitor extends VisitorBase {

	public ModelVisitor(ProblemCollector collector) {
		super(collector);
	}
	
	@Override
	public boolean visit(TypeDeclaration elem) {
		boolean valid = ElementTypeTeller.isModelClass(elem) ||
				ElementTypeTeller.isSignal(elem) ||
				ElementTypeTeller.isAssociation(elem);
		collector.setProblemStatus(!valid, new InvalidModelElement(collector.getSourceInfo(), elem.getName()));
		
		if(ElementTypeTeller.isModelClass(elem)) {
			Utils.checkTemplate(collector, elem);
			Utils.checkModifiers(collector, elem);
			for(Object decl : elem.bodyDeclarations()) {
				((BodyDeclaration)decl).accept(new ModelClassVisitor(collector));
			}
		} else if(ElementTypeTeller.isSignal(elem)) {
			Utils.checkTemplate(collector, elem);
			Utils.checkModifiers(collector, elem);
			for(Object decl : elem.bodyDeclarations()) {
				((BodyDeclaration)decl).accept(new SignalVisitor(collector));
			}
		} else if(ElementTypeTeller.isAssociation(elem)) {
			Utils.checkTemplate(collector, elem);
			Utils.checkModifiers(collector, elem);
			if(ElementTypeTeller.isComposition(elem)) {
				CompositionVisitor visitor = new CompositionVisitor(collector);
				elem.accept(visitor);
			}
			// TODO: check association content
		}
		return false;
	}
	
	@Override
	public boolean visit(FieldDeclaration elem) {
		collector.setProblemStatus(true, new InvalidModelElement(collector.getSourceInfo(), elem));		
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration elem) {
		collector.setProblemStatus(true, new InvalidModelElement(collector.getSourceInfo(), elem.getName()));		
		return false;
	}
}
