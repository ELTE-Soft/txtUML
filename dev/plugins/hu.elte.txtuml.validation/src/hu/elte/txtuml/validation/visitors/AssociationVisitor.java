package hu.elte.txtuml.validation.visitors;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.ProblemCollector;
import hu.elte.txtuml.validation.problems.association.WrongNumberOfAssociationEnds;
import hu.elte.txtuml.validation.problems.association.WrongTypeInAssociation;

public class AssociationVisitor extends VisitorBase {

	public static final Class<?>[] ALLOWED_ASSOCIATION_DECLARATIONS = new Class<?>[] { TypeDeclaration.class,
			SimpleName.class, SimpleType.class, Modifier.class, Annotation.class, Javadoc.class };

	private TypeDeclaration root;
	private int members = 0;
	private boolean errorInside = false;

	public AssociationVisitor(TypeDeclaration root, ProblemCollector collector) {
		super(collector);
		this.root = root;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		if (!ElementTypeTeller.isAssociationeEnd(node)) {
			collector.report(new WrongTypeInAssociation(collector.getSourceInfo(), node));
			errorInside = true;
		} else {
			++members;
		}
		return false;
	}

	@Override
	public void check() {
		if (!errorInside && members != 2) {
			collector.report(new WrongNumberOfAssociationEnds(collector.getSourceInfo(), root));
		}
	}

}
