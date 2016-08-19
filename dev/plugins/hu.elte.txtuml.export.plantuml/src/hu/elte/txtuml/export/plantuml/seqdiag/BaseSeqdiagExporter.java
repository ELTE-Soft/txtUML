package hu.elte.txtuml.export.plantuml.seqdiag;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.export.plantuml.generator.PlantUmlGenerator;

public abstract class BaseSeqdiagExporter<T extends ASTNode> {

	protected PrintWriter targetFile;
	protected PlantUmlGenerator generator;

	public BaseSeqdiagExporter(PrintWriter targetFile,PlantUmlGenerator generator) {
		this.targetFile = targetFile;
		this.generator = generator;
	}

	public abstract boolean validElement(ASTNode curElement);

	public abstract void preNext(T curElement);

	public abstract void afterNext(T curElement);

	public boolean visit(T curElement) {
		if (this.validElement(curElement)) {
			this.preNext(curElement);
		}

		return true;
	}

	public boolean endVisit(T curElement) {
		if (this.validElement(curElement)) {
			this.afterNext(curElement);
			return true;
		}
		return false;
	}
}
