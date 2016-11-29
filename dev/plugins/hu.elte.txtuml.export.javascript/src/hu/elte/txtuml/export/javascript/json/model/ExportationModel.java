package hu.elte.txtuml.export.javascript.json.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.export.javascript.json.model.cd.ClassDiagram;
import hu.elte.txtuml.export.javascript.json.model.smd.SMDiagram;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public class ExportationModel {

	@XmlAccessMethods(getMethodName = "getClassDiagrams")
	private List<ClassDiagram> classDiagrams;
	@XmlAccessMethods(getMethodName = "getStateMachines")
	private List<SMDiagram> stateMachines;

	public ExportationModel() {
		classDiagrams = new ArrayList<ClassDiagram>();
		stateMachines = new ArrayList<SMDiagram>();
	}

	public void addClassDiagram(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links,
			ModelMapProvider map, double spacing) {
		classDiagrams.add(new ClassDiagram(diagramName, nodes, links, map, spacing));
	}

	public void addStateMachine(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links,
			ModelMapProvider map, double spacing) {
		stateMachines.add(new SMDiagram(diagramName, nodes, links, map, spacing));
	}

	public List<ClassDiagram> getClassDiagrams() {
		return classDiagrams;
	}

	public List<SMDiagram> getStateMachines() {
		return stateMachines;
	}

}
