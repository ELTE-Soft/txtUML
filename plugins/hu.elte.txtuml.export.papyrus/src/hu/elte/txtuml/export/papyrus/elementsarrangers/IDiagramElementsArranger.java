package hu.elte.txtuml.export.papyrus.elementsarrangers;

/**
 * An Interface for arranging elements of a diagram 
 *
 * @author András Dobreff
 */
public interface IDiagramElementsArranger {
	
	/**
	 * Arranges the elements of the diagram
	 * @throws Throwable - The arranging algorithms may throw exceptions
	 */
	public void arrange() throws Throwable;
}
