package hu.elte.txtuml.examples.printer.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.printer.model.associations.DocumentBeingPrinted;
import hu.elte.txtuml.examples.printer.model.associations.DocumentQueue;
import hu.elte.txtuml.examples.printer.model.associations.DocumentToPrint;
import hu.elte.txtuml.examples.printer.model.associations.PrinterSystem;
import hu.elte.txtuml.examples.printer.model.signals.FinishedPrinting;
import hu.elte.txtuml.examples.printer.model.signals.OutOfPaperSignal;
import hu.elte.txtuml.examples.printer.model.signals.Print;
import hu.elte.txtuml.examples.printer.model.signals.RestockPaper;

public class PrinterFrontend extends ModelClass {
	public int paperCount;
	boolean lock;

	public class Init extends Initial {
	}

	@From(Init.class)
	@To(AcceptingDocuments.class)
	public class Initialize extends Transition {
		@Override
		public void effect() {
			lock = false;
		}
	}

	public class AcceptingDocuments extends State {
		@Override
		public void entry() {
			if (size() > 0) {
				PrinterBackend pb = PrinterFrontend.this.assoc(PrinterSystem.backend.class).one();
				if (!lock) {
					lock = true;
					Document doc = first();
					if (paperCount < doc.sideCount) {
						Action.send(new OutOfPaperSignal(), PrinterFrontend.this);
					} else {
						Action.link(DocumentBeingPrinted.beingPrinted.class, doc,
								DocumentBeingPrinted.printerBackend.class, pb);
						Action.send(new Print(), pb);
						paperCount -= doc.sideCount;
						Action.unlink(DocumentToPrint.toPrint.class, doc, DocumentToPrint.printerFrontend.class,
								PrinterFrontend.this);
					}
				}
			}

			Action.log("PrinterFrontend: the printer is waiting for documents.");
		}
	}

	@From(AcceptingDocuments.class)
	@To(AcceptingDocuments.class)
	@Trigger(FinishedPrinting.class)
	public class PrintFinished extends Transition {
		@Override
		public void effect() {
			removeFirst();

			lock = false;
			Action.log("PrinterFrontend: the printing of a document has finished. Remaining: " + size() + ". Papers: "
					+ paperCount + ".");
		}

	}

	@From(AcceptingDocuments.class)
	@To(AcceptingDocuments.class)
	@Trigger(Print.class)
	public class PrintRecieved extends Transition {
		@Override
		public void effect() {
			Document doc = PrinterFrontend.this.assoc(DocumentToPrint.toPrint.class).one();
			addAsLast(doc);
			Action.log("PrinterFrontend: Document recieved. Queue size: " + size() + ".");
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
			paperCount += getTrigger(RestockPaper.class).amount;
			Action.log("PrinterFrontend: restocking paper.");
		}
	}

	private void addAsLast(Document doc) {
		Action.link(DocumentQueue.element.class, doc, DocumentQueue.printerFrontend.class, this);
	}

	private Document first() {
		return assoc(DocumentQueue.element.class).one();
	}

	private void removeFirst() {
		Document toRemove = assoc(DocumentQueue.element.class).one();
		
		Action.unlink(DocumentQueue.element.class, toRemove, DocumentQueue.printerFrontend.class, this);
	}

	private int size() {
		return assoc(DocumentQueue.element.class).size();
	}

}
