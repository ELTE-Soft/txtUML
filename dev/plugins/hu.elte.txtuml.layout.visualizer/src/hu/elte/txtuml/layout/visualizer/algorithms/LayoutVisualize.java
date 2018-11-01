package hu.elte.txtuml.layout.visualizer.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.algorithms.boxes.ArrangeObjects;
import hu.elte.txtuml.layout.visualizer.algorithms.links.ArrangeAssociations;
import hu.elte.txtuml.layout.visualizer.algorithms.utils.DefaultStatements;
import hu.elte.txtuml.layout.visualizer.algorithms.utils.Helper;
import hu.elte.txtuml.layout.visualizer.algorithms.utils.StatementHelper;
import hu.elte.txtuml.layout.visualizer.events.ProgressEmitter;
import hu.elte.txtuml.layout.visualizer.events.ProgressManager;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxArrangeConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxOverlapConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementsConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider.Dimension;
import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Options;
import hu.elte.txtuml.layout.visualizer.model.OverlapArrangeMode;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.utils.DiagramTreeEnumerator;
import hu.elte.txtuml.layout.visualizer.model.utils.RectangleObjectTreeEnumerator;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;

/**
 * This class is used to wrap the arrange of a whole diagram.
 */
public class LayoutVisualize {
	/**
	 * Diagram to arrange.
	 */
	private Diagram _diagram;

	/**
	 * Statements which arrange. Later the statements on the objects.
	 */
	private List<Statement> _statements;
	/***
	 * Statements on links.
	 */
	private List<Statement> _assocStatements;
	/**
	 * Options.
	 */
	private Options _options;
	private IPixelDimensionProvider _pixelProvider;

	/***
	 * Get the current set of {@link RectangleObject}. Deprecated: Use
	 * ".getDiagram()" instead.
	 * 
	 * @return Set of Objects. Can be null.
	 */
	@Deprecated
	public Set<RectangleObject> getObjects() {
		return _diagram.Objects;
	}

	/***
	 * Get the current set of {@link LineAssociation}. Deprecated: Use
	 * ".getDiagram()" instead.
	 * 
	 * @return Set of Links. Can be null.
	 */
	@Deprecated
	public Set<LineAssociation> getAssocs() {
		return _diagram.Assocs;
	}

	/**
	 * Returns the outer most {@link Diagram}.
	 * 
	 * @return the outer most {@link Diagram}.
	 */
	public Diagram getDiagram() {
		return _diagram;
	}

	/**
	 * Get the finishing set of Statements on objects.
	 * 
	 * @return List of Statements on objects.
	 */
	public List<Statement> getStatements() {
		return _statements;
	}

	/**
	 * Get the finishing set of Statements on links.
	 * 
	 * @return List of Statements on links.
	 */
	public List<Statement> getAssocStatements() {
		return _assocStatements;
	}

	/**
	 * Returns the Horizontal pixel-grid ratio. Deprecated: Use
	 * ".getDiagram().getPixelGridHorizontal()" instead.
	 * 
	 * @return the Horizontal pixel-grid ratio.
	 */
	@Deprecated()
	public Integer getPixelGridHorizontal() {
		return _diagram.getPixelGridHorizontal().intValue();
	}

	/**
	 * Returns the Vertical pixel-grid ratio. Deprecated: Use
	 * ".getDiagram().getPixelGridHorizontal()" instead.
	 * 
	 * @return the Vertical pixel-grid ratio.
	 */
	@Deprecated
	public Integer getPixelGridVertical() {
		return _diagram.getPixelGridVertical().intValue();
	}

	/**
	 * Returns the ProgressEmitter class.
	 * 
	 * @return the ProgressEmitter class.
	 */
	public ProgressEmitter getProgressEmitter() {
		return ProgressManager.getEmitter();
	}

	/**
	 * Adds an observer to the Event Emitter.
	 * 
	 * @param o
	 *            Observer to add.
	 */
	public void addObserver(Observer o) {
		ProgressManager.addObserver(o);
	}

