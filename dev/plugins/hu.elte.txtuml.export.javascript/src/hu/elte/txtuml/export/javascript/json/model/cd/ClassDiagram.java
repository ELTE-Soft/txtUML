package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;

import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

public class ClassDiagram {
	@XmlAccessMethods(getMethodName="getName")
	private String name;
	@XmlAccessMethods(getMethodName="getClasses")
	private List<ClassNode> classes;
	@XmlAccessMethods(getMethodName="getAttributeLinks")
	private List<ClassAttributeLink> attributeLinks;
	@XmlAccessMethods(getMethodName="getNonAttributeLinks")
	private List<ClassLink> nonAttributeLinks;
	
	protected ClassDiagram() {}
	
	public ClassDiagram(String diagramName, Set<RectangleObject> nodes, Set<LineAssociation> links, ModelMapProvider map) {
		name = diagramName;
		classes = new ArrayList<ClassNode>();
		attributeLinks = new ArrayList<ClassAttributeLink>();
		nonAttributeLinks = new ArrayList<ClassLink>();
		
		for (RectangleObject node : nodes){
			Classifier clazz = (Classifier)map.getByName(node.getName());
			classes.add(new ClassNode(node, clazz));
		}
		
		for (LineAssociation link :  links){
			if (link.getType() == AssociationType.generalization){
				nonAttributeLinks.add(new ClassLink(link, AssociationType.generalization.toString()));
			}else{
				Association assoc = (Association)map.getByName(link.getId());
				attributeLinks.add(new ClassAttributeLink(link, assoc, (Classifier) map.getByName(link.getFrom()), (Classifier) map.getByName(link.getTo())));
			}
		}
		
	}

	public String getName() {
		return name;
	}

	public List<ClassNode> getClasses() {
		return classes;
	}

	public List<ClassAttributeLink> getAttributeLinks() {
		return attributeLinks;
	}

	public List<ClassLink> getNonAttributeLinks() {
		return nonAttributeLinks;
	}
	
}
