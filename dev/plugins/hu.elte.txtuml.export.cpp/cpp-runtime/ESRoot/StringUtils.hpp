#include "Types.hpp"
#include <sstream>

#ifndef STRING_UTILS_HPP
#define STRING_UTILS_HPP

namespace StringUtils
{
	template<typename T>
	ES::String toString(T s)
	{
		std::stringstream stream;
		stream << s;
		return stream.str();
	}
}

#endif