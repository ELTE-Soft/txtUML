#ifndef TYPES_HPP
#define TYPES_HPP

#include <string>
#include <memory>

namespace Model
{
class IStateMachine;

}

namespace Model
{
template<typename BaseDerived>
class IEvent;
class EventBase;
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
}

namespace Model
{
template <typename RequiredInf, typename ProvidedInf>
class Port;

template <typename RequiredInf, typename ProvidedInf>
class BehaviorPort;
}


namespace ES
{
	//basic types
	using String = std::string;

	//ref types

	template<typename T>
	using Ptr = T*;

	template<typename T>
	using SharedPtr = std::shared_ptr<T>;

	using EventRef = SharedPtr<Model::IEvent<Model::EventBase>>;
	using EventConstRef = SharedPtr<const Model::IEvent<Model::EventBase>>;

	using StateMachineRef = Ptr<Model::IStateMachine>;
	using StateMachineConstRef = Ptr<const Model::IStateMachine>;

	template<typename RuntimeType>
	using RuntimePtr = SharedPtr<Execution::IRuntime<RuntimeType>>;

	//ThreadSafeQueue types
	using MessageQueueType = ThreadSafeQueue<EventRef>;
	using PoolQueueType = ThreadSafeQueue<StateMachineRef>;

	using TimerPtr = SharedPtr<Timer>;


}

#endif
