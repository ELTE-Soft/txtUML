package hu.elte.txtuml.layout.visualizer.algorithms.links.utils;

import java.util.ArrayList;
import java.util.HashMap;

import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.utils.RectangleObjectTreeEnumerator;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;
import hu.elte.txtuml.utils.Pair;

/**
 * Diagram to help the arrangement of links in the Diagram
 *
 */
public class LinkArrangeDiagram extends Diagram {

	private final Integer MINIMAL_CORRIDOR_SIZE = 1;

	// Variables

	/**
	 * Width of each {@link RectangleObject}'s cell.
	 */
	public Integer WidthOfCells;
	/**
	 * Height of each {@link RectangleObject}'s cell.
	 */
	public Integer HeightOfCells;
	/**
	 * Position of each {@link RectangleObject}'s cell.
	 */
	public HashMap<String, Point> CellPositions;

	// end Variables

	// Ctors

	/**
	 * Creates a {@link LinkArrangeDiagram} from a {@link Diagram}.
	 * @param diag {@link Diagram} to clone.
	 */
	public LinkArrangeDiagram(final Diagram diag) {
		super(diag);
		WidthOfCells = 1;
		HeightOfCells = 1;
		CellPositions = new HashMap<String, Point>();
	}

	/**
	 * Extract the base {@link Diagram} from this {@link LinkArrangeDiagram}.
	 * @return the base {@link Diagram}.
	 */
	public Diagram getDiagram()
	{
		return new Diagram(Type, Objects, Assocs);
	}
	
	// end Ctors

	// Publics

	/**
	 * Expand of the grid in the first step. Gives boxes a width/height, 
	 * corrects inner diagrams' positions, set boxes's cells, 
	 * set boxes' position in cells, creates corridors between cells
	 * @param corridorRatio Ratio to adjust corridors with.
	 */
	public void initialExpand(Double corridorRatio) {
		// min of all inner box ratio
		Pair<Double, Double> globalRatio = getMinimalRatio();

		// Set Grid width/height
		setupBoxDimensions(globalRatio);

		// Set box cells
		setupCellPositions(corridorRatio);

		// Update the links' positions
		updateLinks();
	}

	/**
	 * Expand of the grid during arrangement.
	 * Creates additional grid lines between every line. 
	 * (Grid multiplication by 2)
	 * @throws ConversionException Throws if a {@link Direction} is not 
	 * convertible to {@link StatementType} or vice versa.
	 */
	public void expand() throws ConversionException
	{
		expand(2);
	}
	
	private void expand(Integer multiplier) throws ConversionException {
		expandBoxes(multiplier);

		expandLinks();
	}

	// Privates

	private void expandBoxes(Integer multiplier) throws ConversionException
	{
		WidthOfCells = WidthOfCells * multiplier;
		HeightOfCells = HeightOfCells * multiplier;
		
		for (RectangleObject box : Objects) {
			/*if (box.hasInner()) {
				LinkArrangeDiagram innerDiagram = new LinkArrangeDiagram(box.getInner());
				innerDiagram.expand(multiplier);
				box.setInner(innerDiagram.getDiagram());
				
				//System.err.print("Inner: " + box.getInner().getWidth() + " x " + box.getInner().getHeight() + " <> ");
			}*/
			
			box.setWidth(box.getWidth() * multiplier);
			box.setHeight(box.getHeight() * multiplier);
			
			// Calculate the position of the box in the cell
			Point tempPos = Point.Multiply(CellPositions.get(box.getName()), multiplier);
			CellPositions.put(box.getName(), new Point(tempPos));

			// Calculate the position of the box in the cell
			box.setPosition(getPositionInCell(tempPos, box));
		}
	}
	
