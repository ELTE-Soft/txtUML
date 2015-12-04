package hu.elte.txtuml.export.uml2.transform.exporters;

import hu.elte.txtuml.export.uml2.transform.visitors.TransitionVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.VertexVisitor;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;

public class RegionExporter {

	private final ModelExporter modelExporter;

	public RegionExporter(ModelExporter modelExporter) {
		this.modelExporter = modelExporter;
	}

	public void exportRegion(TypeDeclaration ownerDeclaration,
			StateMachine stateMachine, Region region) {
		exportVertices(ownerDeclaration, stateMachine, region);
		exportTransitions(ownerDeclaration, stateMachine, region);
	}

	private void exportVertices(TypeDeclaration ownerDeclaration,
			StateMachine stateMachine, Region region) {
		VertexVisitor visitor = new VertexVisitor(new VertexExporter(
				modelExporter, stateMachine, region), ownerDeclaration);
		ownerDeclaration.accept(visitor);
	}

	private void exportTransitions(TypeDeclaration ownerDeclaration,
			StateMachine stateMachine, Region region) {
		TransitionVisitor visitor = new TransitionVisitor(
				new TransitionExporter(modelExporter, stateMachine, region),
				ownerDeclaration);
		ownerDeclaration.accept(visitor);
	}

}
