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
		System.out.println("Test:");
		
		// Algorithm.BellmanFordSP.test();
		// Algorithm.ArrangeObjects.test();
		// Algorithm.GraphSearch.test();
		// Algorithm.ArrangeAssociations.test();
		// Algorithm.LayoutVisualize.usage();
		hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize.test();
	}
	
}
