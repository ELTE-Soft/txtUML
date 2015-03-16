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
import hu.elte.txtuml.layout.visualizer.helpers.MyModel;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
	
	/***
	 * Layout algorithm initialize. Use load(), then arrange().
	 */
	public LayoutVisualize()
	{
		_objects = null;
		_assocs = null;
	}
	
	/***
	 * Arranges the previously loaded model with the given statements.
	 * 
	 * @param stats
	 *            List of statements.
	 * @throws UnknownStatementException
	 * @throws ConversionException
	 *             Throws if algorithm cannot convert certain type into
	 *             other type (Missing Statement upgrade?).
	 * @throws ConflictException
	 * @throws StatementTypeMatchException
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
			ConflictException, ConversionException, StatementTypeMatchException,
			InternalException, CannotPositionObjectException,
			CannotFindAssociationRouteException
	{
		if (_objects == null)
			return;
		
		// Unfold groups
		// stats = Statement.UnfoldGroups(stats);
		
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
		ArrangeObjects ao = new ArrangeObjects(_objects, _statements);
		_objects = new HashSet<RectangleObject>(ao.value());
		
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
				_assocStatements);
		_assocs = aa.value();
		
		// Transform objects according to link transformation
		Integer transformAmount = aa.getTransformAmount();
		for (RectangleObject o : _objects)
		{
			o.setPosition(Point.Multiply(o.getPosition(), transformAmount));
		}
	}
	
	/***
	 * This function loads the model to arrange.
	 * 
	 * @param pair
	 *            A pair of sets about the Objects and Links of the model.
	 */
	public void load(Set<RectangleObject> os, Set<LineAssociation> as)
	{
		_objects = os;
		_assocs = as;
	}
	
	public static void usage()
	{
		try
		{
			LayoutVisualize v = new LayoutVisualize();
			MyModel model = new MyModel();// = Transformers.loadLayout(
			// "C:/Users/serveradmin/Documents/ELTE/SzoftLabor/input.xml",
			// "Class");
			
			v.load(model.Value.First, model.Value.Second);
			
			v.arrange(model.Value.Third);
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
			
			System.out.println("/Set Assocs/");
			
			Set<LineAssociation> testAssocs = new HashSet<LineAssociation>();
			
			testAssocs.add(new LineAssociation("L1", "A", "B", AssociationType.normal));
			/*
			 * testAssocs.add(new LineAssociation("L2", "A", "C",
			 * AssociationType.normal));
			 * testAssocs.add(new LineAssociation("L3", "A", "D",
			 * AssociationType.normal));
			 * testAssocs.add(new LineAssociation("L4", "A", "B",
			 * AssociationType.normal));
			 */
			
			System.out.println("/Load Data/");
			v.load(testObjects, testAssocs);
			
			System.out.println("/Set Statements/");
			
			ArrayList<Statement> stats = new ArrayList<Statement>();
			
			stats.add(Statement.Parse("phantom(F)"));
			stats.add(Statement.Parse("above(A, F)"));
			stats.add(Statement.Parse("below(C, F)"));
			stats.add(Statement.Parse("right(B, F)"));
			stats.add(Statement.Parse("left(D, F)"));
			
			System.out.println("/Arrange/");
			v.arrange(stats);
			
			for (RectangleObject o : v.getObjects())
				System.out.println(o.toString());
			for (LineAssociation a : v.getAssocs())
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
		System.out.println("--END--");
	}
	
}