	/**
	 * Setter for logging property.
	 * 
	 * @param value
	 *            Whether to enable or disable this feature.
	 */
	public void setLogging(Boolean value) {
		_options.Logging = value;
	}

	/**
	 * Layout algorithm initialize. Use load(), then arrange().
	 * 
	 * @param type
	 *            Type of the diagram to arrange.
	 * @param pPImpl
	 *            A class that implements {@link IPixelDimensionProvider}
	 *            interface
	 */
	@Deprecated
	public LayoutVisualize(DiagramType type, IPixelDimensionProvider pPImpl) {
		_pixelProvider = pPImpl;
		_options = new Options();
		setDefaults();
	}

	/**
	 * Layout algorithm initialize. Use load(), then arrange().
	 * 
	 * @param pPImpl
	 *            A class that implements {@link IPixelDimensionProvider}
	 *            interface.
	 * 
	 * @param type
	 *            Type of the diagram to arrange.
	 */
	public LayoutVisualize(IPixelDimensionProvider pPImpl) {
		_pixelProvider = pPImpl;
		_options = new Options();
		setDefaults();
	}

	private void setDefaults() {
		ProgressManager.start();

		_diagram = null;
		_options.ArrangeOverlaps = OverlapArrangeMode.few;
		_options.Logging = false;
		_options.CorridorRatio = new HashMap<String, Double>();
		_options.CornerPercentage = 0.15;
		
		_statements = new ArrayList<Statement>();
		_statements.add(new Statement(StatementType.corridorsize, "1.0", ""));
	}

	/**
	 * Arranges the previously loaded model with the given {@link Statement}s.
	 * 
	 * @param par_stats
	 *            List of {@link Statement}s.
	 * @throws UnknownStatementException
	 *             Throws if an unknown {@link Statement} is provided.
	 * @throws ConversionException
	 *             Throws if algorithm cannot convert certain type into other
	 *             type.
	 * @throws BoxArrangeConflictException
	 *             Throws if there are some conflicts during the layout of
	 *             boxes.
	 * @throws StatementTypeMatchException
	 *             Throws if any of the {@link Statement}s are not in correct
	 *             format.
	 * @throws CannotFindAssociationRouteException
	 *             Throws if the algorithm cannot find the route for a
	 *             {@link LineAssociation}.
	 * @throws StatementsConflictException
	 *             Throws if there are some conflicts in the {@link Statement}s
	 *             given by the user.
	 * @throws BoxOverlapConflictException
	 *             Throws if the algorithm encounters an unsolvable overlap
	 *             during the arrangement of the boxes.
	 * @throws InternalException
	 *             Throws if any error occurs which should not happen. Contact
	 *             developer for more details!
	 */
	public void arrange(List<Statement> par_stats)
			throws InternalException, ConversionException, StatementsConflictException, BoxArrangeConflictException,
			BoxOverlapConflictException, CannotFindAssociationRouteException, UnknownStatementException {
		if (_diagram.Objects == null)
			return;

		// Clone statements into local working copy
		_statements.addAll(Helper.cloneStatementList(par_stats));
		_statements.sort((s1, s2) -> {
			return s1.getType().compareTo(s2.getType());
		});

		// Get options from statements
		// Remove them from statements
		getOptions();

		if (_options.Logging)
			Logger.sys.info("Starting arrange...");

		// Set next Group Id
		Integer maxGroupId = getGroupId();

		// Transform special associations into statements
		maxGroupId = transformAssocsIntoStatements(maxGroupId);

		// Split statements for objects and associations
		// _statements, _assocStatements
		splitStatements();

		// Transform Phantom statements into Objects
		// This is handled in layout.export (?)
		getPhantoms();

		// Set Default Statements
		maxGroupId = addDefaultStatements(maxGroupId);

		// Box arrange
		maxGroupId = boxArrange(maxGroupId);

		// Set start-end positions for associations
		updateAssocsEnd();

		// Sets the number of links connected to each box
		updateLinkNumber();

		// Arrange associations between objects
		maxGroupId = linkArrange(maxGroupId);

		// Eliminate the phantom boxes. Lift up possible inner
		// diagram.
		_diagram = eliminatePhantoms(_diagram);

		// TODO
		// Temporarily move inner diagram items back to their relative
		// coordinates
		_diagram = moveInnersBack(null, _diagram);

		if (_options.Logging)
			Logger.sys.info("End of arrange!");

		ProgressManager.end();
		
		//FileVisualize.printOutput(_diagram, "C:\\Users\\Alez\\Documents\\asd", "1");
	}

