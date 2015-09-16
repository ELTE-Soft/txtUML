package hu.elte.txtuml.examples.printer;

import hu.elte.txtuml.examples.printer.PrinterModel.*;
import hu.elte.txtuml.layout.lang.Diagram;
import hu.elte.txtuml.layout.lang.statements.*;

class PrinterDiagram extends Diagram
{
	@Above(val = PrinterFrontend.class, 
			from = PrinterBackend.class)
	@Left(val = Human.class, 
			from = PrinterFrontend.class)
	@West(val = Document.class, 
		from = PrinterFrontend.class)
    class PrinterLayout extends Layout {}
}