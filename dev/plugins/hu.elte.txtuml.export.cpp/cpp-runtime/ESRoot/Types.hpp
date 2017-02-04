#ifndef TYPES_HPP
#define TYPES_HPP

#include <string>
#include <memory>

namespace ES
{
	//basic types
	using String = std::string;
	
	
	
	//ref types
	template<typename T>
	using Ref = std::shared_ptr<T>;
}

#endif