	private void expandLinks() throws ConversionException
	{
		for (LineAssociation link : Assocs) {
			RectangleObject fromBox = Objects.stream().filter(box -> box.getName().equals(link.getFrom())).findFirst()
					.get();
			RectangleObject toBox = Objects.stream().filter(box -> box.getName().equals(link.getTo())).findFirst()
					.get();

			ArrayList<Point> route = new ArrayList<Point>();
			for (int j = 0; j < link.getRoute().size(); ++j) {
				Point p = new Point(link.getRoute().get(j));

				Point temp = Point.Multiply(p, 2);

				if (link.isPlaced() && j > 1 && j < link.getRoute().size() - 1) {
					Direction beforeDirection = Point.Substract(link.getRoute().get(j - 1), link.getRoute().get(j))
							.asDirection();
					Point before = Point.Add(temp, beforeDirection);

					if (fromBox.getPerimiterPoints().contains(before) || toBox.getPerimiterPoints().contains(before)
							|| (!fromBox.getPoints().contains(before) && !toBox.getPoints().contains(before)))
						route.add(before);
				}

				if (fromBox.getPerimiterPoints().contains(temp) || toBox.getPerimiterPoints().contains(temp)
						|| (!fromBox.getPoints().contains(temp) && !toBox.getPoints().contains(temp)))
					route.add(temp);
			}

			link.setRoute(route);
		}
	}
	
	private Pair<Double, Double> getMinimalRatio() {
		Double minWidth = 1.0;
		Double minHeight = 1.0;

		for (RectangleObject box : Objects) {
			if (box.isPixelDimensionsPresent()) {
				Integer linkNumber = box.getLinkNumber();

				Double minGridLines = (double) (linkNumber + 2);

				Double widthRatio = (double) box.getPixelWidth() / minGridLines;
				Double heightRatio = (double) box.getPixelHeight() / minGridLines;

				if (minWidth > widthRatio)
					minWidth = widthRatio;
				if (minHeight > heightRatio)
					minHeight = heightRatio;
			}
		}

		return Pair.of(minWidth, minHeight);
	}

	private void setupBoxDimensions(Pair<Double, Double> globalRatio) {
		for (RectangleObject box : Objects) {
			Integer linkNumber = box.getLinkNumber();

			if (!box.hasInner()) {
				Integer width = Math.max(linkNumber + 2,
						1 + (int) Math.ceil(box.getPixelWidth() / globalRatio.getFirst()));
				box.setWidth(width);

				Integer height = Math.max(linkNumber + 2,
						1 + (int) Math.ceil(box.getPixelHeight() / globalRatio.getSecond()));
				box.setHeight(height);
			} else {
				box.setWidth(box.getInner().getWidth());
				box.setHeight(box.getInner().getHeight());
			}

			if (WidthOfCells < box.getWidth())
				WidthOfCells = box.getWidth();
			if (HeightOfCells < box.getHeight())
				HeightOfCells = box.getHeight();
		}
	}

	private void setupCellPositions(Double corridorRatio) {
		for (RectangleObject box : Objects) {
			Point tempPos = new Point();

			// Calculate the position of the cell
			tempPos.setX(box.getPosition().getX() * calculateCorridorSize(WidthOfCells, corridorRatio));
			tempPos.setY(box.getPosition().getY() * calculateCorridorSize(HeightOfCells, corridorRatio));
			CellPositions.put(box.getName(), new Point(tempPos));

			// Calculate the position of the box in the cell
			box.setPosition(getPositionInCell(tempPos, box));
		}
	}

	private Integer calculateCorridorSize(Integer cellSize, Double multiplier) {
		Integer result = (int) Math.floor(cellSize * (multiplier + 1.0));

		if (result < MINIMAL_CORRIDOR_SIZE)
			result = MINIMAL_CORRIDOR_SIZE;

		return result;
	}

	private Point getPositionInCell(Point old_value, RectangleObject box) {
		Point result = new Point(old_value);

		Integer freeGridCountWidth = WidthOfCells - box.getWidth();
		Integer freeGridCountHeight = HeightOfCells - box.getHeight();

		result.setX(result.getX() + freeGridCountWidth / 2);
		result.setY(result.getY() - freeGridCountHeight / 2);

		return result;
	}

	private void updateLinks() {
		for (LineAssociation link : Assocs) {
			ArrayList<Point> route = new ArrayList<Point>();
			
			route.add(getBox(link.getFrom()).getPosition());
			route.add(getBox(link.getTo()).getPosition());
			
			link.setRoute(route);
		}
	}
	
	private RectangleObject getBox(String name)
	{
		for(RectangleObject box : new RectangleObjectTreeEnumerator(Objects))
		{
			if(box.getName().equals(name))
				return box;
		}
		
		return null;
	}

}
