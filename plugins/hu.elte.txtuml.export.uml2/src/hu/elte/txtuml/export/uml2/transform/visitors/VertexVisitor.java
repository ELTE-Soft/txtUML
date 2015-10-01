package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.importers.RegionElementImporter;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class VertexVisitor extends ASTVisitor {

	RegionElementImporter regionElementImporter;
	TypeDeclaration ownerDeclaration;
	
	public VertexVisitor(RegionElementImporter regionElementImporter,
			TypeDeclaration ownerDeclaration) {
		this.regionElementImporter = regionElementImporter;
		this.ownerDeclaration = ownerDeclaration;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		if(ElementTypeTeller.isVertex(typeDeclaration) && !typeDeclaration.equals(this.ownerDeclaration)) {
			try {
				this.regionElementImporter.importVertex(typeDeclaration);
			} catch (ImportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}
}
