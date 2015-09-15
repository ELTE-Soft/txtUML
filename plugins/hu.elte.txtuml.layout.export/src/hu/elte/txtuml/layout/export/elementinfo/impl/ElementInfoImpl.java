package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.api.layout.elements.LayoutElement;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.ElementType;

/**
 * Default implementation for {@link ElementInfo}. Shows that the type of the
 * element is <code>Unknown</code>.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class ElementInfoImpl implements ElementInfo {

	private final Class<? extends LayoutElement> elementClass;
	private final DiagramType diagType;
	private final String asString;

	public ElementInfoImpl(Class<? extends LayoutElement> elementClass) {
		this(elementClass, DiagramType.Unknown, "");
	}

	public ElementInfoImpl(Class<? extends LayoutElement> elementClass,
			DiagramType diagType, String asString) {
		this.elementClass = elementClass;
		this.diagType = diagType;
		this.asString = asString;
	}

	@Override
	public ElementType getType() {
		return ElementType.Unknown;
	}

	@Override
	public DiagramType getDiagType() {
		return diagType;
	}

	@Override
	public Class<? extends LayoutElement> getElementClass() {
		return elementClass;
	}

	@Override
	public String toString() {
		return asString;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementInfoImpl other = (ElementInfoImpl) obj;
		if (elementClass == null) {
			if (other.elementClass != null)
				return false;
		} else if (elementClass != other.elementClass)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return elementClass.hashCode();
	}

}
