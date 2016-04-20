#include "standard_functions.hpp"
#include <iostream>

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