	private void getOptions() throws InternalException {
		// Remove corridorsize from statements

		List<Statement> optionList = _statements.stream().filter(s -> s.getType().equals(StatementType.corridorsize))
				.collect(Collectors.toList());

		if (optionList.size() > 0) {
			for (Statement stat : optionList) {
				Double value = Double.parseDouble(stat.getParameter(0));
				_options.CorridorRatio.put(stat.getParameter(1), value);

				// Apply this spacing to all lower layers
				RectangleObject parentBox = findBox(stat.getParameter(1));
				String parentName = (parentBox == null)? "" : parentBox.getName();
				if (parentBox != null && !parentBox.hasInner())
					throw new InternalException("Box should have inner diagram: " + parentName + "!");

				Diagram currDiag = (parentBox == null)? _diagram : parentBox.getInner();
				for (RectangleObject box : new RectangleObjectTreeEnumerator(currDiag.Objects)) {
					if (box.hasInner() && !_options.CorridorRatio.containsKey(box.getName())) {
						_options.CorridorRatio.put(box.getName(), value);
					}
				}
			}

			if (_options.Logging)
				Logger.sys.info("Found Corridor size option setting (" + _options.CorridorRatio.toString() + ")!");
		}

		// Remove overlaparrange from statements
		optionList = _statements.stream().filter(s -> s.getType().equals(StatementType.overlaparrange))
				.collect(Collectors.toList());

		if (optionList.size() > 0) {
			_options.ArrangeOverlaps = Enum.valueOf(OverlapArrangeMode.class, optionList.get(0).getParameter(0));

			if (_options.Logging)
				Logger.sys.info(
						"Found Overlap arrange mode option setting (" + _options.ArrangeOverlaps.toString() + ")!");
		}

		_statements.removeIf(s -> s.getType().equals(StatementType.corridorsize)
				|| s.getType().equals(StatementType.overlaparrange));
	}

	private RectangleObject findBox(String name) {
		for (RectangleObject box : new RectangleObjectTreeEnumerator(_diagram.Objects)) {
			if (box.getName().equals(name)) {
				return box;
			}
		}

		return null;
	}

	private Integer getGroupId() {
		Optional<Integer> tempMax = _statements.stream().filter(s -> s.getGroupId() != null).map(s -> s.getGroupId())
				.max((a, b) -> {
					return Integer.compare(a, b);
				});
		return tempMax.isPresent() ? tempMax.get() : 0;
	}

	private void splitStatements() throws ConversionException, StatementsConflictException {
		_assocStatements = StatementHelper.splitAssocs(_statements, _diagram.Assocs);
		_statements.removeAll(_assocStatements);
		_assocStatements = StatementHelper.reduceAssocs(_assocStatements);
	}

	private Integer transformAssocsIntoStatements(Integer maxGroupId) throws InternalException {
		Pair<List<Statement>, Integer> tempPair = StatementHelper.transformAssocs(_diagram, maxGroupId);
		_statements.addAll(tempPair.getFirst());
		return tempPair.getSecond();
	}

	private void getPhantoms() {
		Set<String> phantoms = new HashSet<String>();

		// get all of the phantom box's names
		phantoms.addAll(StatementHelper.extractPhantoms(_statements));

		// set phantom property on boxes
		for (RectangleObject box : new RectangleObjectTreeEnumerator(_diagram.Objects)) {
			if (phantoms.contains(box.getName())) {
				box.setPhantom(true);
			}
		}

		_statements.removeAll(_statements.stream().filter(s -> s.getType().equals(StatementType.phantom))
				.collect(Collectors.toSet()));
	}

