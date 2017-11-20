package hu.elte.txtuml.export.cpp.wizardz;

import java.util.Arrays;
import java.util.List;

public enum DemoExpectedLines {
	MACHINE(Arrays.asList("Machine and users are starting.",
			"Machine::Initialize",
			"Machine: initializing...",
			"Machine enters state: 'off'",
			"User::Initialize",
			"User: initializing...",
			"User::Initialize",
			"User: initializing...",
			"User::Working",
			"User: working...",
			"User: starting to work...",
			"User: work finished...",
			"Machine exits state: 'off'",
			"Machine::SwitchOn",
			"Machine: switching on...",
			"Machine enters state: 'on'",
			"Machine exits state: 'on'",
			"Machine::SwitchOff",
			"Machine: switching off...",
			"Machine enters state: 'off'",
			"Machine exits state: 'off'",
			"Machine::SwitchOn",
			"Machine: switching on...",
			"Machine enters state: 'on'")),
	MONITORING(Arrays.asList("")),
	PRODUCER_CONSUMER(Arrays.asList("Consumer::Initialize",
			"Consumer::DoRequest",
			"Consumer::Stop",
			"Producer::Initialize",
			"Producer::DoOffer",
			"Producer::Stop",
			"Storage::Initialize",
			"Storage::CanAccept",
			"Storage::CannotAccept",
			"Storage::CanServe",
			"Storage::CannotServe")),
	TRAIN(Arrays.asList("train"));
	
	private List<String> expectedLines;
	
	DemoExpectedLines(List<String> expectedLines){
		this.expectedLines = expectedLines;
	}
	
	public List<String> getLines(){
		return expectedLines;
	}
}
