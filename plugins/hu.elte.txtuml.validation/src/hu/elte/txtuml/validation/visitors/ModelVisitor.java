package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;

public class ModelVisitor extends VisitorBase {

	public ModelVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(TypeDeclaration elem) {
		checkChildren(elem, ModelClass.class, Signal.class, Association.class);
		if (ElementTypeTeller.isSignal(elem)) {
			Utils.checkTemplate(collector, elem);
			Utils.checkModifiers(collector, elem);
			acceptChildren(elem, new SignalVisitor(collector));
		} else if (ElementTypeTeller.isAssociation(elem)) {
			Utils.checkTemplate(collector, elem);
			Utils.checkModifiers(collector, elem);
			if (ElementTypeTeller.isComposition(elem)) {
				acceptChildren(elem, new CompositionVisitor(elem, collector));
			} else {
				acceptChildren(elem, new AssociationVisitor(elem, collector));
			}
		}
		return false;
	}

	private void acceptChildren(TypeDeclaration elem, VisitorBase visitor) {
		for (Object decl : elem.bodyDeclarations()) {
			((BodyDeclaration) decl).accept(visitor);
		}
	}

}
