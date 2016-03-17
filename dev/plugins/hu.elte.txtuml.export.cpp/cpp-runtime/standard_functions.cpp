#include "standard_functions.hpp"

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
