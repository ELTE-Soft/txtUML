package hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout;

import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsRegistry;
import hu.elte.txtuml.layout.visualizer.algorithms.LayoutVisualize;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The instance of this class handles the layout algorithm
 * @author András Dobreff
 */
public class LayoutVisualizerManager {
	private Set<RectangleObject> objects;
	private Set<LineAssociation> associations;
	private List<Statement> statementsSet;
	
	/**
	 * The Constructor
	 * @param txtUmlRegistry - The TxtUMLElementsFinder that connects the model elements with txtUML names  
	 */
	public LayoutVisualizerManager(TxtUMLElementsRegistry txtUmlRegistry) {
		this.objects = txtUmlRegistry.getDescriptor().report.getNodes();
		this.associations = txtUmlRegistry.getDescriptor().report.getLinks();
		this.statementsSet = txtUmlRegistry.getDescriptor().report.getStatements();
	}
	
	/**
	 * Arranging command
	 * @throws UnknownStatementException 
	 * @throws CannotFindAssociationRouteException 
	 * @throws StatementTypeMatchException 
	 * @throws ConversionException 
	 * @throws ConflictException 
	 * @throws InternalException 
	 */
	public void arrange() throws InternalException, ConflictException, ConversionException,
									StatementTypeMatchException, CannotFindAssociationRouteException,
									UnknownStatementException {
		LayoutVisualize v = new LayoutVisualize();
		v.load(objects, associations);
		v.arrange(new ArrayList<Statement>(statementsSet));

		objects = v.getObjects();
		associations = v.getAssocs();
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
