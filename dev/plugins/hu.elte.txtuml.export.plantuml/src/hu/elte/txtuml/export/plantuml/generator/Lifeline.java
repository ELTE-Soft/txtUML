package hu.elte.txtuml.export.plantuml.generator;

import org.eclipse.jdt.core.dom.FieldDeclaration;

/**
 * Represents a pair of lifeline declaration and its user-given position
 * priority.
 */
class Lifeline {

	private int priority;
	private FieldDeclaration lifelineDecl;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public FieldDeclaration getLifelineDecl() {
		return lifelineDecl;
	}

	public void setLifelineDecl(FieldDeclaration lifelineDecl) {
		this.lifelineDecl = lifelineDecl;
	}

}
