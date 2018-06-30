package clock.j;

import java.time.LocalDateTime;

import clock.j.model.classes.Clock;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;

public class Demo implements Execution {
	
	@Override
	public void initialization() {
		LocalDateTime now = LocalDateTime.now();
		Action.create(Clock.class, now.getHour(), now.getMinute(), now.getSecond());		
	}
	
	public static void main(String[] args) {
		new Demo().run();
	}
}
