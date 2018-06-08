package machine1.j.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import machine1.j.model.signals.ButtonPress;

public class Machine extends ModelClass {

	@External private List<String> switchOnLog = new ArrayList<String>();
	@External private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	@ExternalBody
	private void logSwitchOn() {
		LocalDateTime now = LocalDateTime.now();
		switchOnLog.add(dtf.format(now));
	}

	@ExternalBody
	public void printSwitchOnLog() {
		for (String date : switchOnLog) {
			System.out.println(date);
		}
	}

	public class Init extends Initial {}

	public class Off extends State {
		@Override
		public void entry() {
			Action.log("\tMachine enters state: 'off'");
		}

		@Override
		public void exit() {
			Action.log("\tMachine exits state: 'off'");
		}
	}

	public class On extends State {
		@Override
		public void entry() {
			Action.log("\tMachine enters state: 'on'");
			logSwitchOn();
		}

		@Override
		public void exit() {
			Action.log("\tMachine exits state: 'on'");
		}
	}

	@From(Init.class) @To(Off.class)
	class Initialize extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: initializing...");
		}
	}

	@From(Off.class) @To(On.class) @Trigger(ButtonPress.class)
	class SwitchOn extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: switching on...");
		}
	}

	@From(On.class) @To(Off.class) @Trigger(ButtonPress.class)
	class SwitchOff extends Transition {
		@Override
		public void effect() {
			Action.log("\tMachine: switching off...");
		}
	}

}
