package hu.elte.txtuml.layout.export.elementinfo;

/**
 * General information holder about a diagram layout description element.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
public interface ElementInfo {

	/**
	 * @return the element type this object holds info about
	 */
	Class<?> getElementClass();

	/**
	 * @return the string representation of the element this object holds info
	 *         about
	 */
	@Override
	String toString();

	/**
	 * @return whether the group this object holds info about is currently being
	 *         exported; in case of non-group elements (nodes or links), it
	 *         always returns false
	 */
	boolean beingExported();

	/**
	 * Two <code>ElementInfo</code> objects equal if they hold information about
	 * the same diagram layout description element.
	 * 
	 * @param obj
	 *            the other object to be compared with this info holder
	 * @return <code>true</code> if this info holder equals <code>obj</code>,
	 *         <code>false</code> otherwise
	 */
	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

}
