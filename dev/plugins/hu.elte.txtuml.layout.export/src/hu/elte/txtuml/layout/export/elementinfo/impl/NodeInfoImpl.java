package hu.elte.txtuml.layout.export.elementinfo.impl;

import hu.elte.txtuml.api.model.StateMachine.Choice;
import hu.elte.txtuml.api.model.StateMachine.Initial;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
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
		RectangleObject res = new RectangleObject(toString(), getSpecialProperty());
		res.setPhantom(isPhantom());
		res.setVirtualPhantom(isVirtualPhantom());
		return res;	
	}

	@Override
	public boolean isPhantom() {
		return getElementClass() != null && 
				toString().startsWith("#phantom_");
	}
	
	@Override
	public boolean isBoxContainer() {
		return ElementExporter.isBoxContainer(getElementClass());
	}
	
	@Override
	public boolean isVirtualPhantom()
	{
		return getElementClass() == null && 
				toString().startsWith("#phantom_");
	}

	@Override
	public SpecialBox getSpecialProperty() {
		if (!isVirtualPhantom()){
			if(Initial.class.isAssignableFrom(getElementClass())) {
				return SpecialBox.Initial;
			}else if(Choice.class.isAssignableFrom(getElementClass())){
				return SpecialBox.Choice;
			}
		}

		return SpecialBox.None;
	}
}
