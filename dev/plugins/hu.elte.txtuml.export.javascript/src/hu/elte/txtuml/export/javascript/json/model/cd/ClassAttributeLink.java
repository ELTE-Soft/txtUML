package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

public class ClassAttributeLink extends ClassLink {
	
	@XmlAccessMethods(getMethodName="getFrom")
	private AssociationEnd from;
	@XmlAccessMethods(getMethodName="getTo")
	private AssociationEnd to;
	@XmlAccessMethods(getMethodName="getName")
	protected String name;
	
	
	

	protected ClassAttributeLink() {}
	
	public ClassAttributeLink(LineAssociation layout, Association assoc, Classifier fromClass, Classifier toClass) {
		super(layout);
		type = "attribute";
		name = assoc.getLabel();
		from = null;
		to = null;
		for (Property end : assoc.getMemberEnds()){		
			Element ownerElement = end.getOwner();
			if (!(ownerElement instanceof Class)){
				if (from == null){
					from = new AssociationEnd(layout.getFrom(), end);
				}else{
					to = new AssociationEnd(layout.getTo(), end);
				}
			}else{
				Class owner = (Class) ownerElement;
				if (owner == fromClass && from == null){
					from = new AssociationEnd(layout.getFrom(), end);
				}else if (owner == toClass && to == null){
					to = new AssociationEnd(layout.getTo(), end);
				} 
			}
		}		
	}

	public AssociationEnd getFrom() {
		return from;
	}

	public AssociationEnd getTo() {
		return to;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}


}
