package hu.elte.txtuml.examples.printer.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.printer.model.associations.DocumentToPrint;
import hu.elte.txtuml.examples.printer.model.associations.Usage;
import hu.elte.txtuml.examples.printer.model.signals.Print;
import hu.elte.txtuml.examples.printer.model.signals.WantToPrint;

public class Human extends ModelClass {
	int count;

	public Human(int c) {
		count = c;
	}

	public class Init extends Initial {
	}

	public class DoPrint extends State {
		@Override
		public void entry() {
			PrinterFrontend p = Human.this.assoc(Usage.usedPrinter.class).selectAny();

			Document doc = Action.create(Document.class);
			doc.sideCount = count;

			Action.link(DocumentToPrint.toPrint.class, doc, DocumentToPrint.PrinterFrontEnd.class, p);
			Action.send(new Print(), p);
		}
	}

	@From(Init.class)
	@To(DoPrint.class)
	@Trigger(WantToPrint.class)
	public class Initialize extends Transition {
	}

	@From(DoPrint.class)
	@To(DoPrint.class)
	@Trigger(WantToPrint.class)
	public class Redo extends Transition {
	}
}