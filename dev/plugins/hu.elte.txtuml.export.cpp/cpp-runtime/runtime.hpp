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

template<typename RuntimeType, int NC>
class IRuntime
{
public:

	/*!
	Returns the runtime instance during the model execution.
	*/
	static ES::RuntimePtr<RuntimeType,NC> getRuntimeInstance()
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
	void configure(std::array<ES::SharedPtr<Configuration>, NC> configuration)
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
	static ES::RuntimePtr<RuntimeType,NC> instance;
	IRuntime() {}
};

template<int NC>
class SingleThreadRT : public IRuntime<SingleThreadRT<NC>, NC>
{
public:
	virtual ~SingleThreadRT();

	/*!
	Processes the events while the message queue is not empty.
	*/
	void start();

	void setupObjectSpecificRuntime(ES::StateMachineRef);
	void removeObject(ES::StateMachineRef);
	void setConfiguration(std::array<ES::SharedPtr<Configuration>,NC>);
	bool isConfigurated();
	void stopUponCompletion();
	static ES::RuntimePtr<SingleThreadRT,NC> createRuntime() { return ES::RuntimePtr<SingleThreadRT<NC>,NC>(new SingleThreadRT<NC>()); }
private:
	SingleThreadRT();
	ES::SharedPtr<ES::MessageQueueType> _messageQueue;

};

template<int NC>
class ConfiguredThreadedRT : public IRuntime<ConfiguredThreadedRT<NC>,NC>
{
public:
	virtual ~ConfiguredThreadedRT();
	/*!
	Starts the thread pools.
	*/
	void start();

	void setupObjectSpecificRuntime(ES::StateMachineRef);
	void removeObject(ES::StateMachineRef);
	void setConfiguration(std::array<ES::SharedPtr<Configuration>, NC>);
	bool isConfigurated();
	void stopUponCompletion();
	static ES::RuntimePtr<ConfiguredThreadedRT,NC> createRuntime() { return ES::RuntimePtr<ConfiguredThreadedRT<NC>,NC>(new ConfiguredThreadedRT<NC>()); }
private:
	ConfiguredThreadedRT();
	ES::SharedPtr<ThreadPoolManager<NC>> poolManager;
	std::array<int, NC> numberOfObjects;

	ES::SharedPtr<ES::AtomicCounter> worker;
	ES::SharedPtr<ES::AtomicCounter> messages;
	std::condition_variable stop_request_cond;
};




template<typename RuntimeType, int NC>
ES::RuntimePtr<RuntimeType,NC> IRuntime<RuntimeType, NC>::instance = nullptr;


// SingleThreadRT
template<int NC>
SingleThreadRT<NC>::SingleThreadRT() :_messageQueue(new ES::MessageQueueType()) {}

template<int NC>
void SingleThreadRT<NC>::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{
	sm->setMessageQueue(_messageQueue);
	sm->setMessageCounter(ES::SharedPtr<ES::AtomicCounter>(new ES::AtomicCounter()));
}

template<int NC>
SingleThreadRT<NC>::~SingleThreadRT()
{
}

template<int NC>
bool SingleThreadRT<NC>::isConfigurated()
{
	return true;
}

template<int NC>
void SingleThreadRT<NC>::start()
{

	while (!_messageQueue->isEmpty())
	{
		ES::EventRef e = _messageQueue->next();
		if (Model::IEvent<Model::EventBase>::eventIsValid(e)) {
			const ES::StateMachineRef sm = e->getTargetSM();
			sm->processNextEvent();
		}
		else {
			_messageQueue->dequeue(e); // drop event
		}


	}

}

template<int NC>
void SingleThreadRT<NC>::setConfiguration(std::array<ES::SharedPtr<Configuration>, NC>) {}

template<int NC>
void SingleThreadRT<NC>::stopUponCompletion() {}

template<int NC>
void SingleThreadRT<NC>::removeObject(ES::StateMachineRef) {}

// ConfiguredThreadedRT
template<int NC>
ConfiguredThreadedRT<NC>::ConfiguredThreadedRT () :
	poolManager (new ThreadPoolManager<NC> ()),
	worker (new ES::AtomicCounter ()),
	messages (new ES::AtomicCounter ()) {}

template<int NC>
ConfiguredThreadedRT<NC>::~ConfiguredThreadedRT () {}

template<int NC>
void ConfiguredThreadedRT<NC>::start ()
{
	assert (isConfigurated () && "The configurated threaded runtime should be configured before starting.");

	if (isConfigurated ())
	{
		for (int i = 0; i < poolManager->getNumberOfConfigurations (); i++)
		{
			ES::SharedPtr<StateMachineThreadPool> pool = poolManager->getPool (i);
			pool->setWorkersCounter (worker);
			pool->setMessageCounter (messages);
			pool->setStopReqest (&stop_request_cond);
			pool->startPool (poolManager->calculateNOfThreads (i, numberOfObjects[i]));
		}
	}
}

template<int NC>
void ConfiguredThreadedRT<NC>::removeObject (ES::StateMachineRef sm)
{
	int objectId = sm->getPoolId ();
	numberOfObjects[objectId]--;
	poolManager->recalculateThreads (objectId, numberOfObjects[objectId]);
}

template<int NC>
void ConfiguredThreadedRT<NC>::stopUponCompletion ()
{
	for (int i = 0; i < poolManager->getNumberOfConfigurations (); i++)
	{
		poolManager->getPool (i)->stopUponCompletion ();
	}
}

template<int NC>
void ConfiguredThreadedRT<NC>::setupObjectSpecificRuntime (ES::StateMachineRef sm)
{

	sm->setMessageCounter (messages);
	int objectId = sm->getPoolId ();
	ES::SharedPtr<StateMachineThreadPool> matchedPool = poolManager->getPool (objectId);
	sm->setPool (matchedPool);
	numberOfObjects[objectId]++;
	poolManager->recalculateThreads (objectId, numberOfObjects[objectId]);
}

template<int NC>
bool ConfiguredThreadedRT<NC>::isConfigurated ()
{
	return poolManager->isConfigurated ();
}

template<int NC>
void ConfiguredThreadedRT<NC>::setConfiguration (std::array<ES::SharedPtr<Configuration>, NC> conf)
{
	poolManager->setConfiguration (conf);
	for (unsigned i = 0; i < numberOfObjects.size (); ++i) {
		numberOfObjects[i] = 0;
	}
}

}


#endif // RUNTIME_HPP_INCLUDED
