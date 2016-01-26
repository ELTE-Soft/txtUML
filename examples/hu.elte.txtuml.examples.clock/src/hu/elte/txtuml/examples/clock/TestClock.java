package hu.elte.txtuml.examples.clock;

import hu.elte.txtuml.examples.clock.model.classes.Clock;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;

public class TestClock {
	public static void main(String[] args) {
		ModelExecutor.Settings.setExecutorLog(false);
		Action.create(Clock.class);
	}
}
