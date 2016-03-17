#include "standard_functions.hpp"

int delayedInc(int i, int& out)
{
	out = out + 1;
	return i;
}
