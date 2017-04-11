package hu.elte.txtuml.validation.model.visitors;

import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_TYPE_IN_MODEL;

import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.model.Messages;
import hu.elte.txtuml.validation.model.ProblemCollector;

public class ModelVisitor extends VisitorBase {

	public ModelVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(TypeDeclaration elem) {
		Utils.checkTemplate(collector, elem);

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
			checkChildren(elem, Messages.ModelVisitor_association_label,
					AssociationVisitor.ALLOWED_ASSOCIATION_DECLARATIONS);
		} else if (ElementTypeTeller.isModelClass(elem)) {
			checkChildren(elem, Messages.ModelVisitor_class_label, ModelClassVisitor.ALLOWED_MODEL_CLASS_DECLARATIONS);
			acceptChildren(elem, new ModelClassVisitor(collector));
		} else if (ElementTypeTeller.isExternalInterface(elem.resolveBinding())) {
			// nothing to check, but visit inner classes
			return true;
		} else if (ElementTypeTeller.isDataType(elem.resolveBinding())) {
			checkChildren(elem, "data type", DataTypeVisitor.ALLOWED_DATA_TYPE_DECLARATIONS);
			acceptChildren(elem, new DataTypeVisitor(collector));
		} else if (ElementTypeTeller.isInterface(elem)) {
			// TODO: check interfaces
		} else if (ElementTypeTeller.isConnector(elem)) {
			// TODO: check connectors
		} else {
			collector.report(INVALID_TYPE_IN_MODEL.create(collector.getSourceInfo(), elem));
		}
		return false;
	}

	@Override
	public boolean visit(EnumDeclaration elem) {
		if (ElementTypeTeller.isModelEnum(elem)) {
			// TODO: check model enums
		} else {
			collector.report(INVALID_TYPE_IN_MODEL.create(collector.getSourceInfo(), elem));
		}
		return false;
	}

}