	private Integer addDefaultStatements(Integer maxGroupId) throws InternalException {
		DefaultStatements ds = new DefaultStatements(_diagram, _statements, maxGroupId);
		_statements.addAll(ds.value());

		return ds.getGroupId();
	}

	private Integer boxArrange(Integer maxGroupId)
			throws BoxArrangeConflictException, InternalException, ConversionException, BoxOverlapConflictException {
		if (_options.Logging)
			Logger.sys.info("> Starting box arrange...");

		Pair<Integer, Diagram> result = recursiveBoxArrange(maxGroupId, _diagram);
		_diagram = result.getSecond();
		maxGroupId = result.getFirst();

		if (_options.Logging)
			Logger.sys.info("> Box arrange DONE!");

		return maxGroupId;
	}

	private Pair<Integer, Diagram> recursiveBoxArrange(Integer maxGroupId, Diagram diag)
			throws BoxArrangeConflictException, InternalException, ConversionException, BoxOverlapConflictException {
		ArrangeObjects ao = new ArrangeObjects(diag.Objects.stream().collect(Collectors.toList()), _statements,
				maxGroupId, _options);
		diag.Objects = new HashSet<RectangleObject>(ao.value());

		_statements = ao.statements();
		maxGroupId = ao.getGId();

		for (RectangleObject box : diag.Objects) {
			if (box.hasInner()) {
				Pair<Integer, Diagram> result = recursiveBoxArrange(maxGroupId, box.getInner());
				box.setInner(result.getSecond());
				maxGroupId = result.getFirst();
			}
		}

		return Pair.of(maxGroupId, diag);
	}

	private void updateAssocsEnd() throws InternalException {
		for (LineAssociation link : _diagram.Assocs) {
			ArrayList<Point> linkRoute = new ArrayList<Point>();

			Point startend = null;
			Point endend = null;

			for (RectangleObject o : new RectangleObjectTreeEnumerator(_diagram.Objects)) {
				if (link.getFrom().equals(o.getName())) {
					startend = o.getPosition();
				}
				if (link.getTo().equals(o.getName())) {
					endend = o.getPosition();
				}

				if (startend != null && endend != null)
					break;
			}

			if (startend == null || endend == null)
				throw new InternalException("Object at link's end not found: " + link.toString());

			linkRoute.add(startend);
			linkRoute.add(endend);
			link.setRoute(linkRoute);
		}
	}

	private void updateLinkNumber() {
		Map<String, RectangleObject> nameMapper = new HashMap<String, RectangleObject>();

		// Setup mapper
		for (RectangleObject box : new RectangleObjectTreeEnumerator(_diagram.Objects)) {
			nameMapper.put(box.getName(), box);
		}

		// Update linknumbers based on mapper
		for (Diagram diag : new DiagramTreeEnumerator(_diagram)) {
			for (LineAssociation link : diag.Assocs) {
				nameMapper.get(link.getFrom()).addLinkNumber(1);
				nameMapper.get(link.getTo()).addLinkNumber(1);
			}
		}
	}

	private Integer linkArrange(Integer maxGroupId) throws ConversionException, InternalException,
			CannotFindAssociationRouteException, UnknownStatementException {
		if (_options.Logging)
			Logger.sys.info("> Starting link arrange...");

		// Start arrange the links of this diagram inside out.
		Pair<Integer, Diagram> result = recursiveLinkArrange(maxGroupId, _diagram, "");
		_diagram = result.getSecond();
		
		if (_options.Logging)
			Logger.sys.info("> Link arrange DONE!");

		// Return the new maximum group id number.
		return result.getFirst();
	}

