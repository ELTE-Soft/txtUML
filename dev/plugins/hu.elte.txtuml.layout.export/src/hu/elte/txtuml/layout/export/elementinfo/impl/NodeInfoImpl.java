package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.api.model.StateMachine.Initial;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.SpecialBox;

/**
 * Default implementation for {@link NodeInfo}.
 */
public class NodeInfoImpl extends ElementInfoImpl implements NodeInfo {

	public NodeInfoImpl(Class<?> elementClass, String asString) {
		super(elementClass, asString);
	}

	@Override
	public RectangleObject convert() {
		return new RectangleObject(toString(), getSpecialProperty());
	}

	@Override
	public boolean isPhantom() {
		return toString().startsWith("#phantom_");
	}

	@Override
	public SpecialBox getSpecialProperty() {
		if (Initial.class.isAssignableFrom(getElementClass())) {
			return SpecialBox.Initial;
		}

		return SpecialBox.None;
	}

}
