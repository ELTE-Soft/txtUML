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
			"ResourceMonitor::TCloseRead",
			"ResourceMonitor::TErrWrite",
			"Aggregator::TInit",
			"Aggregator::TPrintReport",
			"Aggregator::TWriteError",
			"Alert::TInitialize",
			"Alert::TIncreaseLevel",
			"Alert::TStartAlert",
			"Alert::TStopAlert",
			"!!! Critical number of errors detected !!!",
			"+++ Back to normal operation +++")),
	PRODUCER_CONSUMER(Arrays.asList("Consumer::Initialize",
			"Consumer::DoRequest",
			"Consumer::Stop",
			"Producer::Initialize",
			"Producer::DoOffer",
			"Producer::Stop",
			"Storage::Initialize",
			"Storage::CanAccept",
			"Storage::CanServe")),
	TRAIN(Arrays.asList("Engine::Init_Stopped",
			"Engine::Stopped_Working",
			"Engine::Working_Stopped",
			"Gearbox::Init_Neutral",
			"Gearbox::Neutral_Forwards",
			"Gearbox::Forwards_Neutral",
			"Lamp::Init_Dark",
			"Lamp::Dark_Light",
			"Lamp::Light_Dark",
			/*"Lamp::Light_Dark2", */ //it mostly true because of the sleep, but there is no guarantee for it
			"Forwards_subSM::FInit_F1"));
	
	private List<String> expectedLines;
	
	DemoExpectedLines(List<String> expectedLines){
		this.expectedLines = expectedLines;
	}
	
	public List<String> getLines(){
		return expectedLines;
	}
}
