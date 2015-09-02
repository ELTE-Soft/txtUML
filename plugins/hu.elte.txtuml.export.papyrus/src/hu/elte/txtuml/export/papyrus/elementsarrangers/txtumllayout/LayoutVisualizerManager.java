package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * The instance of this class handles the layout algorithm
 * @author András Dobreff
 */
public class LayoutVisualizerManager {
	private Set<RectangleObject> objects;
	private Set<LineAssociation> associations;
	private List<Statement> statementsSet;
	private IProgressMonitor progressMonitor = new NullProgressMonitor();
	
	/**
	 * The Constructor
	 * @param txtUmlRegistry - The TxtUMLElementsFinder that connects the model elements with txtUML names  
	 */
	public LayoutVisualizerManager(DiagramExportationReport report) {
		this.objects = report.getNodes();
		this.associations = report.getLinks();
		this.statementsSet = report.getStatements();
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
		
		LayoutVisualize v = new LayoutVisualize();
		v.addObserver(new Observer() {
			
			int previous_percent = 0;
			
			@Override
			public void update(Observable o, Object arg) {
				Integer percent = (Integer) arg;
				progressMonitor.worked(percent-this.previous_percent);
				progressMonitor.subTask("Arranging elements... "+percent+"%");
				this.previous_percent = this.previous_percent+percent;
			}
		});
		
		v.load(objects, associations);
		try {
			progressMonitor.beginTask("Arranging elements", 100);
			progressMonitor.subTask("Arranging elements...");
			v.arrange(new ArrayList<Statement>(statementsSet));
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

		objects = v.getObjects();
		associations = v.getAssocs();
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
}
