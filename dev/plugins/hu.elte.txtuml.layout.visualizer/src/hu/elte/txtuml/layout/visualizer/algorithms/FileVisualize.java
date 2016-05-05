package hu.elte.txtuml.layout.visualizer.algorithms;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * This class writes the textual visualization of a model.
 *
 */
public class FileVisualize {
	/**
	 * Writes the textual visualization of a model.
	 * @param objects {@link RectangleObject}s of model.
	 * @param links {@link LineAssociation}s of model.
	 * @param outputFilePath file's path to write in.
	 * @return whether the printing succeeded or not.
	 */
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

	private static String[][] createOutputText(Set<RectangleObject> os, Set<LineAssociation> as) {
		Set<Point> stuff = new HashSet<Point>();

		for (RectangleObject o : os)
			stuff.addAll(o.getPoints().stream().collect(Collectors.toSet()));
		for (LineAssociation a : as)
			stuff.addAll(a.getRoute().stream().collect(Collectors.toSet()));

		Point topleft = getTopLeftPoint(stuff);
		Point bottomright = getBottomRightPoint(stuff);

		Integer verticalDimension = Math.abs(topleft.getY() - bottomright.getY()) + 1;
		Integer horizontalDimension = Math.abs(bottomright.getX() - topleft.getX()) + 1;

		String[][] result = new String[verticalDimension][horizontalDimension];

		for (int i = 0; i < result.length; ++i) {
			for (int j = 0; j < result[i].length; ++j) {
				Point temp = new Point(j + topleft.getX(), i + bottomright.getY());
				String symbol = "";

				if (os.stream().anyMatch(
						o -> Point.Add(Point.Add(o.getPosition(), Direction.east), Direction.south).equals(temp))) {
					String name = os.stream().filter(
							o -> Point.Add(Point.Add(o.getPosition(), Direction.east), Direction.south).equals(temp))
							.findFirst().get().getName();
					if (name.length() >= 2)
						symbol = name.substring(0, 2);
					else if (name.length() == 1)
						symbol = name + " ";
				} else if (os.stream().anyMatch(o -> o.getPoints().contains(temp))) {
					if (as.stream().anyMatch(a -> a.getRoute().contains(temp) && !(a.getRoute().indexOf(temp) == 0
							|| a.getRoute().indexOf(temp) == a.getRoute().size() - 1))) {
						symbol = "@ ";
					} else {
						symbol = "# ";
					}
				} else if (as.stream().anyMatch(a -> a.getRoute().contains(temp))) {
					LineAssociation link = as.stream().filter(a -> a.getRoute().contains(temp)).findFirst().get();
					if (isTurningPoint(link, temp)) {
						symbol = "* ";
					} else if (isCrossingPoint(as, temp)) {
						symbol = "+ ";
					} else if (isVerticalPoint(link, temp)) {
						symbol = "| ";
					} else if (isHorizontalPoint(link, temp)) {
						symbol = "- ";
					}
				} else {
					symbol = "  ";
				}

				result[result.length - i - 1][j] = symbol;
			}
		}

		return result;
	}

	private static Point getBottomRightPoint(Set<Point> sp) {
		Point bottomright = null;
		for (Point p : sp) {
			if (bottomright == null) {
				bottomright = new Point(p);
			} else if (p.getX() > bottomright.getX()) {
				bottomright.setX(p.getX());
			} else if (p.getY() < bottomright.getY()) {
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
			} else if (p.getX() < topleft.getX()) {
				topleft.setX(p.getX());
			} else if (p.getY() > topleft.getY()) {
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

	private static boolean isCrossingPoint(Set<LineAssociation> as, Point p) {
		return as.stream().filter(a -> a.getRoute().contains(p)).collect(Collectors.toSet()).size() == 2;
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
