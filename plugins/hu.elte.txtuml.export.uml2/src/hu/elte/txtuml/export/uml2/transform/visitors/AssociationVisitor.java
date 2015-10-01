package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.importers.AssociationImporter;
import hu.elte.txtuml.export.uml2.transform.importers.ModelImporter;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Instances of this class visit associations in a source txtUML model.
 * @author Ádám Ancsin
 *
 */
public class AssociationVisitor extends ASTVisitor{
	/**
	 * The model importer used.
	 */
	private ModelImporter modelImporter;
	
	/**
	 * Creates an <code>AssociationVisitor</code> instance.
	 * 
	 * @param modelImporter
	 *            The model importer used for importing.
	 */
	public AssociationVisitor(ModelImporter modelImporter) {
		super();
		this.modelImporter = modelImporter;
	}
	
	/**
	 * Visits a TypeDeclaration ASTNode in the AST. If it represents a txtUML
	 * association, imports it and does not continue the visit on it's child
	 * nodes. Otherwise, it ignores the node and continues the visit.
	 */
	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		if(ElementTypeTeller.isAssociation(typeDeclaration)) {
			try {
				new AssociationImporter(typeDeclaration, this.modelImporter.getImportedModel()).importAssociation();
				return false;
			} catch (ImportException e) {
				// TODO: error-handling - cannot throw exception from this method
				// because original implementation does not throw Exception
			}
		}
		
		return true;
	}
}
