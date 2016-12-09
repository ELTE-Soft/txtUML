package hu.elte.txtuml.export.javascript.json.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.export.javascript.json.model.cd.ClassDiagram;
import hu.elte.txtuml.export.javascript.json.model.cd.UnexpectedEndException;
import hu.elte.txtuml.export.javascript.json.model.smd.SMDiagram;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

/**
 * 
 * This model holds the models of the diagrams to be exported
 *
 */
public class ExportationModel {

	@XmlAccessMethods(getMethodName = "getClassDiagrams")
	private List<ClassDiagram> classDiagrams;
	@XmlAccessMethods(getMethodName = "getStateMachines")
	private List<SMDiagram> stateMachines;

	/**
	 * Constructor
	 */
	public ExportationModel() {
		classDiagrams = new ArrayList<ClassDiagram>();
		stateMachines = new ArrayList<SMDiagram>();
	}

	/**
	 * Creates a new ClassDiagram model form the given arguments, and adds it to
	 * the model
	 * 
	 * @param diagramName
	 *            The name of the diagram
	 * @param nodes
	 *            The nodes of the already arranged diagram which are containing
	 *            abstract positions and sizes
	 * @param links
	 *            The links of the already arranged diagram which are containing
	 *            abstract routes
	 * @param map
	 *            The map which links the layout informations to the EMF-UML
	 *            model
	 * @param spacing
	 *            The desired spacing
	 * @throws UnexpectedEndException
	 *             Exception is thrown if an association's end could not be
	 *             linked to the EMF-UML model
	 */
	public void addClassDiagram(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links,
			ModelMapProvider map, double spacing) throws UnexpectedEndException {
		classDiagrams.add(new ClassDiagram(diagramName, nodes, links, map, spacing));
	}

	/**
	 * Creates a new StateMachine model form the given arguments, and adds it to
	 * the model
	 * 
	 * @param diagramName
	 *            The name of the diagram
	 * @param nodes
	 *            The nodes of the already arranged diagram which are containing
	 *            abstract positions and sizes
	 * @param links
	 *            The links of the already arranged diagram which are containing
	 *            abstract routes
	 * @param map
	 *            The map which links the layout informations to the EMF-UML
	 *            model
	 * @param spacing
	 *            The desired spacing
	 */
	public void addStateMachine(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links,
			ModelMapProvider map, double spacing) {
		stateMachines.add(new SMDiagram(diagramName, nodes, links, map, spacing));
	}

	/**
	 * @return the ClassDiagrams contained in the model
	 */
	public List<ClassDiagram> getClassDiagrams() {
		return classDiagrams;
	}

	/**
	 * @return the StateMachines contained in the model
	 */
	public List<SMDiagram> getStateMachines() {
		return stateMachines;
	}

}
