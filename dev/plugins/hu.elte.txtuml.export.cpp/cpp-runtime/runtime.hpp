/** @file runtime.hpp
*/

#ifndef RUNTIME_HPP_INCLUDED
#define RUNTIME_HPP_INCLUDED

#include <thread>
#include <atomic>
#include <mutex>
#include <condition_variable>
#include <array>
#include <assert.h>
#include <stdlib.h>

#include "StateMachineOwner.hpp"
#include "threadpool.hpp"
#include "ievent.hpp"
#include "threadpoolmanager.hpp"
#include "ESRoot/Types.hpp"
#include "ESRoot/AtomicCounter.hpp"

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
		std::call_once (runtimeInstanceInitFlag, initRuntime);
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
	Sets the deployment configuration for the threaded runtime instance.
	*/
	void configure(const std::vector<Configuration>& conf)
	{
		if (!(static_cast<RuntimeType*>(this)->isConfigurated()))
		{
			static_cast<RuntimeType*>(this)->setConfiguration(conf);
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
	IRuntime () {}

private:
	static void initRuntime ()
	{
		instance = RuntimeType::createRuntime ();

	}
	static ES::RuntimePtr<RuntimeType> instance;
	static std::once_flag				   runtimeInstanceInitFlag;
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
	void setConfiguration(const std::vector<Configuration>& conf);
	void stopUponCompletion();
	static ES::RuntimePtr<SingleThreadRT> createRuntime () { return ES::RuntimePtr<SingleThreadRT> (new SingleThreadRT ()); }
private:
	// SingleThreadRT
	SingleThreadRT() :_messageQueue(new ES::MessageQueueType()) {}
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
	void setConfiguration(const std::vector<Configuration>& conf);
	void stopUponCompletion();
	static ES::RuntimePtr<ConfiguredThreadedRT> createRuntime () { return ES::RuntimePtr<ConfiguredThreadedRT> (new ConfiguredThreadedRT ()); }
private:
	ConfiguredThreadedRT ();
	std::vector<Configuration> configurations;

	ES::SharedPtr<ES::AtomicCounter> worker;
	ES::SharedPtr<ES::AtomicCounter> messages;
	std::condition_variable stop_request_cond;
};




template<typename RuntimeType>
ES::RuntimePtr<RuntimeType> IRuntime<RuntimeType>::instance = nullptr;

template<typename RuntimeType>
std::once_flag				 IRuntime<RuntimeType>::runtimeInstanceInitFlag;


}


#endif // RUNTIME_HPP_INCLUDED
