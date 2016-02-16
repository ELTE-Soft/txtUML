package hu.elte.txtuml.export.uml2.transform.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Model;

import hu.elte.txtuml.export.uml2.mapping.ModelMapCollector;
import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.transform.backend.RuntimeExportException;
import hu.elte.txtuml.export.uml2.transform.exporters.AssociationExporter;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;

/**
 * Instances of this class visit associations in a source txtUML model.
 * 
 * @throws RuntimeException
 *             When applied to an <code>ASTNode</code>, methods of this visitor
 *             may throw this kind of exception.
 */
public class AssociationVisitor extends ASTVisitor {

	private final ModelMapCollector mapping;
	private final Model exportedModel;

	public AssociationVisitor(ModelMapCollector mapping, Model exportedModel) {
		this.mapping = mapping;
		this.exportedModel = exportedModel;
	}

	/**
	 * Visits a TypeDeclaration ASTNode in the AST. If it represents a txtUML
	 * association, exports it and does not continue the visit on it's child
	 * nodes. Otherwise, it ignores the node and continues the visit.
	 */
	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		if (ElementTypeTeller.isAssociation(typeDeclaration)) {
			try {
				new AssociationExporter(typeDeclaration, mapping, exportedModel)
						.exportAssociation();
				return false;
			} catch (ExportException e) {
				throw new RuntimeExportException(e);
			}
		}

		return true;
	}
}
