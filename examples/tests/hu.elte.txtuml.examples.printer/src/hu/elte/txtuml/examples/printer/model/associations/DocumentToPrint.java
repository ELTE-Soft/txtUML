package hu.elte.txtuml.examples.printer.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.printer.model.Document;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;

public class DocumentToPrint extends Association {
	public class toPrint extends Many<Document> {
	}

	public class PrinterFrontEnd extends HiddenOne<PrinterFrontend> {
	}
}