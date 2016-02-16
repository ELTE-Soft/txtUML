package hu.elte.txtuml.examples.printer.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.printer.model.Human;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;

public class Usage extends Association {
	public class usedPrinter extends One<PrinterFrontend> {
	}

	public class userOfPrinter extends Many<Human> {
	}
}