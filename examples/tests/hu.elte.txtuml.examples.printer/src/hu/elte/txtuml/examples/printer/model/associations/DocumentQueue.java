package hu.elte.txtuml.examples.printer.model.associations;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.examples.printer.model.Document;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;

public class DocumentQueue extends Association {

	public class element extends End<Any<Document>> {
	}

	public class printerFrontend extends HiddenEnd<One<PrinterFrontend>> {
	}

}
