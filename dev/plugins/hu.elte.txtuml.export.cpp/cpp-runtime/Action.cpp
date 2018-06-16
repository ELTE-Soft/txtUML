#include "Action.hpp"
#include "ModelObject.hpp"
#include <iostream>
#include <sstream>

namespace Action 
{
	
	void log(ES::String message)
	{
		std::stringstream stream;
		stream << message << std::endl;
		std::cout << stream.str();
	}
}
