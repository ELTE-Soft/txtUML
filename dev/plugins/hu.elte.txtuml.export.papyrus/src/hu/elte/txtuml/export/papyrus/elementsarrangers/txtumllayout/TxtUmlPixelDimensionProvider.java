package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.SpecialBox;
import hu.elte.txtuml.utils.Pair;

public class TxtUmlPixelDimensionProvider implements IPixelDimensionProvider {

	@Override
	public Pair<Width, Height> getPixelDimensionsFor(RectangleObject box) {
		// TODO Auto-generated method stub

		if (!box.hasInner()) {
			if(box.isSpecial() && box.getSpecial().equals(SpecialBox.Initial))
				return Pair.of(new Width(20), new Height(20));
			
			//TODO: search among simple elements / default value
			return Pair.of(new Width(40), new Height(40));
		} else // if(box.hasInner())
		{
			Integer wpx = box.getInner().getWidth() * box.getInner().getPixelGridHorizontal();
			Integer hpx = box.getInner().getHeight() * box.getInner().getPixelGridVertical();

			return Pair.of(new Width(wpx), new Height(hpx));
		}
	}

}
