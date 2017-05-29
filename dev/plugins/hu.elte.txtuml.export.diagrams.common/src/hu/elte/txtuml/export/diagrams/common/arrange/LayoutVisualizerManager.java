package hu.elte.txtuml.export.diagrams.common.arrange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxArrangeConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxOverlapConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementsConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.utils.Logger;

/**
 * The instance of this class handles the layout algorithm
 */
public class LayoutVisualizerManager {
	private Diagram diagram;
	private List<Statement> statementsSet;
	private IProgressMonitor progressMonitor = new NullProgressMonitor();
	private final LayoutVisualize layoutVisualize;
	private boolean errorFlag = false;
	private String errorDetails;
	private String errorTitle;
	private String errorMessage;

	/**
	 * The Constructor
	 * 
	 * @param objects
	 *            - Rectangle Objects to be arranged by the algorithm
	 * @param links
	 *            - Links to be arranged by the algorithm
	 * @param statements
	 *            - statements that are to be considered by the arrangement
	 * @param txtUmlRegistry
	 *            - The TxtUMLElementsFinder that connects the model elements
	 *            with txtUML names
	 */
	public LayoutVisualizerManager(Set<RectangleObject> objects, Set<LineAssociation> links, List<Statement> statements,
			DiagramType type, IPixelDimensionProvider pixelProvider) {
		this.diagram = new Diagram(type, objects, links);
		this.statementsSet = statements;
		layoutVisualize = new LayoutVisualize(pixelProvider);
		layoutVisualize.setLogging(false);
	}

	/**
	 * @param monitor
	 */
	public void addProgressMonitor(IProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}

	/**
	 * Arranging command
	 * 
	 * @throws ArrangeException
	 */
	public void arrange() throws ArrangeException {
		Thread t = new Thread(() -> {
			layoutVisualize.load(diagram);
			try {
				progressMonitor.beginTask("Arranging elements", 100);
				progressMonitor.subTask("Arranging elements...");
				layoutVisualize.arrange(new ArrayList<Statement>(statementsSet));
			} catch (InternalException | BoxArrangeConflictException | ConversionException
					| CannotFindAssociationRouteException | UnknownStatementException | BoxOverlapConflictException
					| StatementsConflictException e) {

				if (e instanceof BoxArrangeConflictException) {
					List<Statement> statements = ((BoxArrangeConflictException) e).ConflictStatements;
					errorTitle = "Conflicting statements";
					errorDetails = formatStatements(statements != null ? statements : new ArrayList<Statement>());
				} else if (e instanceof BoxOverlapConflictException) {
					List<String> boxes = ((BoxOverlapConflictException) e).OverlappingBoxes;
					errorTitle = "Overlapping boxes";
					errorDetails = formatDetails(boxes != null ? boxes : new ArrayList<String>());
				} else if (e instanceof StatementsConflictException) {
					List<Statement> statements = ((StatementsConflictException) e).ConflictStatements;
					errorTitle = "Conflicting statements";
					errorDetails = formatStatements(statements != null ? statements : new ArrayList<Statement>());
				} else if (e instanceof UnknownStatementException) {
					String statement = ((UnknownStatementException) e).Statement;
					errorTitle = "Unknown statement";
					errorDetails = statement;
				}
				errorFlag = true;
				errorMessage = e.getMessage();
			}
		});

		try {
			layoutVisualize.addObserver(new Observer() {

				int previous_percent = 0;

				@SuppressWarnings("deprecation")
				@Override
				public void update(Observable o, Object arg) {
					if (progressMonitor.isCanceled()) {
						t.interrupt();
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO: refactor the arranging algorithm to be
							// prepared for thread interruption, then this
							// try-catch can be deleted
							t.stop();
						}
					}

					Integer percent = (Integer) arg;
					progressMonitor.worked(percent - this.previous_percent);
					progressMonitor.subTask("Arranging elements... " + percent + "%");
					this.previous_percent = this.previous_percent + percent;
				}
			});

			t.start();
			t.join();
			if (errorFlag) {
				if (errorTitle == null || errorDetails == null) {
					throw new ArrangeException(errorMessage);
				} else {
					throw new ArrangeException(errorMessage + System.lineSeparator() + errorTitle + ": "
							+ System.lineSeparator() + errorDetails);
				}
			}

			diagram = layoutVisualize.getDiagram();
			// FileVisualize.printOutput(diagram,
			// "C:\\Users\\dobreffandras\\Desktop", "visualize");
		} catch (InterruptedException e) {
			// On interrupt the normal behavior is to return from this method.
			// Cancellation will be handled my the monitor
			Logger.sys.info("Thread interrupted");
			return;
		}
	}

	private String formatStatements(Collection<Statement> statements) {
		List<String> statementMessages = new ArrayList<String>();
		for (Statement statement : statements) {
			statementMessages.add(statement.toString());
		}
		return this.formatDetails(statementMessages);
	}

	private String formatDetails(Collection<String> elements) {
		String msg = String.join(",\n", elements);
		return msg;
	}

	/**
	 * @return - The txtUML Objects
	 */
	public Set<RectangleObject> getObjects() {
		return diagram.Objects;
	}

	/**
	 * @return - The txtUML Links
	 */
	public Set<LineAssociation> getAssociations() {
		return diagram.Assocs;
	}

	/**
	 * @return - The txtUML Statements
	 */
	public List<Statement> getStatementsSet() {
		return statementsSet;
	}

	/**
	 * @return
	 */
	public int getPixelGridRatioHorizontal() {
		return diagram.getPixelGridHorizontal().intValue();
	}

	public int getPixelGridRatioVertical() {
		return diagram.getPixelGridVertical().intValue();
	}
}
