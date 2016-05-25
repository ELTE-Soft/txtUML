package hu.elte.txtuml.layout.visualizer.model.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public class DiagramTreeEnumerator implements Iterable<Diagram>, Iterator<Diagram> {

	private ArrayList<Diagram> _diagrams;
	private Integer currentIndex = 0;
	
	public DiagramTreeEnumerator(Diagram diag) {
		_diagrams = new ArrayList<Diagram>(getChildren(diag));
	}
	
	private ArrayList<Diagram> getChildren(Diagram wrapper)
	{
		ArrayList<Diagram> result = new ArrayList<Diagram>();
		
		result.add(wrapper);
		
		for(RectangleObject box : wrapper.Objects)
		{
			if(box.hasInner())
			{
				result.addAll(getChildren(box.getInner()));
			}
		}
		
		return result;
	}
	
	@Override
	public Iterator<Diagram> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return currentIndex < _diagrams.size();
	}

	@Override
	public Diagram next() {
		if(!this.hasNext()) {
            throw new NoSuchElementException();
        }

		return _diagrams.get(currentIndex++);
	}
}
