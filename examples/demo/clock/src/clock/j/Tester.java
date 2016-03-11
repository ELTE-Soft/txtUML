package clock.j;

import java.time.LocalDateTime;

import clock.j.model.classes.Clock;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;

public class Tester {
	public static void main(String[] args) {
		ModelExecutor.Settings.setExecutorLog(false);
		LocalDateTime now = LocalDateTime.now();
		Action.create(Clock.class, now.getHour(), now.getMinute(), now.getSecond());
	}
}
