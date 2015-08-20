package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.api.layout.elements.LayoutElement;
import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.ElementType;

/**
 * An {@link ElementInfo} implementation to show that the type of the
 * element is <code>Invalid</code>.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class InvalidElementInfoImpl extends
		ElementInfoImpl implements ElementInfo {

	public InvalidElementInfoImpl(Class<? extends LayoutElement> elementClass) {
		super(elementClass);
	}

	@Override
	public ElementType getType() {
		return ElementType.Invalid;
	}

}
