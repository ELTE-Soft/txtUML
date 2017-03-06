#include "Types.hpp"
#include <sstream>

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
