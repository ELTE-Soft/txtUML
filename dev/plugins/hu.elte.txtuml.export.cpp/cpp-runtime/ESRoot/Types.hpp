#ifndef TYPES_HPP
#define TYPES_HPP

#include <string>
#include <memory>

class IStateMachine;
class IEvent;

namespace ES
{
	//basic types
	using String = std::string;	
	
	//ref types
	template<typename T>
	using Ref = std::shared_ptr<T>;
	
	using StateMachineRef = Ref<IStateMachine>;
	using StateMachineConstRef = const Ref<IStateMachine>;
	
	using EventRef = Ref<IEvent>;
}

#endif