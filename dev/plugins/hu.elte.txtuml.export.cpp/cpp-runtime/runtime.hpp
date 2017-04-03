/** @file runtime.hpp
*/

#ifndef RUNTIME_HPP_INCLUDED
#define RUNTIME_HPP_INCLUDED

#include <thread>
#include <atomic>
#include <mutex>
#include <condition_variable>

#include "istatemachine.hpp"
#include "threadpool.hpp"
#include "ievent.hpp"
#include "threadpoolmanager.hpp"
#include "ESRoot/Types.hpp"
#include "ESRoot/Containers/FixedArray.hpp"

namespace Execution 
{

template<typename RuntimeType>
class IRuntime
{
public:

	/*!
	Returns the runtime instance during the model execution.
	*/
	static ES::RuntimePtr<RuntimeType> getRuntimeInstance()
	{
		if (instance == nullptr)
		{
			instance = RuntimeType::createRuntime();
		}
		return instance;
	}

	/*!
	Registers a state machine in the runtime instance so
	the threaded runtime can record the number of object instances during the model execution.
	Called by the state machine constructor.
	*/
	void setupObject(ES::StateMachineRef sm)
	{
		static_cast<RuntimeType*>(this)->setupObjectSpecificRuntime(sm);
	}


	/*!
	Removes a state machine from the runtime instance when
	the threaded runtime can record the number of object instances during the model execution.
	Called by the state machine destructor.
	*/
	void removeObject(ES::StateMachineRef sm)
	{
		static_cast<RuntimeType*>(this)->removeObject(sm);
	}


	/*!
	Sets the deployment configuration for the threaded runtime instance.
	*/
	void configure(ESContainer::FixedArray<ES::SharedPtr<Configuration>> configuration)
	{
		if (!(static_cast<RuntimeType*>(this)->isConfigurated()))
		{
			static_cast<RuntimeType*>(this)->setConfiguration(configuration);
		}

	}

	/*!
	Starts the model execution.
	*/
	void startRT()
	{
		static_cast<RuntimeType*>(this)->start();
	}


	/*!
	Stops the runtime instance when there are no more messages to process and under processing.
	*/
	void stopUponCompletion()
	{
		static_cast<RuntimeType*>(this)->stopUponCompletion();
	}


protected:
	static ES::RuntimePtr<RuntimeType> instance;
	IRuntime() {}
};

class SingleThreadRT : public IRuntime<SingleThreadRT>
{
public:
	virtual ~SingleThreadRT();

	/*!
	Processes the events while the message queue is not empty.
	*/
	void start();

	void setupObjectSpecificRuntime(ES::StateMachineRef);
	void removeObject(ES::StateMachineRef);
	void setConfiguration(ESContainer::FixedArray<ES::SharedPtr<Configuration>>);
	bool isConfigurated();
	void stopUponCompletion();
	static ES::RuntimePtr<SingleThreadRT> createRuntime() { return ES::RuntimePtr<SingleThreadRT>(new SingleThreadRT()); }
private:
	SingleThreadRT();
	ES::SharedPtr<ES::MessageQueueType> _messageQueue;

};

class ConfiguredThreadedRT : public IRuntime<ConfiguredThreadedRT>
{
public:
	virtual ~ConfiguredThreadedRT();
	/*!
	Starts the thread pools.
	*/
	void start();

	void setupObjectSpecificRuntime(ES::StateMachineRef);
	void removeObject(ES::StateMachineRef);
	void setConfiguration(ESContainer::FixedArray<ES::SharedPtr<Configuration>>);
	bool isConfigurated();
	void stopUponCompletion();
	static ES::RuntimePtr<ConfiguredThreadedRT> createRuntime() { return ES::RuntimePtr<ConfiguredThreadedRT>(new ConfiguredThreadedRT()); }
private:
	ConfiguredThreadedRT();
	ES::SharedPtr<ThreadPoolManager> poolManager;
	ESContainer::FixedArray<int> numberOfObjects;

	std::atomic_int worker;
	std::atomic_int messages;
	std::condition_variable stop_request_cond;
};

}


#endif // RUNTIME_HPP_INCLUDED
