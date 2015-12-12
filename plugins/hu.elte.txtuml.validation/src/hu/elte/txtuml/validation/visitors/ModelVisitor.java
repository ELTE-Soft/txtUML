package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.Messages;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.general.InvalidTypeInModel;

public class ModelVisitor extends VisitorBase {

	public ModelVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(TypeDeclaration elem) {
		Utils.checkTemplate(collector, elem);
		if (!ElementTypeTeller.isSignal(elem) && !ElementTypeTeller.isAssociation(elem)
				&& !ElementTypeTeller.isModelClass(elem)) {
			collector.setProblemStatus(new InvalidTypeInModel(collector.getSourceInfo(), elem));
		}

		if (ElementTypeTeller.isSignal(elem)) {
			Utils.checkModifiers(collector, elem);
			checkChildren(elem, Messages.ModelVisitor_signal_label, SignalVisitor.ALLOWED_SIGNAL_DECLARATIONS);
			acceptChildren(elem, new SignalVisitor(collector));
		} else if (ElementTypeTeller.isAssociation(elem)) {
			Utils.checkModifiers(collector, elem);
			if (ElementTypeTeller.isComposition(elem)) {
				acceptChildren(elem, new CompositionVisitor(elem, collector));
			} else {
				acceptChildren(elem, new AssociationVisitor(elem, collector));
			}
			checkChildren(elem, Messages.ModelVisitor_association_label, AssociationVisitor.ALLOWED_ASSOCIATION_DECLARATIONS);
		} else if (ElementTypeTeller.isModelClass(elem)) {
			checkChildren(elem, Messages.ModelVisitor_class_label, ModelClassVisitor.ALLOWED_MODEL_CLASS_DECLARATIONS);
			acceptChildren(elem, new ModelClassVisitor(collector));
		}
		return false;
	}

}
