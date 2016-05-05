package hu.elte.txtuml.examples.printer;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.printer.model.PrinterBackend;
import hu.elte.txtuml.examples.printer.model.PrinterBackend.Init;
import hu.elte.txtuml.examples.printer.model.PrinterBackend.OutOfToner;
import hu.elte.txtuml.examples.printer.model.PrinterBackend.Printing;
import hu.elte.txtuml.examples.printer.model.PrinterBackend.Waiting;

class PrinterBackendSM extends StateMachineDiagram<PrinterBackend> {
	@Column({Init.class, Waiting.class, OutOfToner.class})
	@Right(from= Waiting.class, val=Printing.class)
	class L extends Layout{}
}
