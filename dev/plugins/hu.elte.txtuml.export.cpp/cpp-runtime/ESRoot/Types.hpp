#ifndef TYPES_HPP
#define TYPES_HPP

#include <string>
#include <memory>
#include "Containers/threadsafequeue.hpp"

class IStateMachine;

template<typename BaseDerived>
class IEvent;
class EventBase;

template<typename RuntimeType>
class IRuntime;

class Timer;

namespace ES
{
	//basic types
	using String = std::string;

	//ref types

	template<typename T>
	using Ptr = T*;

	template<typename T>
	using SharedPtr = std::shared_ptr<T>;

	using EventRef = SharedPtr<IEvent<EventBase>>;
	using EventConstRef = SharedPtr<const IEvent<EventBase>>;

	using StateMachineRef = Ptr<IStateMachine>;
	using StateMachineConstRef = Ptr<const IStateMachine>;

	template<typename RuntimeType>
	using RuntimePtr = SharedPtr<IRuntime<RuntimeType>>;

	//ThreadSafeQueue types
	using MessageQueueType = ThreadSafeQueue<EventRef>;
	using PoolQueueType = ThreadSafeQueue<StateMachineRef>;

	using TimerPtr = SharedPtr<Timer>;


}

#endif
