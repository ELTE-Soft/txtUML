package hu.elte.txtuml.seqdiag.export.plantuml.generator;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * Represents a sequence diagram lifeline.
 */
class Lifeline {

	private LifelineDeclaration declaration;
	private String name;
	private boolean isActive;

	Lifeline(final LifelineDeclaration declaration) {
		this.declaration = declaration;
		this.name = ((VariableDeclarationFragment) declaration.getLifelineDecl().fragments().get(0)).toString();
	}

	public void activate(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getName() {
		return name;
	}

	public int getPriority() {
		return declaration.getPriority();
	}

	public FieldDeclaration getLifelineDeclaration() {
		return declaration.getLifelineDecl();
	}

	public void processed() {
		declaration.setPriority(-1);
	}

}
