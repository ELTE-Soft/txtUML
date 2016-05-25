package hu.elte.txtuml.layout.visualizer.algorithms.links.utils;

import java.util.Comparator;

import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

public class LinkComparator implements Comparator<LineAssociation> {

	@Override
	public int compare(LineAssociation o1, LineAssociation o2) {
		return Integer.compare(o1.getPriority(), o2.getPriority());
	}

}
