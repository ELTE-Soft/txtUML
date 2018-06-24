package hu.elte.txtuml.seqdiag.export.plantuml.generator;

import org.eclipse.jdt.core.dom.FieldDeclaration;

/**
 * Represents a pair of lifeline declaration and its user-given position
 * priority.
 */
class LifelineDeclaration {

	private int priority;
	private FieldDeclaration lifelineDecl;

	public LifelineDeclaration(final FieldDeclaration lifeline, int priority) {
		this.lifelineDecl = lifeline;
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public FieldDeclaration getLifelineDecl() {
		return lifelineDecl;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
