package hu.elte.txtuml.examples.printer.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.printer.model.Document;
import hu.elte.txtuml.examples.printer.model.PrinterBackend;

public class DocumentBeingPrinted extends Association {
	public class beingPrinted extends One<Document> {
	}

	public class PrinterBackEnd extends HiddenOne<PrinterBackend> {
	}
}