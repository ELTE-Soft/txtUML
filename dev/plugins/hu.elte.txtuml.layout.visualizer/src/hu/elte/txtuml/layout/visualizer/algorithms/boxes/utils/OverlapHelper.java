package hu.elte.txtuml.layout.visualizer.algorithms.boxes.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import hu.elte.txtuml.layout.visualizer.algorithms.utils.Helper;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementLevel;
import hu.elte.txtuml.utils.Pair;

/**
 * The class that provides helping functions in the process of Overlap
 * Arrangement,
 */
public class OverlapHelper
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
}
