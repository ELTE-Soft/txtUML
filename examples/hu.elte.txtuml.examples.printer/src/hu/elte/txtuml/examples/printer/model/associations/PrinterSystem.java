package hu.elte.txtuml.examples.printer.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.printer.model.PrinterBackend;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;

public class PrinterSystem extends Association {
	public class frontend extends One<PrinterFrontend> {
	}

	public class backend extends One<PrinterBackend> {
	}
}