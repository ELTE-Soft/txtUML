#include "standard_functions.hpp"
#include <iostream>
#include <string>
#include <math.h>

int delayedInc(int i, int& out)
{
	out = i + 1;
	return i;
}

int delayedDec(int i, int& out)
{
	out = i - 1;
	return i;
}

namespace action 
{
	void log(std::string line)
	{
		std::cout << line << std::endl;
	}
}

namespace conversion
{
	std::string to_string(StateMachineI* sm)
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
	
	std::string to_string(std::string s)
	{
		return s;
	}
}

