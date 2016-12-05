package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

public abstract class ClassMember {
	@XmlAccessMethods(getMethodName = "getVisibility")
	protected String visibility;
	@XmlAccessMethods(getMethodName = "getName")
	protected String name;

	protected ClassMember() {
	}

	protected ClassMember(Property attr) {
		name = attr.getName();
		visibility = attr.getVisibility().getLiteral();

	}

	public ClassMember(Operation op) {
		name = op.getName();
		visibility = op.getVisibility().getLiteral();
	}

	public String getVisibility() {
		return visibility;
	}

	public String getName() {
		return name;
	}

}
