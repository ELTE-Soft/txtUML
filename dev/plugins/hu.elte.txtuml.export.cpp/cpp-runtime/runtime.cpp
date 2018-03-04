#include "runtime.hpp"
#include "StateMachineOwner.hpp"
#include "ESRoot/Types.hpp"
#include "ievent.hpp"

#include <assert.h>
#include <stdlib.h>


//********************************SingleThreadRT**********************************

namespace Execution {

template<>
ES::RuntimePtr<SingleThreadRT> IRuntime<SingleThreadRT>::instance = nullptr;

SingleThreadRT::SingleThreadRT() :_messageQueue(new ES::MessageQueueType()) {}

void SingleThreadRT::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{
	sm->setMessageQueue(_messageQueue);
	sm->setMessageCounter(ES::SharedPtr<ES::AtomicCounter>(new ES::AtomicCounter()));
}

SingleThreadRT::~SingleThreadRT()
{
}

bool SingleThreadRT::isConfigurated()
{
	return true;
}

void SingleThreadRT::start()
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

void SingleThreadRT::setConfiguration(ESContainer::FixedArray<ES::SharedPtr<Configuration>>) {}

void SingleThreadRT::stopUponCompletion() {}

void SingleThreadRT::removeObject(ES::StateMachineRef) {}



//********************************ConfiguratedThreadedRT************************************

template<>
ES::RuntimePtr<ConfiguredThreadedRT> IRuntime<ConfiguredThreadedRT>::instance = nullptr;

ConfiguredThreadedRT::ConfiguredThreadedRT() : 
	poolManager(new ThreadPoolManager()), 
	worker(new ES::AtomicCounter()),
	messages(new ES::AtomicCounter()) {}

ConfiguredThreadedRT::~ConfiguredThreadedRT() {}

void ConfiguredThreadedRT::start()
{
	assert(isConfigurated() && "The configurated threaded runtime should be configured before starting.");

	if (isConfigurated())
	{
		for (int i = 0; i < poolManager->getNumberOfConfigurations(); i++)
		{
			ES::SharedPtr<StateMachineThreadPool> pool = poolManager->getPool(i);
			pool->setWorkersCounter(worker);
			pool->setMessageCounter(messages);
			pool->setStopReqest(&stop_request_cond);
			pool->startPool(poolManager->calculateNOfThreads(i, numberOfObjects[i]));
		}
	}
}

void ConfiguredThreadedRT::removeObject(ES::StateMachineRef sm)
{
	int objectId = sm->getPoolId();
	numberOfObjects[objectId]--;
	poolManager->recalculateThreads(objectId, numberOfObjects[objectId]);
}

void ConfiguredThreadedRT::stopUponCompletion()
{
	for (int i = 0; i < poolManager->getNumberOfConfigurations(); i++)
	{
		poolManager->getPool(i)->stopUponCompletion();
	}
}

void ConfiguredThreadedRT::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{

	sm->setMessageCounter(messages);
	int objectId = sm->getPoolId();
	ES::SharedPtr<StateMachineThreadPool> matchedPool = poolManager->getPool(objectId);
	sm->setPool(matchedPool);
	numberOfObjects[objectId]++;
	poolManager->recalculateThreads(objectId, numberOfObjects[objectId]);
}

bool ConfiguredThreadedRT::isConfigurated()
{
	return poolManager->isConfigurated();
}

void ConfiguredThreadedRT::setConfiguration(ESContainer::FixedArray<ES::SharedPtr<Configuration>> conf)
{
	poolManager->setConfiguration(conf);
	int numberOfConfigurations = poolManager->getNumberOfConfigurations();
	numberOfObjects = ESContainer::FixedArray<int>(numberOfConfigurations, 0);
}

}


// Constans
Model::PortType Model::PortType::AnyPort = Model::PortType(1);

