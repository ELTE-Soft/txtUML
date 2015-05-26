package hu.elte.txtuml.layout.visualizer.algorithms.links;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementLevel;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation.RouteConfig;
import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultAssocStatements
{
	
	private ArrayList<Statement> _result;
	private Integer _gId;
	
	public DefaultAssocStatements(Integer gid, ArrayList<Statement> stat,
			ArrayList<LineAssociation> assocs) throws InternalException
	{
		_result = stat;
		_gId = gid;
		
		predefinePriorities(assocs);
	}
	
	public Integer getGroupId()
	{
		return _gId;
	}
	
	public ArrayList<Statement> value()
	{
		return _result;
	}
	
	private void predefinePriorities(ArrayList<LineAssociation> assocs)
			throws InternalException
	{
		// select min priority
		Set<Statement> priorities = _result.stream()
				.filter(s -> s.getType().equals(StatementType.priority))
				.collect(Collectors.toSet());
		Optional<Integer> minPriority = _result.stream()
				.filter(s -> s.getType().equals(StatementType.priority))
				.map(s -> Integer.valueOf(s.getParameter(1))).min((i, j) ->
				{
					return Integer.compare(i, j);
				});
		if (minPriority.isPresent())
		{
			Integer min = minPriority.get();
			if ((assocs.size() - priorities.size()) >= min)
			{
				// not good, ++ every prior
				Integer alterAmount = (assocs.size() - priorities.size());
				for (Statement s : _result)
				{
					if (s.getType().equals(StatementType.priority))
					{
						s.setParameter(1, ""
								+ (Integer.valueOf(s.getParameter(1)) + alterAmount));
					}
				}
			}
		}
		
		Integer actPrior = 0;
		ArrayList<LineAssociation> orderedAssocs = (ArrayList<LineAssociation>) assocs
				.stream().collect(Collectors.toList());
		orderedAssocs.sort((a1, a2) ->
		{
			return Double.compare(distanceOfEnds(a1), distanceOfEnds(a2));
		});
		for (LineAssociation a : orderedAssocs)
		{
			++_gId;
			if (!priorities.stream().anyMatch(s -> s.getParameter(0).equals(a.getId())))
			{
				_result.add(new Statement(StatementType.priority, StatementLevel.Low,
						_gId, a.getId(), actPrior.toString()));
				++actPrior;
			}
		}
	}
	
	private Double distanceOfEnds(LineAssociation a)
	{
		Point one = a.getRoute(RouteConfig.START);
		Point two = a.getRoute(RouteConfig.END);
		
		return (Point.Substract(one, two)).length();
	}
}
