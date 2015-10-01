package hu.elte.txtuml.export.uml2.transform.importers;

import hu.elte.txtuml.export.uml2.transform.visitors.TransitionVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.VertexVisitor;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Region;

public class RegionImporter {
	private ModelImporter modelImporter;
	
	public RegionImporter(ModelImporter modelImporter) {
		this.modelImporter = modelImporter;
	}
	
	public Region importRegion(TypeDeclaration ownerDeclaration, Region region) {
		importVertices(ownerDeclaration, region);
		importTransitions(ownerDeclaration, region);
		return region;
	}

	private void importTransitions(TypeDeclaration ownerDeclaration, Region region) {
		TransitionVisitor visitor = new TransitionVisitor(
				new RegionElementImporter(this.modelImporter, region),
				ownerDeclaration);
		ownerDeclaration.accept(visitor);
	}

	private void importVertices(TypeDeclaration ownerDeclaration, Region region) {
		VertexVisitor visitor = new VertexVisitor(
				new RegionElementImporter(this.modelImporter, region),
				ownerDeclaration);
		ownerDeclaration.accept(visitor);
	}
}
