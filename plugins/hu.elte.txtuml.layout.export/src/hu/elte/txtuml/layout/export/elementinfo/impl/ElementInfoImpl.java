package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;

/**
 * Default implementation for {@link ElementInfo}.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
abstract class ElementInfoImpl implements ElementInfo {

	private final Class<?> elementClass;
	private final DiagramType diagType;
	private final String asString;

	public ElementInfoImpl(Class<?> elementClass) {
		this(elementClass, DiagramType.Unknown, "");
	}

	public ElementInfoImpl(Class<?> elementClass,
			DiagramType diagType, String asString) {
		this.elementClass = elementClass;
		this.diagType = diagType;
		this.asString = asString;
	}

	@Override
	public boolean beingExported() {
		return false;
	}

	@Override
	public DiagramType getDiagType() {
		return diagType;
	}

	@Override
	public Class<?> getElementClass() {
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
