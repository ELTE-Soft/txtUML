#ifndef STANDARD_FUNCTIONS
#define STANDARD_FUNCTIONS

#include <string>

int delayedInc(int,int&);
int delayedDec(int,int&);

namespace action 
{
	void log(std::string);
}

#endif