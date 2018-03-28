package hu.elte.txtuml.export.javascript.json.model;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.export.javascript.UnexpectedDiagramTypeException;
import hu.elte.txtuml.export.javascript.json.model.cd.ClassDiagram;
import hu.elte.txtuml.export.javascript.json.model.cd.UnexpectedEndException;
import hu.elte.txtuml.export.javascript.json.model.smd.SMDiagram;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.exceptions.ArrangeException;

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
	@XmlAccessMethods(getMethodName = "getModelName")
	private String modelName;

	/**
	 * Constructor
	 */
	public ExportationModel() {
		classDiagrams = new ArrayList<ClassDiagram>();
		stateMachines = new ArrayList<SMDiagram>();
		this.modelName = "Untitled";
	}

	/**
	 * Creates a new diagram model form the given arguments, and adds it to the
	 * model
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
	 * @throws UnexpectedEndException
	 *             Exception is thrown if an association's end could not be
	 *             linked to the EMF-UML model
	 * @throws ArrangeException
	 *             Exception is thrown if a diagram could not be arranged
	 * @throws UnexpectedDiagramTypeException
	 *             Exception is thrown if a diagram type is not supported
	 * @throws UnexpectedException
	 *             Exception is thrown if a diagram contains unexpected parts
	 */
	public void createDiagram(String diagramName, DiagramExportationReport der, ModelMapProvider map)
			throws UnexpectedEndException, ArrangeException, UnexpectedDiagramTypeException, UnexpectedException {

		switch (der.getType()) {
		case Class:
			classDiagrams.add(new ClassDiagram(diagramName, der, map));
			break;
		case StateMachine:
			stateMachines.add(new SMDiagram(diagramName, der, map));
			break;
		default:
			throw new UnexpectedDiagramTypeException(diagramName, der.getType().name());
		}
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
	
	/**
	 * @return the model name
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * @param modelName - the name of the model
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}
