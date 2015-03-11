package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotPositionObjectException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.MyException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.helpers.MyModel;
import hu.elte.txtuml.layout.visualizer.helpers.Pair;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashSet;

public class LayoutVisualize
{
	/***
	 * Objects to arrange.
	 */
	private HashSet<RectangleObject> _objects;
	/***
	 * Links to arrange
	 */
	private HashSet<LineAssociation> _assocs;
	
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
	public HashSet<RectangleObject> getObjects()
	{
		return _objects;
	}
	
	/***
	 * Get the current set of Links.
	 * 
	 * @return Set of Links. Can be null.
	 */
	public HashSet<LineAssociation> getAssocs()
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
		stats = Statement.UnfoldGroups(stats);
		
		// Split statements on assocs
		_assocStatements = Statement.SplitAssocs(stats, _assocs);
		_assocStatements = Statement.ReduceAssocs(_assocStatements);
		stats.removeAll(_assocStatements);
		
		// Reduce statements: mosts
		stats.addAll(Statement.TransformAssocs(stats, _assocs));
		_statements = Statement.ReduceObjects(stats, _objects);
		
		// Check Obejct Statement Types
		for (Statement s : _statements)
		{
			if (!Statement.IsTypeChecked(s, _objects, _assocs))
				throw new StatementTypeMatchException("Types not match at statement: "
						+ s.toString() + "!");
		}
		// Check Association Statement Types
		for (Statement s : _assocStatements)
		{
			if (!Statement.IsTypeChecked(s, _objects, _assocs))
				throw new StatementTypeMatchException("Types not match at statement: "
						+ s.toString() + "!");
		}
		
		// Arrange objects
		ArrangeObjects ao = new ArrangeObjects(_objects, _statements);
		_objects = ao.value();
		
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
	public void load(Pair<HashSet<RectangleObject>, HashSet<LineAssociation>> pair)
	{
		_objects = pair.First;
		_assocs = pair.Second;
	}
	
