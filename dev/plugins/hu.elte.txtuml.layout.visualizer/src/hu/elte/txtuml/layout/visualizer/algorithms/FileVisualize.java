package hu.elte.txtuml.layout.visualizer.algorithms;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.utils.DiagramTreeEnumerator;
import hu.elte.txtuml.layout.visualizer.model.utils.RectangleObjectTreeEnumerator;

/**
 * This class writes the textual visualization of a model.
 *
 */
public class FileVisualize {
	/**
	 * Writes the textual visualization of a model.
	 * 
	 * @param objects
	 *            {@link RectangleObject}s of model.
	 * @param links
	 *            {@link LineAssociation}s of model.
	 * @param outputFilePath
	 *            file's path to write in.
	 * @return whether the printing succeeded or not.
	 */
	@Deprecated
	public static boolean printOutput(Set<RectangleObject> objects, Set<LineAssociation> links, String outputFilePath) {
		try {
			PrintWriter writer = new PrintWriter(outputFilePath, "UTF-8");
			String[][] outputText = createOutputText(objects, links);
			for (String[] ss : outputText) {
				for (String s : ss)
					writer.print(s);
				writer.println();
			}

			writer.close();

		} catch (UnsupportedEncodingException | FileNotFoundException ex) {
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Use this to print a diagram to an output file.
	 * @param diagram {@link Diagram} to print.
	 * @param outputFilePath File to print.
	 * @return whether the print was successful or not.
	 */
	public static boolean printOutput(Diagram diagram, String outputFilePath) {
		try {
			Set<RectangleObject> objects = new HashSet<RectangleObject>();
			Set<LineAssociation> links = new HashSet<LineAssociation>();

			objects = getObjects(diagram);
			links = getLinks(diagram);

			PrintWriter writer = new PrintWriter(outputFilePath, "UTF-8");
			
			writer.println(diagram.getWidth() + " x " + diagram.getHeight());
			writer.println(diagram.getPixelGridHorizontal() + ", " + diagram.getPixelGridVertical());
			writer.println("Boxes:");
			for(RectangleObject box : new RectangleObjectTreeEnumerator(diagram.Objects))
			{
				writer.println(box.toString());
			}
			writer.println("Links:");
			for(Diagram diag : new DiagramTreeEnumerator(diagram))
			{
				for(LineAssociation link : diag.Assocs)
					writer.println(link.toString());
			}
			writer.println("-----");
			
			String[][] outputText = createOutputText(objects, links);
			for (String[] ss : outputText) {
				for (String s : ss)
					writer.print(s);
				writer.println();
			}

			writer.close();

		} catch (/*UnsupportedEncodingException | FileNotFoundException ex |*/
				Exception ex) {
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}

	private static Set<RectangleObject> getObjects(Diagram diagram) {
		Set<RectangleObject> objects = new HashSet<RectangleObject>();

		for (RectangleObject box : diagram.Objects) {
			if (box.hasInner()) {
				objects.addAll(getObjects(box.getInner()));
			}

			if (!box.isPhantom())
				objects.add(box);
		}

		return objects;
	}

	private static Set<LineAssociation> getLinks(Diagram diagram) {
		Set<LineAssociation> links = new HashSet<LineAssociation>();

		for (LineAssociation link : diagram.Assocs) {
			links.add(link);
		}

		for (RectangleObject box : diagram.Objects) {
			if (box.hasInner()) {
				links.addAll(getLinks(box.getInner()));
			}
		}

		return links;
	}

	private static String[][] createOutputText(Set<RectangleObject> objects, Set<LineAssociation> links) {
		Set<Point> stuff = new HashSet<Point>();

		for (RectangleObject o : objects)
		{
			stuff.add(o.getTopLeft());
			stuff.add(o.getBottomRight());
		}
		for (LineAssociation a : links)
		{
			stuff.addAll(a.getRoute().stream().collect(Collectors.toSet()));
		}

		Point topleft = getTopLeftPoint(stuff);
		Point bottomright = getBottomRightPoint(stuff);

		Integer verticalDimension = Math.abs(topleft.getY() - bottomright.getY()) + 1;
		Integer horizontalDimension = Math.abs(bottomright.getX() - topleft.getX()) + 1;

		Integer verticalShift = Math.abs(bottomright.getY());
		Integer horizontalShift = -1*Math.abs(topleft.getX());
		
		String[][] result = new String[verticalDimension][horizontalDimension];
		for(int i = 0; i < horizontalDimension; ++i)
			for(int j = 0; j < verticalDimension; ++j)
				result[j][i] = "  ";
		
		for(LineAssociation link : links)
		{
			for(Point linkPoint : link.getRoute())
			{
				//Skip Start/End Point
				if(link.getRoute().get(0).equals(linkPoint))
					continue;
				if(link.getRoute().get(link.getRoute().size() - 1).equals(linkPoint))
					continue;
				
				if(link.getRoute().get(1) == linkPoint || 
						link.getRoute().get(link.getRoute().size() - 2).equals(linkPoint))
				{
					// Start/End
					result[verticalShift + linkPoint.getY()][horizontalShift + linkPoint.getX()] = "@ ";
				} else if(result[verticalShift + linkPoint.getY()][horizontalShift + linkPoint.getX()] != "  ") {
					result[verticalShift + linkPoint.getY()][horizontalShift + linkPoint.getX()] = "+ ";
				} else if (isTurningPoint(link, linkPoint)) {
					result[verticalShift + linkPoint.getY()][horizontalShift + linkPoint.getX()] = "* ";
				} else if (isVerticalPoint(link, linkPoint)) {
					result[verticalShift + linkPoint.getY()][horizontalShift + linkPoint.getX()] = "| ";
				} else if (isHorizontalPoint(link, linkPoint)) {
					result[verticalShift + linkPoint.getY()][horizontalShift + linkPoint.getX()] = "- ";
				}
			}
		}
		
		for(RectangleObject box : objects)
		{
			Point nameLocation = Point.Add(Point.Add(box.getPosition(), Direction.east), Direction.south);
			String name = box.getName().length() >= 2 ? box.getName().substring(0, 2) : "# ";
			result[verticalShift + nameLocation.getY()][horizontalShift + nameLocation.getX()] = name;
			
			if(box.hasInner())
			{
				for(Point peri : box.getPerimiterPoints())
				{
					if(result[verticalShift + peri.getY()][horizontalShift + peri.getX()] == "  ")
					{
						result[verticalShift + peri.getY()][horizontalShift + peri.getX()] = "# ";
					}
				}
			}
			else
			{
				for(Point poi : box.getPoints())
				{
					if(result[verticalShift + poi.getY()][horizontalShift + poi.getX()] == "  ")
					{
						result[verticalShift + poi.getY()][horizontalShift + poi.getX()] = "# ";
					}
				}
			}
		}
		
		//Flip diagram
		for(int i = 0; i < (verticalDimension / 2); ++i)
		{
			String[] row = result[result.length - 1 - i];
			result[result.length - 1 - i] = result[i];
			result[i] = row;
		}

		return result;
	}

	private static Point getBottomRightPoint(Set<Point> sp) {
		Point bottomright = null;
		for (Point p : sp) {
			if (bottomright == null) {
				bottomright = new Point(p);
			}
			if (p.getX() > bottomright.getX()) {
				bottomright.setX(p.getX());
			}
			if (p.getY() < bottomright.getY()) {
				bottomright.setY(p.getY());
			}
		}

		return bottomright;
	}

	private static Point getTopLeftPoint(Set<Point> sp) {
		Point topleft = null;
		for (Point p : sp) {
			if (topleft == null) {
				topleft = new Point(p);
			}
			if (p.getX() < topleft.getX()) {
				topleft.setX(p.getX());
			}
			if (p.getY() > topleft.getY()) {
				topleft.setY(p.getY());
			}
		}

		return topleft;
	}

	private static boolean isTurningPoint(LineAssociation a, Point p) {
		Integer j = a.getRoute().indexOf(p);

		if (j > 1 && j < a.getRoute().size() - 1) {
			if (!Point.Substract(a.getRoute().get(j - 1), a.getRoute().get(j))
					.equals(Point.Substract(a.getRoute().get(j), a.getRoute().get(j + 1)))
					&& !Point.Substract(a.getRoute().get(j - 1), a.getRoute().get(j)).equals(
							Point.Multiply(Point.Substract(a.getRoute().get(j), a.getRoute().get(j + 1)), -1))) {
				return true;
			}
		}

		return false;
	}

	private static boolean isVerticalPoint(LineAssociation a, Point p) {
		Integer j = a.getRoute().indexOf(p);

		if (j > 1 && j < a.getRoute().size() - 1) {
			if ((Point.Substract(a.getRoute().get(j - 1), a.getRoute().get(j)).equals(new Point(0, 1))
					&& Point.Substract(a.getRoute().get(j), a.getRoute().get(j + 1)).equals(new Point(0, 1)))
					|| (Point.Substract(a.getRoute().get(j - 1), a.getRoute().get(j)).equals(new Point(0, -1)) && Point
							.Substract(a.getRoute().get(j), a.getRoute().get(j + 1)).equals(new Point(0, -1)))) {
				return true;
			}
		}

		return false;
	}

	private static boolean isHorizontalPoint(LineAssociation a, Point p) {
		Integer j = a.getRoute().indexOf(p);

		if (j > 1 && j < a.getRoute().size() - 1) {
			if ((Point.Substract(a.getRoute().get(j - 1), a.getRoute().get(j)).equals(new Point(1, 0))
					&& Point.Substract(a.getRoute().get(j), a.getRoute().get(j + 1)).equals(new Point(1, 0)))
					|| (Point.Substract(a.getRoute().get(j - 1), a.getRoute().get(j)).equals(new Point(-1, 0)) && Point
							.Substract(a.getRoute().get(j), a.getRoute().get(j + 1)).equals(new Point(-1, 0)))) {
				return true;
			}
		}

		return false;
	}

}
