package hu.elte.txtuml.api.model.seqdiag;

/**
 * Interaction Class, representing a full Interaction( SequenceDiagram )
 *
 */
public abstract class Interaction implements Runnable {
	
	protected Interaction(){}
	
	public void initialize(){};
	
	public abstract void run();
}
