package hu.elte.txtuml.examples.printer;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.examples.printer.model.Human;
import hu.elte.txtuml.examples.printer.model.PrinterBackend;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;
import hu.elte.txtuml.examples.printer.model.associations.PrinterSystem;
import hu.elte.txtuml.examples.printer.model.associations.Usage;
import hu.elte.txtuml.examples.printer.model.signals.RestockPaper;
import hu.elte.txtuml.examples.printer.model.signals.WantToPrint;

public class PrinterTester {

	static Human h1;
	static Human h2;
	static Human h3;
	static Human h4;
	static PrinterFrontend p;
	static PrinterBackend pb;

	static void init() {
		p = Action.create(PrinterFrontend.class);
		pb = Action.create(PrinterBackend.class);
		p.paperCount = 2;

		h1 = Action.create(Human.class, 2);
		h2 = Action.create(Human.class, 2);
		h3 = Action.create(Human.class, 2);
		h4 = Action.create(Human.class, 2);

		// building links
		Action.link(PrinterSystem.frontend.class, p, PrinterSystem.backend.class, pb);
		Action.link(Usage.usedPrinter.class, p, Usage.userOfPrinter.class, h1);
		Action.link(Usage.usedPrinter.class, p, Usage.userOfPrinter.class, h2);
		Action.link(Usage.usedPrinter.class, p, Usage.userOfPrinter.class, h3);
		Action.link(Usage.usedPrinter.class, p, Usage.userOfPrinter.class, h4);

		Action.log("Machine and humans are starting.");
		Action.start(p);
		Action.start(pb);
		Action.start(h1);
		Action.start(h2);
		Action.start(h3);
		Action.start(h4);
	}

	public static void main(String[] args) throws InterruptedException {
		ModelExecutor executor = ModelExecutor.create().setLogLevel(LogLevel.TRACE).start(PrinterTester::init);

		API.send(new WantToPrint(), h1); // 2
		Thread.sleep(500);
		API.send(new WantToPrint(), h2); // 2
		Thread.sleep(500);
		API.send(new WantToPrint(), h1); // 2
		Thread.sleep(500);
		API.send(new WantToPrint(), h3); // 2
		Thread.sleep(500);
		API.send(new WantToPrint(), h4); // 2
		Thread.sleep(500);
		API.send(new WantToPrint(), h1); // 2
		Thread.sleep(2000);
		API.send(new RestockPaper(20), p);

		Thread.sleep(24000);

		API.log("Test: pc: " + p.paperCount + "."); // TODO unsafe access

		executor.shutdown();
	}
}
