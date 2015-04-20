package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotPositionObjectException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.MyException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is used to wrap the arrange of a whole diagram.
 * 
 * @author Balázs Gregorics
 */
public class LayoutVisualize
{
	/***
	 * Objects to arrange.
	 */
	private Set<RectangleObject> _objects;
	/***
	 * Links to arrange
	 */
	private Set<LineAssociation> _assocs;
	
	/***
	 * Statements which arrange.
	 * Later the statements on the objects.
	 */
	private ArrayList<Statement> _statements;
	/***
	 * Statements on links.
	 */
	private ArrayList<Statement> _assocStatements;
	
	private DiagramType _diagramType;
	
	/***
	 * Get the current set of Objects.
	 * 
	 * @return Set of Objects. Can be null.
	 */
	public Set<RectangleObject> getObjects()
	{
		return _objects;
	}
	
	/***
	 * Get the current set of Links.
	 * 
	 * @return Set of Links. Can be null.
	 */
	public Set<LineAssociation> getAssocs()
	{
		return _assocs;
	}
	
	/**
	 * Get the finishing set of Statements on objects.
	 * 
	 * @return List of Statements on objects.
	 */
	public ArrayList<Statement> getStatements()
	{
		return _statements;
	}
	
	/**
	 * Get the finishing set of Statements on links.
	 * 
	 * @return List of Statements on links.
	 */
	public ArrayList<Statement> getAssocStatements()
	{
		return _assocStatements;
	}
	
	/**
	 * Layout algorithm initialize. Use load(), then arrange().
	 */
	public LayoutVisualize()
	{
		_objects = null;
		_assocs = null;
		_diagramType = DiagramType.Class;
	}
	
	/**
	 * Layout algorithm initialize for setting the reflexive links arrange. Use
	 * load(), then arrange().
	 * 
	 * @param type
	 *            The type of the diagram to arrange.
	 */
	public LayoutVisualize(DiagramType type)
	{
		_objects = null;
		_assocs = null;
		_diagramType = type;
	}
	
	/***
	 * Arranges the previously loaded model with the given statements.
	 * 
	 * @param stats
	 *            List of statements.
	 * @throws UnknownStatementException
	 *             Throws if an unknown statement is provided.
	 * @throws ConversionException
	 *             Throws if algorithm cannot convert certain type into
	 *             other type (Missing Statement upgrade?).
	 * @throws ConflictException
	 *             Throws if there are some conflicts in the user statements.
	 * @throws StatementTypeMatchException
	 *             Throws if any of the statements are not in correct format.
	 * @throws InternalException
	 *             Throws if any error occurs which should not happen. Contact
	 *             developer!
	 * @throws CannotPositionObjectException
	 *             Throws if the algorithm cannot find the place of an object.
	 * @throws CannotFindAssociationRouteException
	 *             Throws if the algorithm cannot find the route for a
	 *             association.
	 */
	public void arrange(ArrayList<Statement> stats) throws UnknownStatementException,
			ConflictException, ConversionException, InternalException,
			CannotPositionObjectException, CannotFindAssociationRouteException,
			StatementTypeMatchException
	{
		if (_objects == null)
			return;
		
		// set default statements
		DefaultStatements ds = new DefaultStatements(_objects, _assocs);
		stats.addAll(ds.value());
		
		// Split statements on assocs
		_assocStatements = StatementHelper.splitAssocs(stats, _assocs);
		_assocStatements = StatementHelper.reduceAssocs(_assocStatements);
		stats.removeAll(_assocStatements);
		
		// Transform special associations into statements
		stats.addAll(StatementHelper.transformAssocs(stats, _assocs));
		
		// Transform Phantom statements into Objects
		Set<String> phantoms = new HashSet<String>();
		phantoms.addAll(StatementHelper.extractPhantoms(stats));
		for (String p : phantoms)
			_objects.add(new RectangleObject(p));
		stats.removeAll(stats.stream()
				.filter(s -> s.getType().equals(StatementType.phantom))
				.collect(Collectors.toSet()));
		
		// Remove duplicates
		_statements = StatementHelper.reduceObjects(stats, _objects);
		
		// Check Obejct Statement Types
		for (Statement s : _statements)
		{
			if (!StatementHelper.isTypeChecked(s, _objects, _assocs))
				throw new StatementTypeMatchException("Types not match at statement: "
						+ s.toString() + "!");
		}
		// Check Association Statement Types
		for (Statement s : _assocStatements)
		{
			if (!StatementHelper.isTypeChecked(s, _objects, _assocs))
				throw new StatementTypeMatchException("Types not match at statement: "
						+ s.toString() + "!");
		}
		
		// Arrange objects
		Boolean isConflicted;
		do
		{
			isConflicted = false;
			
			try
			{
				ArrangeObjects ao = new ArrangeObjects(_objects, _statements);
				_objects = new HashSet<RectangleObject>(ao.value());
			}
			catch (ConflictException ex)
			{
				isConflicted = true;
				// Remove a weak statement if possible
				if (_statements.stream().anyMatch(s -> !s.isUserDefined()))
				{
					Statement toDelete = _statements
							.stream()
							.filter(s -> !s.isUserDefined())
							.max((s1, s2) ->
							{
								return Integer.compare(StatementHelper.getComplexity(s1),
										StatementHelper.getComplexity(s2));
							}).get();
					_statements.remove(toDelete);
				}
			}
		} while (isConflicted);
		
		// Set start-end positions for associations
		for (LineAssociation a : _assocs)
		{
			ArrayList<Point> al = new ArrayList<Point>();
			Point startend = null;
			Point endend = null;
			int ends = 2;
			for (RectangleObject o : _objects)
			{
				if (a.getFrom().equals(o.getName()))
				{
					startend = o.getPosition();
					--ends;
				}
				if (a.getTo().equals(o.getName()))
				{
					endend = o.getPosition();
					--ends;
				}
				
				if (ends == 0)
					break;
			}
			al.add(startend);
			al.add(endend);
			a.setRoute(al);
		}
		
		// Remove phantom objects
		_objects.removeAll(_objects.stream().filter(o -> phantoms.contains(o.getName()))
				.collect(Collectors.toSet()));
		
		// Arrange associations between objects
		if (_assocs.size() <= 0)
			return;
		
		ArrangeAssociations aa = new ArrangeAssociations(_objects, _assocs,
				_assocStatements, Helper.isReflexiveOnSameSide(_diagramType));
		_assocs = aa.value();
		
		// Transform objects according to link transformation
		Integer transformAmount = aa.getTransformAmount();
		Integer widthAmount = aa.getWidthAmount();
		for (RectangleObject o : _objects)
		{
			o.setPosition(Point.Multiply(o.getPosition(), transformAmount));
			o.setWidth(widthAmount);
		}
	}
	
