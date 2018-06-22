package hu.elte.txtuml.validation.model.visitors;

import static hu.elte.txtuml.validation.model.ModelErrors.WRONG_NUMBER_OF_ASSOCIATION_ENDS;
import static hu.elte.txtuml.validation.model.ModelErrors.WRONG_TYPE_IN_ASSOCIATION;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.validation.common.ProblemCollector;

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
			collector.report(WRONG_TYPE_IN_ASSOCIATION.create(collector.getSourceInfo(), node));
			errorInside = true;
		} else {
			++members;
		}
		return false;
	}

	@Override
	public void check() {
		if (!errorInside && members != 2) {
			collector.report(WRONG_NUMBER_OF_ASSOCIATION_ENDS.create(collector.getSourceInfo(), root));
		}
	}

}
