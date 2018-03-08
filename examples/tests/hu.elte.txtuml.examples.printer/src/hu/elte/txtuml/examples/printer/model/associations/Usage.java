package hu.elte.txtuml.examples.printer.model.associations;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.examples.printer.model.Human;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;

public class Usage extends Association {
	public class usedPrinter extends End<One<PrinterFrontend>> {
	}

	public class userOfPrinter extends End<Any<Human>> {
	}
}