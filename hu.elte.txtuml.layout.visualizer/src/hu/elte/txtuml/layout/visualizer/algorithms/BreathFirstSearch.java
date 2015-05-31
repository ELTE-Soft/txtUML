package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.algorithms.boxes.bellmanfordhelpers.Queue;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Graph;

import java.util.HashMap;

class BreathFirstSearch
{
	public enum LabelType
	{
		not_discovered,
		discovered
	}
	
	private HashMap<String, LabelType> _labels;
	
	public BreathFirstSearch(Graph<String> G, String start)
	{
		Queue<String> Q = new Queue<String>();
		_labels = new HashMap<String, BreathFirstSearch.LabelType>();
		for (String node : G.Nodes)
		{
			_labels.put(node, LabelType.not_discovered);
		}
		
		Q.enqueue(start);
		_labels.put(start, LabelType.discovered);
		
		while (!Q.isEmpty())
		{
			String v = Q.dequeue();
			for (String w : G.adjacentNodes(v))
			{
				if (_labels.get(w).equals(LabelType.not_discovered))
				{
					Q.enqueue(w);
					_labels.put(w, LabelType.discovered);
				}
			}
		}
	}
	
	public HashMap<String, LabelType> value()
	{
		return _labels;
	}
}
