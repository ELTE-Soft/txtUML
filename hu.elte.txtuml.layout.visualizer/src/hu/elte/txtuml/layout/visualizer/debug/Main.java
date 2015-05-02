package hu.elte.txtuml.layout.visualizer.debug;

import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotPositionObjectException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;

/**
 * Main class for Debug reasons only.
 * 
 * @author Balázs Gregorics
 */
public class Main
{
	/**
	 * Main function for Debug reasons only.
	 * 
	 * @param args
	 *            Arguments, not used.
	 * @throws StatementTypeMatchException
	 * @throws CannotFindAssociationRouteException
	 * @throws CannotPositionObjectException
	 * @throws InternalException
	 * @throws ConversionException
	 * @throws ConflictException
	 * @throws UnknownStatementException
	 */
	public static void main(String[] args) throws UnknownStatementException,
			ConflictException, ConversionException, InternalException,
			CannotPositionObjectException, CannotFindAssociationRouteException,
			StatementTypeMatchException
	{
		long startTime = System.nanoTime();
		System.out.println("Test:");
		
		// Algorithm.BellmanFordSP.test();
		// Algorithm.ArrangeObjects.test();
		// Algorithm.GraphSearch.test();
		// Algorithm.ArrangeAssociations.test();
		// Algorithm.LayoutVisualize.usage();
		// hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize.test();
		// hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize.test2();
		hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize.testReflexive();
		long endTime = System.nanoTime();
		System.out.println("Took " + ((endTime - startTime) / 1000000000.0) + " sec(s).");
	}
}
