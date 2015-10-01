package hu.elte.txtuml.export.uml2.transform.importers;

import org.eclipse.jdt.core.dom.Statement;

public interface IControlStructImporter {
	
	public void importControlStructure(Statement statement);

}