	/***
	 * This function is used to load data to arrange.
	 * 
	 * @param os
	 *            Set of RectangleObjects to arrange on a grid.
	 * @param as
	 *            Set of LineAssociations to arrange between objects.
	 */
	public void load(Set<RectangleObject> os, Set<LineAssociation> as)
	{
		_objects = os;
		_assocs = as;
	}
	
	/**
	 * A simple use case of the <i>LayoutVisualize<i> class.
	 */
	public static void usage()
	{
		try
		{
			LayoutVisualize v = new LayoutVisualize();
			Set<RectangleObject> objs = new HashSet<RectangleObject>();
			// load objs
			Set<LineAssociation> links = new HashSet<LineAssociation>();
			// load links
			ArrayList<Statement> stats = new ArrayList<Statement>();
			// load stats
			
			v.load(objs, links);
			
			v.arrange(stats);
			Set<RectangleObject> o = v.getObjects();
			Set<LineAssociation> a = v.getAssocs();
			
			// Do sth with o and a
			System.out.println(o.toString());
			System.out.println(a.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		catch (MyException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Test function.
	 */
	public static void test()
	{
		System.out.println("--START--");
		
		try
		{
			LayoutVisualize v = new LayoutVisualize();
			
			System.out.println("/Set Objects/");
			
			Set<RectangleObject> testObjects = new HashSet<RectangleObject>();
			testObjects.add(new RectangleObject("A"));
			testObjects.add(new RectangleObject("B"));
			testObjects.add(new RectangleObject("C"));
			testObjects.add(new RectangleObject("D"));
			testObjects.add(new RectangleObject("E"));
			testObjects.add(new RectangleObject("F"));
			
			System.out.println("/Set Assocs/");
			
			Set<LineAssociation> testAssocs = new HashSet<LineAssociation>();
			
			// testAssocs.add(new LineAssociation("reflexive1", "A", "A"));
			// testAssocs.add(new LineAssociation("reflexive2", "E", "E"));
			testAssocs.add(new LineAssociation("LG1", "A", "B"));
			testAssocs.add(new LineAssociation("LG2", "B", "C"));
			testAssocs.add(new LineAssociation("LF1", "D", "E"));
			testAssocs.add(new LineAssociation("LF2", "D", "F"));
			
			System.out.println("/Load Data/");
			v.load(testObjects, testAssocs);
			
			System.out.println("/Set Statements/");
			
			ArrayList<Statement> stats = new ArrayList<Statement>();
			
			// stats.add(Statement.Parse("north(A, D)"));
			// stats.add(new Statement(StatementType.east, false, "A", "B"));
			
			System.out.println("/Arrange/");
			v.arrange(stats);
			
			System.out.println("/Output/");
			ArrayList<RectangleObject> objs = (ArrayList<RectangleObject>) v.getObjects()
					.stream().collect(Collectors.toList());
			objs.sort((a, b) ->
			{
				return a.getName().compareTo(b.getName());
			});
			for (RectangleObject o : objs)
				System.out.println(o.toString());
			ArrayList<LineAssociation> assocs = (ArrayList<LineAssociation>) v
					.getAssocs().stream().collect(Collectors.toList());
			assocs.sort((a, b) ->
			{
				return a.getId().compareTo(b.getId());
			});
			for (LineAssociation a : assocs)
				System.out.println(a.toString());
			
			System.out.println("\nObject statements:");
			for (Statement s : v.getStatements())
				System.out.println(s.toString());
			
			System.out.println("\nLinks statements:");
			for (Statement s : v.getAssocStatements())
				System.out.println(s.toString());
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		catch (MyException e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("--END--");
	}
}
