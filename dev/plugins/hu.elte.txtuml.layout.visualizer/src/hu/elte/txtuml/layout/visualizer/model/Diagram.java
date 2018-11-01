package hu.elte.txtuml.layout.visualizer.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;

/**
 * Class to represent a Diagram.
 */
public class Diagram {

	// Variables

	/**
	 * Type of the {@link Diagram}.
	 */
	public DiagramType Type;
	/**
	 * {@link RectangleObject}s in the {@link Diagram}
	 */
	public Set<RectangleObject> Objects;
	/**
	 * {@link LineAssociation}s in the {@link Diagram}.
	 */
	public Set<LineAssociation> Assocs;

	private int pxLeftBorder;
	
	private int pxRightBorder;
	
	private int pxTopBorder;
	
	private int pxBottomBorder;
	
	private int pxHeader;
	
	private int _previousHash;
	
	private double _pixelGridRatioHorizontal;
	
	private double _pixelGridRatioVertical;
	
	private Integer _Width;
	
	private Integer _Height;
	// end Variables

	// Ctors

	/**
	 * Create {@link Diagram}.
	 * @param ty Type of the {@link Diagram}.
	 */
	public Diagram(DiagramType ty) {
		Type = ty;
		Objects = new HashSet<RectangleObject>();
		Assocs = new HashSet<LineAssociation>();
	}

	/**
	 * Copy {@link Diagram}.
	 * @param diag Other {@link Diagram} to copy.
	 */
	public Diagram(final Diagram diag) {
		Type = diag.Type;
		Objects = new HashSet<RectangleObject>(diag.Objects);
		Assocs = new HashSet<LineAssociation>(diag.Assocs);
	}

	/**
	 * Create {@link Diagram}.
	 * @param ty Type of the {@link Diagram}.
	 * @param os {@link RectangleObject}s in the {@link Diagram}.
	 * @param as {@link LineAssociation}s in the {@link Diagram}.
	 */
	public Diagram(DiagramType ty, Set<RectangleObject> os, Set<LineAssociation> as) {
		Type = ty;

		if (os == null)
			Objects = new HashSet<RectangleObject>();
		else
			Objects = new HashSet<RectangleObject>(os);

		if (as == null)
			Assocs = new HashSet<LineAssociation>();
		else
			Assocs = new HashSet<LineAssociation>(as);
	}

	// end Ctors

	// Statics

	/**
	 * Method to clone a {@link Diagram}.
	 * 
	 * @param d
	 *            {@link Diagram} to clone.
	 * @return cloned {@link Diagram}.
	 */
	public static Diagram clone(Diagram d) {
		if (null == d)
			return null;
		else
			return new Diagram(d);
	}

	// end Statics

	// Publics

	/**
	 * Returns the Horizontal pixel-grid ratio.
	 * 
	 * @return the Horizontal pixel-grid ratio.
	 */
	public Double getPixelGridHorizontal() {
		if(hashCode() != _previousHash){
			updateTemps();
		}
		return _pixelGridRatioHorizontal;
	}

	/**
	 * Returns the Vertical pixel-grid ratio.
	 * 
	 * @return the Vertical pixel-grid ratio.
	 */
	public Double getPixelGridVertical() {
		if(hashCode() != _previousHash){
			updateTemps();
		}
		return _pixelGridRatioVertical;
	}

	/**
	 * Returns whether this {@link Diagram} has a layout applied or not.
	 * 
	 * @return whether this {@link Diagram} has a layout applied or not.
	 */
	public boolean hasValidLayout() {
		Boolean result = true;

		for (RectangleObject box1 : Objects) {
			if (!result)
				return result;

			for (RectangleObject box2 : Objects) {
				if (box1.equals(box2))
					continue;

				if (!result)
					return result;

				result = result && !box1.getPosition().equals(box2.getPosition());
			}
		}

		for (LineAssociation link : Assocs) {
			if (!result)
				return result;

			result = result && link.getRoute().size() > 2;
		}

		return result;
	}

	/**
	 * Returns the grid width of this {@link Diagram}. This method is
	 * calculation heavy.
	 * 
	 * @return the grid width of this {@link Diagram}.
	 * @throws InternalException 
	 */
	public Integer getWidth() {
		if(hashCode() != _previousHash){
			updateTemps();
		}
		return _Width;
	}

	/**
	 * Returns the grid height of this {@link Diagram}. This method is
	 * calculation heavy.
	 * 
	 * @return the grid height of this {@link Diagram}.
	 * @throws InternalException 
	 */
	public Integer getHeight() {
		if(hashCode() != _previousHash){
			updateTemps();
		}
		return _Height;
	}

	/**
	 * Returns the grid area boundaries that this diagram fits into (including
	 * extra barrier grid lines).
	 * 
	 * @return the grid area boundaries that this diagram fits into.
	 * @throws InternalException
	 */
	public Boundary getArea(){
		Boundary dim = getDimensions();

		return new Boundary(dim.get_top() + 1, dim.get_bottom() - 1, dim.get_left() - 1, dim.get_right() + 1);
	}

