#ifndef TYPES_HPP
#define TYPES_HPP

#include <string>
#include <memory>
#include <queue>
#include "Containers/PriorityQueue.hpp"

namespace Model
{
class IStateMachine;

}

namespace Model
{
template<typename BaseDerived>
class IEvent;
class EventBase;
template<typename BaseDerived>
class SpecialEventChecker;
}


namespace Execution 
{
template<typename RuntimeType>
class IRuntime;
}

namespace ES
{
class Timer;

template<typename T>
class ThreadSafeQueue;

template<typename T, typename Compare>
class SpecialPriorityQueue;

}

namespace Model
{
template <typename ProvidedInf, typename RequiredInf>
class Port;

template <typename ProvidedInf, typename RequiredInf>
class BehaviorPort;
}


namespace ES
{
	//basic types
	using String = std::string;

	//ref types
	template<typename T>
	using SharedPtr = std::shared_ptr<T>;

	using EventRef = SharedPtr<Model::IEvent<Model::EventBase>>;
	using EventConstRef = SharedPtr<const Model::IEvent<Model::EventBase>>;

	using StateMachineRef = Model::IStateMachine*;
	using StateMachineConstRef = Model::IStateMachine const *;

	template<typename RuntimeType>
	using RuntimePtr = SharedPtr<Execution::IRuntime<RuntimeType>>;

	//ThreadSafeQueue types
	using MessageQueueType = ThreadSafeQueue<SpecialPriorityQueue<EventRef, Model::SpecialEventChecker<Model::EventBase>>>;
	using PoolQueueType = ThreadSafeQueue<Queue<StateMachineRef>>;

	using TimerPtr = SharedPtr<Timer>;


}

#endif
