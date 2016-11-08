package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Property;

public class Attribute extends ClassMember {
	@XmlAccessMethods(getMethodName="getType")
	private String type;
	
	protected Attribute(){}
	
	
	public Attribute(Property attr) {
		super(attr);
		type = attr.getType().getName();
	}

	public String getType() {
		return type;
	}


}
