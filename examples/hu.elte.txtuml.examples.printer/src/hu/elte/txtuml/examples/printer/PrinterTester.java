package hu.elte.txtuml.examples.printer;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.printer.model.Human;
import hu.elte.txtuml.examples.printer.model.PrinterBackend;
import hu.elte.txtuml.examples.printer.model.PrinterFrontend;
import hu.elte.txtuml.examples.printer.model.associations.PrinterSystem;
import hu.elte.txtuml.examples.printer.model.associations.Usage;
import hu.elte.txtuml.examples.printer.model.signals.RestockPaper;
import hu.elte.txtuml.examples.printer.model.signals.WantToPrint;

public class PrinterTester {

	void test() throws InterruptedException {
		ModelExecutor.Settings.setExecutorLog(false);

		PrinterFrontend p = Action.create(PrinterFrontend.class);
		PrinterBackend pb = Action.create(PrinterBackend.class);
		p.paperCount = 2;

		Human h1 = Action.create(Human.class, 2);
		Human h2 = Action.create(Human.class, 2);
		Human h3 = Action.create(Human.class, 2);
		Human h4 = Action.create(Human.class, 2);

		// building links
		Action.link(PrinterSystem.frontend.class, p, PrinterSystem.backend.class, pb);
		Action.link(Usage.usedPrinter.class, p, Usage.userOfPrinter.class, h1);
		Action.link(Usage.usedPrinter.class, p, Usage.userOfPrinter.class, h2);
		Action.link(Usage.usedPrinter.class, p, Usage.userOfPrinter.class, h3);
		Action.link(Usage.usedPrinter.class, p, Usage.userOfPrinter.class, h4);

		Action.log("Machine and human are starting.");
		Action.start(p);
		Action.start(pb);
		Action.start(h1);
		Action.start(h2);
		Action.start(h3);
		Action.start(h4);

		Action.send(h1, new WantToPrint()); // 2
		Thread.sleep(500);
		Action.send(h2, new WantToPrint()); // 2
		Thread.sleep(500);
		Action.send(h1, new WantToPrint()); // 2
		Thread.sleep(500);
		Action.send(h3, new WantToPrint()); // 2
		Thread.sleep(500);
		Action.send(h4, new WantToPrint()); // 2
		Thread.sleep(500);
		Action.send(h1, new WantToPrint()); // 2
		Thread.sleep(2000);
		Action.send(p, new RestockPaper(20));

		Thread.sleep(24000);

		Action.log("Test: pc: " + p.paperCount + ".");

		ModelExecutor.shutdown();
	}

	public static void main(String[] args) throws InterruptedException {
		new PrinterTester().test();
	}
}
