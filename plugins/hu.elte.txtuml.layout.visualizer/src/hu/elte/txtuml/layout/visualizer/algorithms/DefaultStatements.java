package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.algorithms.BreathFirstSearch.LabelType;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Link;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementLevel;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class generates the necessary default statements to auto layout a
 * diagram.
 * 
 * @author Balázs Gregorics
 *
 */
public class DefaultStatements
{
	
	private ArrayList<Statement> _statements;
	private Integer _gId;
	
	/**
	 * Constructor and run command for the generation of default statements.
	 * 
	 * @param os
	 *            Set of objects in the diagram.
	 * @param as
	 *            Set of links in the diagram.
	 * @param gid
	 *            The last used group id.
	 * @throws InternalException
	 *             Throws if some algorithm related error occurs. Contact with
	 *             your programmer in the nearest zoo for more details.
	 */
	public DefaultStatements(Set<RectangleObject> os, Set<LineAssociation> as, Integer gid)
			throws InternalException
	{
		_statements = new ArrayList<Statement>();
		_gId = gid;
		
		defaults(os, as);
	}
	
	/**
	 * Returns the maximum group Id.
	 * 
	 * @return the maximum group Id.
	 */
	public Integer getGroupId()
	{
		return _gId;
	}
	
	/**
	 * Returns the list of default statements.
	 * 
	 * @return List of default statements.
	 */
	public ArrayList<Statement> value()
	{
		return _statements;
	}
	
	private void defaults(Set<RectangleObject> os, Set<LineAssociation> as)
			throws InternalException
	{
		// Count degrees
		HashMap<String, HashSet<String>> accesses = new HashMap<String, HashSet<String>>();
		
		for (LineAssociation a : as)
		{
			if (a.isReflexive())
				continue;
			
			if (accesses.containsKey(a.getFrom()))
			{
				HashSet<String> temp = accesses.get(a.getFrom());
				if (!temp.contains(a.getTo()))
					temp.add(a.getTo());
				accesses.put(a.getFrom(), temp);
			}
			else
			{
				HashSet<String> temp = new HashSet<String>();
				temp.add(a.getTo());
				accesses.put(a.getFrom(), temp);
			}
			
			if (accesses.containsKey(a.getTo()))
			{
				HashSet<String> temp = accesses.get(a.getTo());
				if (!temp.contains(a.getFrom()))
					temp.add(a.getFrom());
				accesses.put(a.getTo(), temp);
			}
			else
			{
				HashSet<String> temp = new HashSet<String>();
				temp.add(a.getFrom());
				accesses.put(a.getTo(), temp);
			}
		}
		
		// Detect groups, arrange groups, arrange in groups
		_statements.addAll(detectGroups(accesses, as));
	}
	
	private Graph<String> buildGraph(HashMap<String, HashSet<String>> edges)
	{
		Graph<String> result = new Graph<String>();
		
		for (Entry<String, HashSet<String>> entry : edges.entrySet())
		{
			// add Node
			result.addNode(entry.getKey());
			
			// add Link
			for (String to : entry.getValue())
			{
				result.addLink(new Link<String>(entry.getKey(), to));
				result.addLink(new Link<String>(to, entry.getKey()));
			}
		}
		
		return result;
	}
	
	private ArrayList<Statement> detectGroups(HashMap<String, HashSet<String>> accesses,
			Set<LineAssociation> as) throws InternalException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		ArrayList<HashSet<String>> groups = new ArrayList<HashSet<String>>();
		
		HashSet<String> openToCheck = (HashSet<String>) Helper.cloneStringSet(accesses
				.keySet());
		Graph<String> G = buildGraph(accesses);
		
		if (openToCheck.size() != 0)
		{
			String start = openToCheck.stream().findFirst().get();
			do
			{
				BreathFirstSearch bfs = new BreathFirstSearch(G, start);
				
				HashSet<String> oneGroup = (HashSet<String>) bfs.value().entrySet()
						.stream()
						.filter(entry -> entry.getValue().equals(LabelType.discovered))
						.map(entry -> entry.getKey()).collect(Collectors.toSet());
				groups.add(oneGroup);
				
				openToCheck.removeAll(oneGroup);
				
				if (openToCheck.size() == 0)
					break;
				
				start = openToCheck.stream().findFirst().get();
				
			} while (true);
		}
		
		// Arrange groups
		groups.sort((a, b) ->
		{
			return -1 * Integer.compare(a.size(), b.size());
		});
		
		for (int i = 0; i < groups.size() - 1; ++i)
		{
			for (int j = 1; (i + j) < groups.size(); ++j)
			{
				++_gId;
				result.addAll(statementsBetweenGroups(groups.get(i), groups.get(i + j),
						_gId));
			}
		}
		
		return result;
	}
	
	private ArrayList<Statement> statementsBetweenGroups(HashSet<String> g1,
			HashSet<String> g2, Integer gid) throws InternalException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		for (String w : g1)
		{
			for (String e : g2)
			{
				result.add(new Statement(StatementType.west, StatementLevel.Medium, gid,
						w, e));
			}
		}
		
		return result;
	}
	
}
