package hu.elte.txtuml.export.plantuml.seqdiag;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTNode;

public abstract class BaseSeqdiagExporter<T extends ASTNode> {

	protected PrintWriter targetFile;

	public BaseSeqdiagExporter(PrintWriter targetFile) {
		this.targetFile = targetFile;
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
