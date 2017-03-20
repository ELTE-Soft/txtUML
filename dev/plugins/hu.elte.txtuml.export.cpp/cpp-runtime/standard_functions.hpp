#ifndef STANDARD_FUNCTIONS
#define STANDARD_FUNCTIONS

#include <string>
#include <sstream>
#include <list>
#include <math.h>
#include "istatemachine.hpp"

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

template<typename E>
int count(std::list<E*> elements)
{
	return (int)elements.size();
}

template<typename T1, typename T2>
std::string concat(T1 s1, T2 s2)
{
    std::ostringstream stream;
    stream << s1 << s2;
    return stream.str();
}



namespace conversion 
{
std::string to_string(IStateMachine*);
std::string to_string(int);
std::string to_string(double);
std::string to_string(bool);
std::string to_string(std::string);
std::string to_string(const char*);	
}



#endif
