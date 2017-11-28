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
	MONITORING(Arrays.asList("ResourceMonitor::TInit",
			"ResourceMonitor::TOpenRead",
			"ResourceMonitor::TOpenWrite",
			"ResourceMonitor::TReadAgain",
			"ResourceMonitor::TWriteAgain",
			"ResourceMonitor::TCloseRead",
			"ResourceMonitor::TCloseWrite",
			"ResourceMonitor::TErrRead",
			"ResourceMonitor::TErrWrite",
			"Aggregator::TInit",
			"Aggregator::TPrintReport",
			"Aggregator::TWriteError",
			"Aggregator::TReadError",
			"Alert::TInitialize",
			"Alert::TIncreaseLevel",
			"Alert::TStartAlert",
			"Alert::TStopAlert",
			"!!! Critical number of errors detected !!!",
			"+++ Back to normal operation +++",
			"# read errors: 0, # write errors: 0")),
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
	TRAIN(Arrays.asList("Engine::Init_Stopped",
			"Engine::Stopped_Working",
			"Engine::Working_Stopped",
			"Gearbox::Init_Neutral",
			"Gearbox::Neutral_Forwards",
			"Gearbox::Neutral_Backwards",
			"Gearbox::Forwards_Neutral",
			"Gearbox::Backwards_Neutral",
			"Lamp::Init_Dark",
			"Lamp::Dark_Light",
			"Lamp::Light_Dark",
			"Lamp::Light_Dark2",
			"Backwards_subSM::BInit_B1",
			"Backwards_subSM::B1_B2",
			"Backwards_subSM::B2_B1",
			"Forwards_subSM::FInit_F1",
			"Forwards_subSM::F1_F2",
			"Forwards_subSM::F2_F1"));
	
	private List<String> expectedLines;
	
	DemoExpectedLines(List<String> expectedLines){
		this.expectedLines = expectedLines;
	}
	
	public List<String> getLines(){
		return expectedLines;
	}
}
