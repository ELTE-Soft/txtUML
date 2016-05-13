package hu.elte.txtuml.examples.printer;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend.AcceptingDocuments;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend.Init;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend.OutOfPaper;

public class PrinterFrontendSMDiagram extends StateMachineDiagram<PrinterFrontend> {
	@Row({Init.class, AcceptingDocuments.class, OutOfPaper.class})
	class L extends Layout{}
}
