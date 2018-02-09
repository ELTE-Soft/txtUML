package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.export.plantuml.generator.PlantUmlCompiler;

/**
 * Exporter implementation, which is responsible for exporting
 * {@link ModelClass} lifelines from the user-given {@link SequenceDiagram}.
 */
public class MethodDeclarationExporter extends ExporterBase<MethodDeclaration> {

	public MethodDeclarationExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (curElement.getNodeType() == ASTNode.METHOD_DECLARATION) {
			return ((MethodDeclaration) curElement).getName().getFullyQualifiedName().equals("initialize");
		}
		return false;
	}

	@Override
	public boolean preNext(MethodDeclaration curElement) {
		compiler.printLifelines();
		return true;
	}

	@Override
	public void afterNext(MethodDeclaration curElement) {
	}

}
