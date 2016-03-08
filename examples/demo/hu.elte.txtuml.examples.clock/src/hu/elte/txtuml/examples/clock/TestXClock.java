package hu.elte.txtuml.examples.clock;

import java.time.LocalDateTime;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.clock.xmodel.Clock;

public class TestXClock {
	public static void main(String[] args) {
		ModelExecutor.Settings.setExecutorLog(false);
		LocalDateTime now = LocalDateTime.now();
		Action.create(Clock.class, now.getHour(), now.getMinute(), now.getSecond());
	}
}
