#include "Action.hpp"

#include <iostream>

namespace Action 
{
	void send(ES::ModelObject* target, ES::EventRef signal)
	{
		target->send(signal);
	}
	
	void start(ES::ModelObject* sm)
	{
		sm->start();
	}
	
	void log(ES::String message)
	{
		std::cout << message << std::endl;
	}
}
