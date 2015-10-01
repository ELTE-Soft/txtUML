package hu.elte.txtuml.export.uml2.transform.visitors;

import hu.elte.txtuml.export.uml2.transform.importers.RegionElementImporter;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TransitionVisitor extends ASTVisitor{

	private RegionElementImporter regionElementImporter;
	private TypeDeclaration ownerDeclaration;
	
	public TransitionVisitor(RegionElementImporter regionElementImporter,
			TypeDeclaration ownerDeclaration) {
		this.regionElementImporter = regionElementImporter;
		this.ownerDeclaration = ownerDeclaration;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		if(ElementTypeTeller.isTransition(typeDeclaration) && !typeDeclaration.equals(this.ownerDeclaration)) {
			this.regionElementImporter.importTransition(typeDeclaration);
			return false;
		} else if(ElementTypeTeller.isState(typeDeclaration) && !typeDeclaration.equals(this.ownerDeclaration)) {
			return false;
		}
		return true;
	}
}
