package hu.elte.txtuml.layout.visualizer.algorithms.boxes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementLevel;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;
import hu.elte.txtuml.utils.Pair;

/**
 * The class that provides helping functions in the process of Overlap
 * Arrangement,
 * 
 * @author Bal�zs Gregorics
 *
 */
class OverlapHelper
{
	
	/**
	 * Returns the list of {@link Statement}s that fixes the current layout of
	 * boxes.
	 * 
	 * @param objs
	 *            Boxes in the model.
	 * @param old
	 *            Previous statement list.
	 * @param p_gid
	 *            Previously used group id.
	 * @return the list of {@link Statement}s that fixes the current layout of
	 *         boxes.
	 * @throws ConversionException
	 *             Throws if some conversions could not be made.
	 * @throws InternalException
	 *             See your programmer for more detail.
	 */
	public static Pair<List<Statement>, Integer> fixCurrentState(
			List<RectangleObject> objs, List<Statement> old, Integer p_gid)
			throws ConversionException, InternalException
	{
		List<Statement> result = new ArrayList<Statement>();
		Integer gid = p_gid;
		
		for (RectangleObject o1 : objs)
		{
			for (RectangleObject o2 : objs)
			{
				if (o1.equals(o2) || o1.getPosition().equals(o2.getPosition()))
					continue;
				
				for (Direction dir : Point.directionOfAll(o1.getPosition(),
						o2.getPosition()))
				{
					++gid;
					Statement s = new Statement(Helper.asStatementType(dir),
							StatementLevel.Low, gid, o1.getName(), o2.getName());
					if (!old.contains(s))
						result.add(s);
				}
			}
		}
		
		return new Pair<List<Statement>, Integer>(result, gid);
	}
	
	/**
	 * Returns true if there are overlapping boxes.
	 * 
	 * @param objs
	 *            Boxes in the model.
	 * @return true if there are overlapping boxes.
	 */
	public static boolean isThereOverlapping(List<RectangleObject> objs)
	{
		return isThereOverlapping(overlappingCount(objs));
	}
	
	/**
	 * Returns true if the number provided is considered overlapping.
	 * 
	 * @param oc
	 *            Number to check.
	 * @return true if the number provided is considered overlapping.
	 */
	public static boolean isThereOverlapping(Integer oc)
	{
		return oc > 1;
	}
	
	/**
	 * Returns the points and the boxes that are on it.
	 * 
	 * @param objs
	 *            Boxes in the model.
	 * @return the points and the boxes that are on it.
	 */
	public static HashMap<Point, HashSet<String>> overlaps(List<RectangleObject> objs)
	{
		HashMap<Point, HashSet<String>> overlaps = new HashMap<Point, HashSet<String>>();
		
		for (RectangleObject o : objs)
		{
			
			if (o.isPhantom())
				continue;
			
			if (overlaps.containsKey(o.getPosition()))
			{
				HashSet<String> temp = overlaps.get(o.getPosition());
				temp.add(o.getName());
				
				overlaps.put(o.getPosition(), temp);
			}
			else
			{
				HashSet<String> temp = new HashSet<String>();
				temp.add(o.getName());
				overlaps.put(o.getPosition(), temp);
			}
		}
		
		return overlaps;
	}
	
	/**
	 * Returns the number of overlapping boxes.
	 * 
	 * @param objs
	 *            Boxes in the model.
	 * @return the number of overlapping boxes.
	 */
	public static Integer overlappingCount(List<RectangleObject> objs)
	{
		Integer count = 0;
		for (Entry<Point, HashSet<String>> entry : overlaps(objs).entrySet())
		{
			if (entry.getValue().size() > 1)
			{
				count += entry.getValue().size();
			}
		}
		
		return count;
	}
	
	private static List<Pair<String, String>> _pairs;
	
	/**
	 * Returns the number of available pairs of overlapping boxes.
	 * 
	 * @param objs
	 *            Boxes in the model.
	 * @return the number of available pairs of overlapping boxes.
	 * @throws InternalException
	 *             Throws if something is not supposed to happen.
	 */
	public static Integer pairsCount(List<RectangleObject> objs) throws InternalException
	{
		if (_pairs != null)
			return _pairs.size();
		
		_pairs = new ArrayList<Pair<String, String>>();
		
		for (int i = 0; i < objs.size(); ++i)
		{
			for (int j = i + 1; j < objs.size(); ++j)
			{
				if (objs.get(i).getPosition().equals(objs.get(j).getPosition()))
				{
					_pairs.add(new Pair<String, String>(objs.get(i).getName(), objs
							.get(j).getName()));
				}
			}
		}
		
		return _pairs.size();
	}
	
	/**
	 * All of the possible combinations.
	 */
	private static List<List<Pair<String, String>>> _combinations;
	
	/**
	 * Returns the list of pairs of num amount of boxes.
	 * 
	 * @param num
	 *            The number of boxes to make pairs of.
	 * @return the list of pairs of num amount of boxes.
	 */
	public static List<List<Pair<String, String>>> selectPairs(Integer num)
	{
		if (_combinations != null)
			return _combinations.stream().filter(lst -> lst.size() == num)
					.collect(Collectors.toList());
		
		_combinations = new ArrayList<List<Pair<String, String>>>();
		Integer count = 0;
		
		for (Pair<String, String> p : _pairs)
		{
			List<List<Pair<String, String>>> addToCombinations = new ArrayList<List<Pair<String, String>>>();
			for (List<Pair<String, String>> list : _combinations)
			{
				List<Pair<String, String>> temp = new ArrayList<Pair<String, String>>(
						list);
				temp.add(p);
				addToCombinations.add(temp);
			}
			
			List<Pair<String, String>> temp = new ArrayList<Pair<String, String>>();
			temp.add(p);
			_combinations.add(temp);
			
			count = count + 1;
			System.err.println(count + "/" + _pairs.size());
			
			for (List<Pair<String, String>> list : addToCombinations)
			{
				_combinations.add(list);
			}
		}
		
		return _combinations.stream().filter(lst -> lst.size() == num)
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns a list of statement for the given pairs and given directions.
	 * 
	 * @param pairs
	 *            list of pairs to make the statements on.
	 * @param fn
	 *            a base-4 number representing the directions of the statements
	 *            to make.
	 * @param p_gid
	 *            the previously used group id.
	 * @return a list of statement for the given pairs and given directions.
	 * @throws InternalException
	 *             Throws if something bad happened.
	 * @throws ConversionException
	 *             Throws if a {@link Direction} could not be converted into
	 *             {@link StatementType}.
	 */
	public static List<Statement> getStatementsForPairs(List<Pair<String, String>> pairs,
			FourNumber fn, Integer p_gid) throws InternalException, ConversionException
	{
		List<Statement> result = new ArrayList<Statement>();
		Integer gid = new Integer(p_gid);
		
		for (int i = 0; i < pairs.size(); ++i)
		{
			++gid;
			result.add(new Statement(Helper.asStatementType(Direction.fromInteger(fn
					.getBit(i))), StatementLevel.Medium, gid, pairs.get(i).getFirst(), pairs
					.get(i).getSecond()));
		}
		
		return result;
	}
	
}
