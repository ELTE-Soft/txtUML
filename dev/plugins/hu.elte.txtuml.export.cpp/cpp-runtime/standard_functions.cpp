#include "standard_functions.hpp"
#include <iostream>
#include <string>
#include <math.h>

namespace conversion
{
	std::string to_string(IStateMachine* sm)
	{
		return sm->toString();
	}
	std::string to_string(int i)
	{
		return std::to_string(i);
	}
	std::string to_string(double d)
	{
		return std::to_string(d);
	}
	std::string to_string(bool b)
	{
		return b ? "True" : "False";
	}

	std::string to_string(const char* s)
	{
		return std::string(s);
	}
	
	std::string to_string(std::string s)
	{
		return s;
	}
}

