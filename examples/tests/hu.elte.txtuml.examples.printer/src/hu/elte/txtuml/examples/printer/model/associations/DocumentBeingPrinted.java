package hu.elte.txtuml.examples.printer.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.examples.printer.model.Document;
import hu.elte.txtuml.examples.printer.model.PrinterBackend;

public class DocumentBeingPrinted extends Association {
	public class beingPrinted extends End<One<Document>> {
	}

	public class printerBackend extends HiddenEnd<One<PrinterBackend>> {
	}
}