package hu.elte.txtuml.export.plantuml.seqdiag;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class InteractionExporter extends BaseSeqdiagExporter<TypeDeclaration> {

	public InteractionExporter(PrintWriter targetFile) {
		super(targetFile);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if(curElement.getNodeType() == ASTNode.TYPE_DECLARATION)
			return true;
		else
			return false;
	}

	@Override
	public void preNext(TypeDeclaration curElement) {
		targetFile.println("@startuml");
	}

	@Override
	public void afterNext(TypeDeclaration curElement) {
		targetFile.println("@enduml");
	}
}
