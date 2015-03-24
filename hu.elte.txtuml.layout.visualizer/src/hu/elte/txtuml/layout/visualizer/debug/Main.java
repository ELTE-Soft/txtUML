package hu.elte.txtuml.layout.visualizer.debug;

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
	 */
	public static void main(String[] args)
	{
		long startTime = System.nanoTime();
		System.out.println("Test:");
		
		// Algorithm.BellmanFordSP.test();
		// Algorithm.ArrangeObjects.test();
		// Algorithm.GraphSearch.test();
		// Algorithm.ArrangeAssociations.test();
		// Algorithm.LayoutVisualize.usage();
		hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize.test();
		long endTime = System.nanoTime();
		double Time = (endTime - startTime) * 1000000000;
		System.out.println("Took " + Time + " sec(s).");
	}
}
