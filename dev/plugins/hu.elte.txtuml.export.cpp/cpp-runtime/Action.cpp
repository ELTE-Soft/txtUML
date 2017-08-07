#include "Action.hpp"

#include <iostream>

namespace Action 
{
	/*void send(ES::StateMachineRef target, ES::EventRef signal)
	{
		target->send(signal);
	}*/
	
	void start(ES::StateMachineRef sm)
	{
		sm->startSM();
	}
	
	void log(ES::String message)
	{
		std::cout << message << std::endl;
	}
}
