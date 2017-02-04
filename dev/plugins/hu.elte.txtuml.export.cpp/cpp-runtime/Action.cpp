#include "Action.hpp"


namespace Action 
{
	void send(IStateMachine * target, EventPtr signal)
	{
		target->send(signal);
	}
	
	void start(IStateMachine * sm)
	{
		sm->startSM();
	}
	
	void log(ES::String message)
	{
		std::cout << message << std::endl;
	}
}