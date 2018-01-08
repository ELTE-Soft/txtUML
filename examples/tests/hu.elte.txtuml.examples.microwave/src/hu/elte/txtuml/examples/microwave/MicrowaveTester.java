package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.examples.microwave.model.Human;
import hu.elte.txtuml.examples.microwave.model.Microwave;
import hu.elte.txtuml.examples.microwave.model.associations.Usage;
import hu.elte.txtuml.examples.microwave.model.signals.Close;
import hu.elte.txtuml.examples.microwave.model.signals.Get;
import hu.elte.txtuml.examples.microwave.model.signals.Open;
import hu.elte.txtuml.examples.microwave.model.signals.Put;
import hu.elte.txtuml.examples.microwave.model.signals.SetIntensity;
import hu.elte.txtuml.examples.microwave.model.signals.SetTime;
import hu.elte.txtuml.examples.microwave.model.signals.Start;
import hu.elte.txtuml.examples.microwave.model.signals.Stop;

public class MicrowaveTester {

	static Microwave m;
	static Human h;

	static void init() {
		m = Action.create(Microwave.class);
		h = Action.create(Human.class);

		Action.link(Usage.usedMicrowave.class, m, Usage.userOfMicrowave.class, h);

		Action.log("Machine and human are starting.");
		Action.start(m);
		Action.start(h);
	}

	public static void main(String args) throws InterruptedException {
		ModelExecutor executor = ModelExecutor.create().setLogLevel(LogLevel.TRACE).start(MicrowaveTester::init);
		
		String inp = "";
		do {
			System.out.println("Do Action: ");
			inp = System.console().readLine();
			inp = inp.toLowerCase();

			switch (inp) {
			case "open":
				API.send(new Open(), m);
				break;
			case "close":
				API.send(new Close(), m);
				break;
			case "put":
				API.send(new Put(), m);
				break;
			case "get":
				API.send(new Get(), m);
				break;
			case "setintensity":
				API.log("  Intensity Level (1-5): ");
				Integer i = Integer.parseInt(System.console().readLine());
				API.send(new SetIntensity(i), m);
				break;
			case "settime":
				API.log("  Time in sec(s): ");
				Integer t = Integer.parseInt(System.console().readLine());
				API.send(new SetTime(t), m);
				break;
			case "start":
				API.send(new Start(), m);
				break;
			case "stop":
				API.send(new Stop(), m);
				break;
			case "quit":

				break;
			case "help":
			default:
				System.out.println("Actions to use: Open, Close, Put, " + "Get, SetIntensity, SetTime, Start, Stop.");
				break;
			}

		} while (!inp.equals("quit"));

		executor.shutdown();
	}

}