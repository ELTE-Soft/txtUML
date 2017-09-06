package hu.elte.txtuml.examples.feeder;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.examples.feeder.model.Sink;
import hu.elte.txtuml.examples.feeder.model.Source;

public class RunSimulation {

	static void init() {
		Source source = Action.create(Source.class);
		Sink sink = Action.create(Sink.class, source);
		Action.start(source);
		Action.start(sink);
		sink.start();
	}

	public static void main(String[] args) {
		ModelExecutor.create().setTraceLogging(false).run(RunSimulation::init);
	}
	
}
