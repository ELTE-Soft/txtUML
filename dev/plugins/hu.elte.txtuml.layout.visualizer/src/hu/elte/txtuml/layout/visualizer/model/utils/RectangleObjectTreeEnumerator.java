package hu.elte.txtuml.layout.visualizer.model.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * Class that helps enumerate {@link RectangleObject}s.
 */
public class RectangleObjectTreeEnumerator implements Iterable<RectangleObject>, Iterator<RectangleObject> {

	private ArrayList<RectangleObject> _objects;
	private Integer currentIndex = 0;
	
	/**
	 * Create {@link RectangleObjectTreeEnumerator}.
	 * @param objs {@link RectangleObject}s to work with.
	 */
	public RectangleObjectTreeEnumerator(Set<RectangleObject> objs) {
		_objects = new ArrayList<RectangleObject>(getChildren(objs));
		_objects.sort((box1, box2) -> box1.getName().compareTo(box2.getName()));
	}
	
	private ArrayList<RectangleObject> getChildren(Set<RectangleObject> parents)
	{
		ArrayList<RectangleObject> result = new ArrayList<RectangleObject>();
		
		for(RectangleObject box : parents)
		{
			if(box.hasInner())
			{
				result.addAll(getChildren(box.getInner().Objects));
			}
			
			result.add(box);
		}
		
		return result;
	}
	
	@Override
	public Iterator<RectangleObject> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return currentIndex < _objects.size();
	}

	@Override
	public RectangleObject next() {
		if(!this.hasNext()) {
            throw new NoSuchElementException();
        }
        
		//++currentIndex;
		return _objects.get(currentIndex++);
	}
}
