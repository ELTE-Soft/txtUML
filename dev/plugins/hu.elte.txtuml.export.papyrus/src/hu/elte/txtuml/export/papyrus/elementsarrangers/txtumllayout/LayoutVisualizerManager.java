package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxArrangeConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxOverlapConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementsConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

/**
 * The instance of this class handles the layout algorithm
 */
public class LayoutVisualizerManager {
	private Set<RectangleObject> objects;
	private Set<LineAssociation> associations;
	private List<Statement> statementsSet;
	private IProgressMonitor progressMonitor = new NullProgressMonitor();
	private final LayoutVisualize layoutVisualize;
	
	/**
	 * The Constructor
	 * @param objects - Rectangle Objects to be arranged by the algorithm
	 * @param links  - Links to be arranged by the algorithm
	 * @param statements - statements that are to be considered by the arrangement
	 * @param txtUmlRegistry - The TxtUMLElementsFinder that connects the model elements with txtUML names  
	 */
	public LayoutVisualizerManager(Set<RectangleObject> objects, Set<LineAssociation> links, List<Statement> statements) {
		this.objects = objects;
		this.associations = links;
		this.statementsSet = statements;
		layoutVisualize = new LayoutVisualize();
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
	 * @throws ArrangeException 
	 */
	public void arrange() throws ArrangeException {

		layoutVisualize.addObserver(new Observer() {
			
			int previous_percent = 0;
			
			@Override
			public void update(Observable o, Object arg) {
				Integer percent = (Integer) arg;
				progressMonitor.worked(percent-this.previous_percent);
				progressMonitor.subTask("Arranging elements... "+percent+"%");
				this.previous_percent = this.previous_percent+percent;
			}
		});
		
		layoutVisualize.load(objects, associations);
		try {
			progressMonitor.beginTask("Arranging elements", 100);
			progressMonitor.subTask("Arranging elements...");
			layoutVisualize.arrange(new ArrayList<Statement>(statementsSet));
		} catch (InternalException| BoxArrangeConflictException|
				ConversionException| StatementTypeMatchException|
				CannotFindAssociationRouteException| UnknownStatementException|
				BoxOverlapConflictException| StatementsConflictException e) {
			
			String explanation = null;
			String details = null;
			
			if(e instanceof BoxArrangeConflictException){
				List<Statement> statements =((BoxArrangeConflictException) e).ConflictStatements;
				explanation = "Conflicting statements";
				details = formatStatements(statements != null ? statements : new ArrayList<Statement>());
			}else if(e instanceof BoxOverlapConflictException){
				List<String> boxes =((BoxOverlapConflictException) e).OverlappingBoxes;
				explanation = "Overlapping boxes";
				details = formatDetails(boxes != null ? boxes : new ArrayList<String>());
			}else if(e instanceof StatementsConflictException){
				List<Statement> statements =((StatementsConflictException) e).ConflictStatements;
				explanation = "Conflicting statements";
				details = formatStatements(statements != null ? statements : new ArrayList<Statement>());
			}else if(e instanceof UnknownStatementException){
				String statement =((UnknownStatementException) e).Statement;
				explanation = "Unknown statement";
				details = statement;
			}
			
			if(explanation == null || details == null){
				throw new ArrangeException(e.getMessage());
			}else{
				throw new ArrangeException(e.getMessage()+" \n"+explanation+": \n"+details);
			}
		}

		objects = layoutVisualize.getObjects();
		associations = layoutVisualize.getAssocs();
	}
	
	private String formatStatements(Collection<Statement> statements){
		List<String> statementMessages = new ArrayList<String>();
		for(Statement statement : statements){
			statementMessages.add(statement.toString());
		}
		return this.formatDetails(statementMessages);
	}
	
	private String formatDetails(Collection<String> elements){
		String msg = String.join(",\n", elements);
		return msg;
	}
	
	/**
	 * @return - The txtUML Objects
	 */
	public Set<RectangleObject> getObjects() {
		return objects;
	}
	
	/**
	 * @return - The txtUML Links
	 */
	public Set<LineAssociation> getAssociations() {
		return associations;
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
	public int getPixelGridRatioHorizontal(){
		return layoutVisualize.getPixelGridHorizontal().intValue();
	}
	
	public int getPixelGridRatioVertical(){
		return layoutVisualize.getPixelGridVertical().intValue();
	}
}
