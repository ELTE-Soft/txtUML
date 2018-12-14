#ifndef TYPES_HPP
#define TYPES_HPP

#include <string>
#include <memory>
#include <queue>
#include "Containers/PriorityQueue.hpp"

namespace Model
{
class StateMachineOwner;

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

class StateMachineThreadPool;
using ThreadPoolPtr = std::shared_ptr<Execution::StateMachineThreadPool>;

}

namespace ES
{
class Timer;

template<typename T>
class ThreadSafeQueue;

template<typename T, typename Compare>
class SpecialPriorityQueue;

class ModelObject;

}

namespace Model
{

template<typename ProvidedInf, typename RequiredInf>
class IPort;

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

	using StateMachineRef = Model::StateMachineOwner*;
	using StateMachineConstRef = Model::StateMachineOwner const *;
	using ModelObjectRef = ES::ModelObject*;

    template<typename ProvidedInf, typename RequiredInf>
    using IPortRef = ES::SharedPtr<Model::IPort<ProvidedInf,RequiredInf>>;

    template<typename ProvidedInf, typename RequiredInf>
    using PortRef = ES::SharedPtr<Model::Port<ProvidedInf,RequiredInf>>;

	template<typename RuntimeType>
	using RuntimePtr = SharedPtr<Execution::IRuntime<RuntimeType>>;

	//ThreadSafeQueue types
	using MessageQueueType = ThreadSafeQueue<SpecialPriorityQueue<EventRef, Model::SpecialEventChecker<Model::EventBase>>>;
	//using PoolQueueType = ThreadSafeQueue<Queue<StateMachineRef>>;

	using TimerPtr = SharedPtr<Timer>;


}

namespace Model 
{
	struct IConnection;
	using ConnectionPtr = ES::SharedPtr<Model::IConnection>;
	
	class PortType {
	public:
		static int portIdCounter;
		static PortType AnyPort;

	public:
		PortType () : portTypeId (portIdCounter++) {}
		PortType (const PortType& o) = default;
		PortType& operator=(const PortType& o) = default;

		int getPortTypeId () const { return portTypeId; }
		bool operator==(const PortType& o) const { return portTypeId == o.getPortTypeId (); }
		bool operator!=(const PortType& o) const { return !(portTypeId == o.getPortTypeId ()); }

	private:
		int portTypeId;

	};

}

#endif
