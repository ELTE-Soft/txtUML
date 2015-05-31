package hu.elte.txtuml.layout.export.elementinfo;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.impl.ElementInfoImpl;
import hu.elte.txtuml.layout.export.elementinfo.impl.InvalidElementInfoImpl;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;

/**
 * General information holder about a diagram layout description element.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public interface ElementInfo {

	/**
	 * Creates a default info holder for the given element. The info holder will
	 * show that the type of the element is <code>Unknown</code>.
	 */
	static ElementInfo createDefault(Class<? extends LayoutElement> elementClass) {
		return new ElementInfoImpl(elementClass);
	}

	/**
	 * Creates an info holder for the given element. The info holder will show
	 * that the type of the element is <code>Invalid</code>.
	 */
	static ElementInfo createInvalid(Class<? extends LayoutElement> elementClass) {
		return new InvalidElementInfoImpl(elementClass);
	}

	/**
	 * @return whether <code>getType</code> method returns anything different
	 *         from <code>Invalid</code>
	 */
	default boolean isValid() {
		return getType() != ElementType.Invalid;
	}

	/**
	 * @return the type of the element this object holds info about
	 */
	ElementType getType();

	/**
	 * @return the diagram type in which the element this object holds info
	 *         about may occur. Null if <code>getType</code> method returns
	 *         <code>ElementType.Invalid</code>
	 */
	DiagramType getDiagType();

	/**
	 * @return the element type this object holds info about
	 */
	Class<? extends LayoutElement> getElementClass();

	/**
	 * @return the string representation of the element this object holds info
	 *         about
	 */
	@Override
	String toString();

	/**
	 * Casts to <code>LinkInfo</code>.
	 * 
	 * @return this object casted to LinkInfo. Is <code>null</code> if
	 *         <code>getType</code> method does not return
	 *         <code>ElementType.Link</code>.
	 */
	default LinkInfo asLinkInfo() {
		return null;
	}

	/**
	 * Casts to <code>LinkGroupInfo</code>.
	 * 
	 * @return this object casted to LinkGroupInfo. Is <code>null</code> if
	 *         <code>getType</code> method does not return
	 *         <code>LinkGroup</code>.
	 */
	default LinkGroupInfo asLinkGroupInfo() {
		return null;
	}

	/**
	 * Casts to <code>NodeInfo</code>.
	 * 
	 * @return this object casted to NodeInfo. Is <code>null</code> if
	 *         <code>getType</code> method does not return <code>Node</code>.
	 */
	default NodeInfo asNodeInfo() {
		return null;
	}

	/**
	 * Casts to <code>NodeGroupInfo</code>.
	 * 
	 * @return this object casted to NodeGroupInfo. Is <code>null</code> if
	 *         <code>getType</code> method does not return
	 *         <code>NodeGroup</code>.
	 */
	default NodeGroupInfo asNodeGroupInfo() {
		return null;
	}

	/**
	 * Two <code>ElementInfo</code> objects equal if they hold information about
	 * the same diagram layout description element. As an abstraction, this
	 * object will
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
