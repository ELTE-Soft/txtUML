package hu.elte.txtuml.export.uml2.transform.exporters;

import hu.elte.txtuml.export.uml2.transform.visitors.TransitionVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.VertexVisitor;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Region;

public class RegionExporter {

	private final ModelExporter modelExporter;

	public RegionExporter(ModelExporter modelExporter) {
		this.modelExporter = modelExporter;
	}

	public void exportRegion(TypeDeclaration ownerDeclaration, Region region) {
		exportVertices(ownerDeclaration, region);
		exportTransitions(ownerDeclaration, region);
	}

	private void exportVertices(TypeDeclaration ownerDeclaration, Region region) {
		VertexVisitor visitor = new VertexVisitor(new VertexExporter(
				modelExporter, region), ownerDeclaration);
		ownerDeclaration.accept(visitor);
	}

	private void exportTransitions(TypeDeclaration ownerDeclaration,
			Region region) {
		TransitionVisitor visitor = new TransitionVisitor(
				new TransitionExporter(modelExporter, region), ownerDeclaration);
		ownerDeclaration.accept(visitor);
	}

}
