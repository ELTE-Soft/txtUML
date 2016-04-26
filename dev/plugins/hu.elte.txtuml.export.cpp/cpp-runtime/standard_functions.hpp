#ifndef STANDARD_FUNCTIONS
#define STANDARD_FUNCTIONS

#include <string>
#include <list>
int delayedInc(int,int&);
int delayedDec(int,int&);

namespace action 
{
	void log(std::string);
}

template<typename E>
E* select(std::list<E*> elements)
{
	return elements.front();
}


#endif