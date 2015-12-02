package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.InvalidTypeInModel;

public class ModelVisitor extends VisitorBase {

	public ModelVisitor(ProblemCollector collector) {
		super(collector);
	}

	@Override
	public boolean visit(TypeDeclaration elem) {
		collector.setProblemStatus(
				!ElementTypeTeller.isSignal(elem) && !ElementTypeTeller.isAssociation(elem)
						&& !ElementTypeTeller.isModelClass(elem),
				new InvalidTypeInModel(collector.getSourceInfo(), elem));

		if (ElementTypeTeller.isSignal(elem)) {
			Utils.checkTemplate(collector, elem);
			Utils.checkModifiers(collector, elem);
			acceptChildren(elem, new SignalVisitor(collector));
			checkChildren(elem, "signal", FieldDeclaration.class, MethodDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class);
		} else if (ElementTypeTeller.isAssociation(elem)) {
			Utils.checkTemplate(collector, elem);
			Utils.checkModifiers(collector, elem);
			if (ElementTypeTeller.isComposition(elem)) {
				acceptChildren(elem, new CompositionVisitor(elem, collector));
			} else {
				acceptChildren(elem, new AssociationVisitor(elem, collector));
			}
			checkChildren(elem, "association", TypeDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class);
		} else if (ElementTypeTeller.isModelClass(elem)) {
			acceptChildren(elem, new ModelClassVisitor(collector));
			checkChildren(elem, "class", TypeDeclaration.class, FieldDeclaration.class, MethodDeclaration.class, SimpleName.class, SimpleType.class, Modifier.class);
		}
		return false;
	}

}
