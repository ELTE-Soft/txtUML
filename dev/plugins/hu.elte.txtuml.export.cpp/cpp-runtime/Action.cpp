#include "Action.hpp"
#include "ModelObject.hpp"
#include <iostream>
#include <sstream>

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

	void deleteObject(ES::ModelObject * modelObject)
	{
		modelObject->deleteObject();
	}
	
	void log(ES::String message)
	{
		std::stringstream stream;
		stream << message << std::endl;
		std::cout << stream.str();
	}
}
