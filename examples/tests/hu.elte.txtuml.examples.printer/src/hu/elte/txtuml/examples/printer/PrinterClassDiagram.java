package hu.elte.txtuml.examples.printer;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.West;
import hu.elte.txtuml.examples.printer.model.Document;
import hu.elte.txtuml.examples.printer.model.Human;
import hu.elte.txtuml.examples.printer.model.PrinterBackend;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;

public class PrinterClassDiagram extends ClassDiagram {
	@Above(val = PrinterFrontend.class, from = PrinterBackend.class)
	@Left(val = Human.class, from = PrinterFrontend.class)
	@West(val = Document.class, from = PrinterFrontend.class)
	class PrinterLayout extends Layout {
	}
}