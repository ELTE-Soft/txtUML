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
	
	@XmlAccessMethods(getMethodName="getClassDiagrams")
	private List<ClassDiagram> classDiagrams;
	@XmlAccessMethods(getMethodName="getStateChartDiagrams")
	private List<SMDiagram> stateChartDiagrams;
	
	public ExportationModel(){
		classDiagrams = new ArrayList<ClassDiagram>();
		stateChartDiagrams = new ArrayList<SMDiagram>();
	}

	public void addClassDiagram(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links, ModelMapProvider map){
		classDiagrams.add(new ClassDiagram(diagramName, nodes, links, map));
	}
	
	public void addStateChartDiagram(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links, ModelMapProvider map){
		stateChartDiagrams.add(new SMDiagram(diagramName, nodes, links, map));
	}
	
	public List<ClassDiagram> getClassDiagrams() {
		return classDiagrams;
	}
	
	public List<SMDiagram> getStateChartDiagrams() {
		return stateChartDiagrams;
	}
	
	 
	
}