	private Pair<Integer, Diagram> recursiveLinkArrange(Integer maxGroupId, Diagram toLayout, String parentName)
			throws ConversionException, InternalException, CannotFindAssociationRouteException,
			UnknownStatementException {
		// Arrange all siblings' children and get their pixel values
		for (RectangleObject box : toLayout.Objects) {
			if (box.hasInner()) {
				if (_options.Logging)
					Logger.sys.info("> > Starting recursive link arrange!");

				Pair<Integer, Diagram> res = recursiveLinkArrange(maxGroupId, box.getInner(), box.getName());
				maxGroupId = res.getFirst();
				box.setInner(res.getSecond());

				if (_options.Logging)
					Logger.sys.info("> > Recursive link arrange DONE!");
			}

			Dimension dim = _pixelProvider.getPixelDimensionsFor(box);
			box.setPixelWidth(dim.Width);
			box.setPixelHeight(dim.Height);
			
			if(box.hasInner())
			{
				Integer topGap = (int)Math.round(dim.TopBorder / box.getInner().getPixelGridVertical());
				Integer leftGap = (int)Math.round(dim.LeftBorder / box.getInner().getPixelGridHorizontal());
				
				box.getInner().setLeftPixelBorder(dim.LeftBorder);
				box.getInner().setTopPixelBorder(dim.TopBorder);
				box.getInner().setBottomPixelBorder(dim.BottomBorder);
				box.getInner().setRightPixelBorder(dim.RightBorder);
				
				box.getInner().setPixelHeader(dim.Header);
				addGap(box, topGap, leftGap);
			}
		}
		// Arrange of siblings
		ArrangeAssociations aa = new ArrangeAssociations(toLayout, _assocStatements, parentName, maxGroupId, _options);
		toLayout = aa.getDiagram();

		return new Pair<Integer, Diagram>(aa.getGId(), toLayout);
	}
	
	private void addGap(RectangleObject rect, Integer topGap, Integer leftGap)
	{
		/*
		rect.setHeight(rect.getHeight()+topGap*2);
		rect.setWidth(rect.getWidth()+leftGap*2);
		*/
		Diagram inner = rect.getInner();
		
		for(RectangleObject box : inner.Objects)
		{
			box.setPosition(new Point(box.getPosition().getX() + leftGap,
					box.getPosition().getY() - topGap));
		}
		
		for(LineAssociation link : inner.Assocs)
		{
			List<Point> newroute = new ArrayList<Point>();
			for(Point p : link.getRoute())
			{
				newroute.add(new Point(p.getX() + leftGap, p.getY() - topGap));
			}
			link.setRoute(newroute);
		}
	}

	private Diagram eliminatePhantoms(final Diagram diag) {
		Diagram result = new Diagram(diag.Type);

		for (RectangleObject box : diag.Objects) {
			if (box.hasInner()) {
				box.setInner(eliminatePhantoms(box.getInner()));
			}

			if (box.isPhantom()) {
				if (box.hasInner()) {
					result.Objects.addAll(box.getInner().Objects);
					result.Assocs.addAll(box.getInner().Assocs);
				}
			} else {
				result.Objects.add(box);
			}
		}

		result.Assocs.addAll(diag.Assocs);

		return result;
	}

	private Diagram moveInnersBack(Point parentPos, Diagram diag) {
		if (parentPos != null) {
			for (RectangleObject box : diag.Objects) {
				box.setPosition(Point.Substract(box.getPosition(), parentPos));
			}

			for (LineAssociation link : diag.Assocs) {
				for (Point p : link.getRoute()) {
					p.setX(p.getX() - parentPos.getX());
					p.setY(p.getY() - parentPos.getY());
				}
			}
		}

		for (RectangleObject box : diag.Objects) {
			if (box.hasInner())
				box.setInner(moveInnersBack(box.getPosition(), box.getInner()));
		}

		return diag;
	}

	/**
	 * Loads a diagram into the arranger.
	 * 
	 * @param diag
	 *            Diagram to load.
	 */
	public void load(Diagram diag) {
		_diagram = new Diagram(diag);
	}

	/***
	 * This function is used to load data to arrange.
	 * 
	 * @param os
	 *            Set of RectangleObjects to arrange on a grid.
	 * @param as
	 *            Set of LineAssociations to arrange between objects.
	 */
	@Deprecated
	public void load(Set<RectangleObject> os, Set<LineAssociation> as) {
		_diagram = new Diagram(DiagramType.Class);
		_diagram.Objects = os;
		_diagram.Assocs = as;
	}

}
