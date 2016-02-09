package hu.elte.txtuml.examples.printer.model;

import java.util.LinkedList;
import java.util.Queue;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.printer.model.associations.DocumentBeingPrinted;
import hu.elte.txtuml.examples.printer.model.associations.DocumentToPrint;
import hu.elte.txtuml.examples.printer.model.associations.PrinterSystem;
import hu.elte.txtuml.examples.printer.model.signals.FinishedPrinting;
import hu.elte.txtuml.examples.printer.model.signals.OutOfPaperSignal;
import hu.elte.txtuml.examples.printer.model.signals.Print;
import hu.elte.txtuml.examples.printer.model.signals.RestockPaper;

public class PrinterFrontend extends ModelClass {
	Queue<Document> queue;
	public int paperCount;
	boolean lock;

	public class Init extends Initial {
	}

	@From(Init.class)
	@To(AcceptingDocuments.class)
	public class Initialize extends Transition {
		@Override
		public void effect() {
			queue = new LinkedList<Document>();
			lock = false;
		}
	}

	public class AcceptingDocuments extends State {
		@Override
		public void entry() {
			if (queue.size() > 0) {
				PrinterBackend pb = PrinterFrontend.this.assoc(PrinterSystem.backend.class).selectAny();
				if (!lock) {
					lock = true;
					Document doc = queue.peek();
					if (paperCount < doc.sideCount) {
						Action.send(PrinterFrontend.this, new OutOfPaperSignal());
					} else {
						Action.link(DocumentBeingPrinted.beingPrinted.class, doc,
								DocumentBeingPrinted.PrinterBackEnd.class, pb);
						Action.send(pb, new Print());
						paperCount -= doc.sideCount;
						Action.unlink(DocumentToPrint.toPrint.class, doc, DocumentToPrint.PrinterFrontEnd.class,
								PrinterFrontend.this);
					}
					;
				}
				;
			}
			;

			Action.log("PrinterFrontend: the printer is waiting for documents.");
		}
	}

	@From(AcceptingDocuments.class)
	@To(AcceptingDocuments.class)
	@Trigger(FinishedPrinting.class)
	public class PrintFinished extends Transition {
		@Override
		public void effect() {
			queue.poll();

			lock = false;
			Action.log("PrinterFrontend: the printing of a document has finished. Remaining: " + queue.size()
					+ ". Papers: " + paperCount + ".");
		}
	}

	@From(AcceptingDocuments.class)
	@To(AcceptingDocuments.class)
	@Trigger(Print.class)
	public class PrintRecieved extends Transition {
		@Override
		public void effect() {
			Document doc = PrinterFrontend.this.assoc(DocumentToPrint.toPrint.class).selectAny();
			queue.add(doc);
			Action.log("PrinterFrontend: Document recieved. Queue size: " + queue.size() + ".");
		}
	}

	@From(AcceptingDocuments.class)
	@To(OutOfPaper.class)
	@Trigger(OutOfPaperSignal.class)
	public class Error extends Transition {
	}

	public class OutOfPaper extends State {
		@Override
		public void entry() {
			Action.log("PrinterFrontend: the printer is out of paper!");
		}
	}

	@From(OutOfPaper.class)
	@To(AcceptingDocuments.class)
	@Trigger(RestockPaper.class)
	public class Restocking extends Transition {
		@Override
		public void effect() {
			lock = false;
			paperCount += ((RestockPaper) getSignal()).amount;
			Action.log("PrinterFrontend: restocking paper.");
		}
	}

}

