package hu.elte.txtuml.layout.visualizer.helpers;

import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.model.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Helper
{
	
	public static HashMap<String, ArrayList<String>> cloneMap(
			HashMap<String, ArrayList<String>> toClone)
	{
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		
		for (Entry<String, ArrayList<String>> entry : toClone.entrySet())
		{
			result.put(new String(entry.getKey()), cloneList(entry.getValue()));
		}
		
		return result;
	}
	
	public static ArrayList<String> cloneList(ArrayList<String> toClone)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for (String s : toClone)
		{
			result.add(new String(s));
		}
		
		return result;
	}
	
	public static boolean tryParseInt(String value)
	{
		try
		{
			Integer.parseInt(value);
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}
	
	public static Direction asDirection(StatementType ty) throws ConversionException
	{
		switch (ty)
		{
			case north:
				return Direction.north;
			case south:
				return Direction.south;
			case east:
				return Direction.east;
			case west:
				return Direction.west;
			default:
				throw new ConversionException("Cannot convert type " + ty
						+ " to Direction!");
		}
	}
	
}
