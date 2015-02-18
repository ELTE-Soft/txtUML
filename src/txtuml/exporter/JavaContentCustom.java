package txtuml.exporter;

import java.util.ArrayList;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;

public class JavaContentCustom {
	protected static int tabs;
	protected Model model;
	protected ArrayList<String> multipleNames;

	public JavaContentCustom(Model model, ArrayList<String> multipleNames) {
		tabs = 0;
		this.model = model;
		this.multipleNames = multipleNames;
	}

	public String parse() {
		return getContent(model);
	}

	public boolean isMultipleName(String name) {
		return multipleNames.contains(name);
	}

	public String getContent(Element element) {
		String content = "";
		++tabs;

		CodeRecognizer cr = new CodeRecognizer();

		content += cr.getCode(this, element, tabs, LiteralTypes.Property);
		content += cr.getCode(this, element, tabs, LiteralTypes.Class);
		content += cr.getCode(this, element, tabs, LiteralTypes.Pseudostate);
		content += cr.getCode(this, element, tabs, LiteralTypes.State);
		content += cr.getCode(this, element, tabs, LiteralTypes.Signal);
		content += cr.getCode(this, element, tabs, LiteralTypes.Association);
		content += cr.getCode(this, element, tabs, LiteralTypes.Transition);
		content += cr.getCode(this, element, tabs, LiteralTypes.Operation);

		--tabs;
		return content;
	}
}
