
package hu.elte.txtuml.export.javascript.json;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;

import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class JSONExportModel {
	private Set<RectangleObject> nodes;
	private Set<LineAssociation> links;
	
	public JSONExportModel(){
		this(null,null);
	}

	public JSONExportModel(Set<RectangleObject> nodes, Set<LineAssociation> links){
		this.nodes = nodes;
		this.links = links;
	}
	
	@XmlAccessMethods(getMethodName="getNodes")
	public Set<RectangleObject> getNodes() {
		return nodes;
	}
	
	@XmlAccessMethods(getMethodName="getLinks")
	public Set<LineAssociation> getLinks() {
		return links;
	}
	
}
