package clock.j;

import java.time.LocalDateTime;

import clock.j.model.classes.Clock;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;

public class Tester {
	public static void main(String[] args) {
		ModelExecutor.create().start(() -> {
			LocalDateTime now = LocalDateTime.now();
			Action.create(Clock.class, now.getHour(), now.getMinute(), now.getSecond());
		});
	}
}
