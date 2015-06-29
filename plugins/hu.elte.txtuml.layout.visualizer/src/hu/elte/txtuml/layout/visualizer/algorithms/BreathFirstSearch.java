package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.algorithms.boxes.bellmanfordhelpers.Queue;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Graph;

import java.util.HashMap;

/**
 * This class provides an implementation to the Breath-First search algorithm.
 * 
 * @author Balázs Gregorics
 * @param <T>
 *            Type of the graph nodes.
 *
 */
class BreathFirstSearch<T>
{
	/**
	 * Types of discovery property.
	 * 
	 * @author Balázs Gregorics
	 *
	 */
	public enum LabelType
	{
		/**
		 * Not yet discovered.
		 */
		not_discovered,
		/**
		 * Already discovered.
		 */
		discovered
	}
	
	/**
	 * The map representing which nodes are discovered and which are not.
	 */
	private HashMap<T, LabelType> _labels;
	
	/**
	 * Method to initialize and run the Breath-First search algorithm.
	 * 
	 * @param G
	 *            the {@link Graph} to search in.
	 * @param start
	 *            the node to start from.
	 */
	public BreathFirstSearch(Graph<T> G, T start)
	{
		// Init
		Queue<T> Q = new Queue<T>();
		_labels = new HashMap<T, BreathFirstSearch.LabelType>();
		for (T node : G.Nodes)
		{
			_labels.put(node, LabelType.not_discovered);
		}
		
		Q.enqueue(start);
		_labels.put(start, LabelType.discovered);
		
		// Start
		while (!Q.isEmpty())
		{
			T v = Q.dequeue();
			for (T w : G.adjacentNodes(v))
			{
				if (_labels.get(w).equals(LabelType.not_discovered))
				{
					Q.enqueue(w);
					_labels.put(w, LabelType.discovered);
				}
			}
		}
	}
	
	/**
	 * Returns a {@link HashMap} of nodes and their access property from start
	 * node.
	 * 
	 * @return a {@link HashMap} of nodes and their access property from start
	 *         node.
	 */
	public HashMap<T, LabelType> value()
	{
		return _labels;
	}
}
