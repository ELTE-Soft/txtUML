package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Parameter;

public class Argument {
	@XmlAccessMethods(getMethodName = "getName")
	private String name;
	@XmlAccessMethods(getMethodName = "getType")
	private String type;

	protected Argument() {
	};

	public Argument(Parameter arg) {
		name = arg.getName();
		type = arg.getType().getName();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
}
