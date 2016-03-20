package hu.elte.txtuml.examples.printer.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.printer.model.Document;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;

public class DocumentQueue extends Association {

	public class element extends Many<Document> {
	}

	public class printerFrontend extends HiddenOne<PrinterFrontend> {
	}

}
