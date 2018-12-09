package hu.elte.txtuml.seqdiag.export.plantuml.generator;

import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * Represents a sequence diagram lifeline.
 */

class Lifeline {

	private LifelineDeclaration declaration;
	private String name;
	private boolean isActive;
	//private List<String> Names;

	Lifeline(final LifelineDeclaration declaration) {
		this.declaration = declaration;
		this.name = ((VariableDeclarationFragment) declaration.getLifelineDecl().fragments().get(0)).toString();
		//this.previousNames = null;
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

	public void SetName(String newName) {
		if(newName != null) {
			//this.Names.add(name);
			this.name = newName;
		}
	}
	/*
	public void ChangeBackName(){
		if(this.Names.empty()){
			return;
		}
		
		this.name = Names.pop();
	}*/
	
}
