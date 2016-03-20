package hu.elte.txtuml.examples.printer.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.printer.model.associations.DocumentBeingPrinted;
import hu.elte.txtuml.examples.printer.model.associations.PrinterSystem;
import hu.elte.txtuml.examples.printer.model.signals.FinishedPrinting;
import hu.elte.txtuml.examples.printer.model.signals.Print;

public class PrinterBackend extends ModelClass {
	int tonerPercent;

	public class Init extends Initial {
	}

	@From(Init.class)
	@To(Waiting.class)
	public class Initialize extends Transition {
		@Override
		public void effect() {
			tonerPercent = 100;
		}
	}

	public class Waiting extends State {
	}

	@From(Waiting.class)
	@To(Printing.class)
	@Trigger(Print.class)
	public class RecievedJob extends Transition {
		@Override
		public boolean guard() {
			Document doc = PrinterBackend.this.assoc(DocumentBeingPrinted.beingPrinted.class).selectAny();
			return tonerPercent >= doc.sideCount;
		}

		@Override
		public void effect() {
			Action.log("PrinterBackend: Recieved document to print. Has enough papers to do it.");
		}
	}

	@From(Waiting.class)
	@To(OutOfToner.class)
	@Trigger(Print.class)
	public class RecievedJobError extends Transition {
		@Override
		public boolean guard() {
			Document doc = PrinterBackend.this.assoc(DocumentBeingPrinted.beingPrinted.class).selectAny();
			return tonerPercent < doc.sideCount;
		}

		@Override
		public void effect() {
			Action.log("PrinterBackend: Recieved document to print. Not enough paper to do it.");
		}
	}

	public class Printing extends State {
		@Override
		public void entry() {
			Action.log("PrinterBackend: started printing.");
			Document doc = PrinterBackend.this.assoc(DocumentBeingPrinted.beingPrinted.class).selectAny();
			for (int i = 0; i < doc.sideCount; ++i) {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			;
			tonerPercent -= doc.sideCount;
			Action.log("PrinterBackend: finished printing.");
			Action.send(new FinishedPrinting(), PrinterBackend.this);
		}
	}

	@From(Printing.class)
	@To(Waiting.class)
	@Trigger(FinishedPrinting.class)
	public class FinishedJob extends Transition {
		@Override
		public void effect() {
			Document doc = PrinterBackend.this.assoc(DocumentBeingPrinted.beingPrinted.class).selectAny();
			Action.unlink(DocumentBeingPrinted.beingPrinted.class, doc, DocumentBeingPrinted.printerBackend.class,
					PrinterBackend.this);
			PrinterFrontend pf = PrinterBackend.this.assoc(PrinterSystem.frontend.class).selectAny();
			Action.send(new FinishedPrinting(), pf);
		}
	}

	public class OutOfToner extends State {
		@Override
		public void entry() {
			Action.log("PrinterBackend: out of toner!");
		}
	}

}