	@Override
	public String toString() {
		String result = "(O:";

		for (RectangleObject obj : Objects) {
			result += obj.getName() + ", ";
		}

		result += "| A: ";

		for (LineAssociation link : Assocs) {
			result += link.getId() + ", ";
		}

		result += ")";

		return result;
	}	

	/**
	 * @return left border thickness of the diagram in pixels
	 */
	public int getLeftPixelBorder() {
		return pxLeftBorder;
	}

	/**
	 * @param pxBorder sets the left border thickness of the diagram in pixels
	 */
	public void setLeftPixelBorder(int pxBorder) {
		this.pxLeftBorder = pxBorder;
	}

	/**
	 * @return right border thickness of the diagram in pixels
	 */
	public int getRightPixelBorder() {
		return pxRightBorder;
	}

	/**
	 * @param pxBorder sets the right border thickness of the diagram in pixels
	 */
	public void setRightPixelBorder(int pxBorder) {
		this.pxRightBorder = pxBorder;
	}
	

	/**
	 * @return top border thickness of the diagram in pixels
	 */
	public int getTopPixelBorder() {
		return pxTopBorder;
	}

	/**
	 * @param pxBorder sets the top border thickness of the diagram in pixels
	 */
	public void setTopPixelBorder(int pxBorder) {
		this.pxTopBorder = pxBorder;
	}
	

	/**
	 * @return bottom border thickness of the diagram in pixels
	 */
	public int getBottomPixelBorder() {
		return pxBottomBorder;
	}

	/**
	 * @param pxBorder sets the bottom border thickness of the diagram in pixels
	 */
	public void setBottomPixelBorder(int pxBorder) {
		this.pxBottomBorder = pxBorder;
	}
	/**
	 * @return the header thickness of the diagram
	 */
	public int getPixelHeader() {
		return pxHeader;
	}

	/**
	 * @param pxHeader sets the header thickness of the diagram in pixels
	 */
	public void setPixelHeader(int pxHeader) {
		this.pxHeader = pxHeader;
	}
	
	// end Publics

	// Privates

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Assocs == null) ? 0 : Assocs.hashCode());
		result = prime * result + ((Objects == null) ? 0 : Objects.hashCode());
		result = prime * result + ((Type == null) ? 0 : Type.hashCode());
		result = prime * result + pxBottomBorder;
		result = prime * result + pxHeader;
		result = prime * result + pxLeftBorder;
		result = prime * result + pxRightBorder;
		result = prime * result + pxTopBorder;
		return result;
	}

	private double getPixelGridRatio(Function<RectangleObject, Integer> gridSelector,
			Function<RectangleObject, Integer> pixelSelector) {
		Integer gridSum = 0;
		Integer pixelSum = 0;

		for (RectangleObject box : Objects) {
			if (box.isPixelDimensionsPresent()) {
				gridSum += (gridSelector.apply(box) - 1);
				pixelSum += pixelSelector.apply(box);
			}
		}

		double result = (double)pixelSum / (double)gridSum;
		
		if(result <= 1)
			return 1;
		
		return result;
	}

	private Boundary getDimensions() {
		Integer left = Integer.MAX_VALUE;
		Integer right = Integer.MIN_VALUE;
		Integer top = Integer.MIN_VALUE;
		Integer bottom = Integer.MAX_VALUE;

		for (RectangleObject box : Objects) {
			if (box.getPosition().getX() < left)
				left = box.getPosition().getX();
			if ((box.getPosition().getX() + (box.getWidth() - 1)) > right)
				right = box.getPosition().getX() + (box.getWidth() - 1);
			if (box.getPosition().getY() > top)
				top = box.getPosition().getY();
			if ((box.getPosition().getY() - (box.getHeight() - 1)) < bottom)
				bottom = box.getPosition().getY() - (box.getHeight() - 1);
		}

		for (LineAssociation link : Assocs) {
			for (Point poi : link.getRoute()) {
				if (poi.getX() < left)
					left = poi.getX();
				if (poi.getX() > right)
					right = poi.getX();
				if (poi.getY() > top)
					top = poi.getY();
				if (poi.getY() < bottom)
					bottom = poi.getY();
			}
		}

		return new Boundary(top, bottom, left, right);
	}
	

	private void updateTemps() {
		_pixelGridRatioHorizontal = getPixelGridRatio(box -> box.getWidth(), box -> box.getPixelWidth());
		_pixelGridRatioVertical = getPixelGridRatio(box -> box.getHeight(), box -> box.getPixelHeight());
		_Width = Math.abs((getDimensions().get_right() - getDimensions().get_left()) + 1);
		_Height = Math.abs((getDimensions().get_top() - getDimensions().get_bottom()) + 1);
		_previousHash = hashCode();
	}
	// end Privates
}