	public static void usage()
	{
		try
		{
			LayoutVisualize v = new LayoutVisualize();
			MyModel model = new MyModel();// = Transformers.loadLayout(
			// "C:/Users/serveradmin/Documents/ELTE/SzoftLabor/input.xml",
			// "Class");
			
			v.load(model.Value.ToFirstPair());
			
			v.arrange(model.Value.Third);
			HashSet<RectangleObject> o = v.getObjects();
			HashSet<LineAssociation> a = v.getAssocs();
			
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
			
			HashSet<RectangleObject> testObjects = new HashSet<RectangleObject>();
			testObjects.add(new RectangleObject("A"));
			testObjects.add(new RectangleObject("B"));
			testObjects.add(new RectangleObject("C"));
			testObjects.add(new RectangleObject("D"));
			
			System.out.println("/Set Assocs/");
			
			HashSet<LineAssociation> testAssocs = new HashSet<LineAssociation>();
			
			testAssocs.add(new LineAssociation("Fugg", "A", "B", AssociationType.normal));
			testAssocs.add(new LineAssociation("Viz", "C", "D", AssociationType.normal));
			
			System.out.println("/Load Data/");
			v.load(new Pair<HashSet<RectangleObject>, HashSet<LineAssociation>>(
					testObjects, testAssocs));
			
			System.out.println("/Set Statements/");
			
			ArrayList<Statement> stats = new ArrayList<Statement>();
			
			stats.add(Statement.Parse("topmost(A)"));
			stats.add(Statement.Parse("bottommost(D)"));
			stats.add(Statement.Parse("leftmost(C)"));
			stats.add(Statement.Parse("rightmost(B)"));
			stats.add(Statement.Parse("south(Fugg, A)"));
			stats.add(Statement.Parse("west(Fugg, B)"));
			
			System.out.println("/Arrange/");
			v.arrange(stats);
			
			System.out.println(v.getObjects());
			System.out.println(v.getAssocs());
			
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
	
	public static void test2()
	{
		System.out.println("--START--");
		
		try
		{
			LayoutVisualize v = new LayoutVisualize();
			
			System.out.println("/Set Objects/");
			
			HashSet<RectangleObject> testObjects = new HashSet<RectangleObject>();
			testObjects.add(new RectangleObject("A"));
			testObjects.add(new RectangleObject("B"));
			testObjects.add(new RectangleObject("C"));
			
			System.out.println("/Set Assocs/");
			
			HashSet<LineAssociation> testAssocs = new HashSet<LineAssociation>();
			
			testAssocs.add(new LineAssociation("L1", "A", "B", AssociationType.normal));
			testAssocs.add(new LineAssociation("L2", "B", "C", AssociationType.normal));
			testAssocs.add(new LineAssociation("L3", "A", "C", AssociationType.normal));
			testAssocs.add(new LineAssociation("L4", "B", "B", AssociationType.normal));
			
			System.out.println("/Load Data/");
			v.load(new Pair<HashSet<RectangleObject>, HashSet<LineAssociation>>(
					testObjects, testAssocs));
			
			System.out.println("/Set Statements/");
			
			ArrayList<Statement> stats = new ArrayList<Statement>();
			
			stats.add(Statement.Parse("topmost(A)"));
			stats.add(Statement.Parse("bottommost(C)"));
			stats.add(Statement.Parse("south(B, A)"));
			
			stats.add(Statement.Parse("south(L1, A)"));
			stats.add(Statement.Parse("north(L1, B)"));
			
			stats.add(Statement.Parse("south(L2, B)"));
			stats.add(Statement.Parse("north(L2, C)"));
			
			stats.add(Statement.Parse("west(L3, A)"));
			stats.add(Statement.Parse("west(L3, C)"));
			
			stats.add(Statement.Parse("east(L4, B)"));
			
			System.out.println("/Arrange/");
			// v.arrange(Transformers.loadStatements("TARGET_FILE_NAME"));
			v.arrange(stats);
			
			System.out.println("--Objects--");
			
			for (RectangleObject o : v.getObjects())
			{
				System.out.println(o.getName() + ": " + o.getPosition().toString());
			}
			System.out.println();
			
			System.out.println("--Assocs--");
			
			for (LineAssociation a : v.getAssocs())
			{
				System.out.println("(" + a.getId() + ") " + a.getFrom() + " - "
						+ a.getTo() + " [T:" + a.getTurns() + "] :");
				boolean first = true;
				for (Point p : a.getRoute())
				{
					if (first)
						first = false;
					else
						System.out.print(" -> ");
					System.out.print(p.toString());
				}
				System.out.println();
			}
			System.out.println();
			
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
	
	public static void test3()
	{
		System.out.println("--START--");
		
		try
		{
			LayoutVisualize v = new LayoutVisualize();
			
			System.out.println("/Set Objects/");
			
			HashSet<RectangleObject> testObjects = new HashSet<RectangleObject>();
			testObjects.add(new RectangleObject("A"));
			testObjects.add(new RectangleObject("B"));
			testObjects.add(new RectangleObject("C"));
			testObjects.add(new RectangleObject("F"));
			
			System.out.println("/Set Assocs/");
			
			HashSet<LineAssociation> testAssocs = new HashSet<LineAssociation>();
			
			// testAssocs.add(new LineAssociation("1", "E", "B",
			// AssociationType.normal));
			// testAssocs.add(new LineAssociation("2", "C", "D",
			// AssociationType.normal));
			// testAssocs.add(new LineAssociation("3", "E", "C",
			// AssociationType.normal));
			
			System.out.println("/Load Data/");
			v.load(new Pair<HashSet<RectangleObject>, HashSet<LineAssociation>>(
					testObjects, testAssocs));
			
			System.out.println("/Set Statements/");
			ArrayList<Statement> stats = new ArrayList<Statement>();
			
			// Objects
			stats.add(Statement.Parse("group(A, G)"));
			stats.add(Statement.Parse("group(C, G3)"));
			stats.add(Statement.Parse("group(B, G2)"));
			stats.add(Statement.Parse("group(G3, G2)"));
			stats.add(Statement.Parse("group(G2, G)"));
			stats.add(Statement.Parse("south(F, G)"));
			
			System.out.println("/Arrange/");
			v.arrange(stats);
			
			System.out.println("--Objects--");
			for (RectangleObject o : v.getObjects())
				System.out.println(o);
			System.out.println();
			
			System.out.println("--Assocs--");
			for (LineAssociation a : v.getAssocs())
				System.out.println(a);
			System.out.println();
			
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
