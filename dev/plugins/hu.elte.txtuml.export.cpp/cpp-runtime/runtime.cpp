#include "runtime.hpp"
#include "istatemachine.hpp"
#include "ESRoot/Types.hpp"

#include <assert.h>
#include <stdlib.h>


//********************************SingleThreadRT**********************************

template<>
ES::RuntimePtr<SingleThreadRT> IRuntime<SingleThreadRT>::instance = nullptr;

SingleThreadRT::SingleThreadRT():_messageQueue(new ES::MessageQueueType()) {}

void SingleThreadRT::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{
	sm->setMessageCounter(new std::atomic_int(0));
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

    while(!_messageQueue->isEmpty())
    {
		ES::EventRef e = _messageQueue->next();
		const ES::StateMachineRef sm = e->getTargetSM();
		if(sm->isStarted())
		{
			sm->processEventVirtual();
		}
    }

}

void SingleThreadRT::setConfiguration(ESContainer::FixedArray<ES::SharedPtr<Configuration>>){}

void SingleThreadRT::stopUponCompletion() {}

void SingleThreadRT::removeObject(ES::StateMachineRef) {}



//********************************ConfiguratedThreadedRT************************************

template<>
ES::RuntimePtr<ConfiguratedThreadedRT> IRuntime<ConfiguratedThreadedRT>::instance = nullptr;

ConfiguratedThreadedRT::ConfiguratedThreadedRT(): poolManager(new ThreadPoolManager()), worker(0), messages(0){}

ConfiguratedThreadedRT::~ConfiguratedThreadedRT() {}

void ConfiguratedThreadedRT::start()
{
    if (isConfigurated())
    {
		for(int i = 0; i < poolManager->getNumberOfConfigurations(); i++)
		{
			poolManager->getPool(i)->setWorkersCounter(&worker);
			poolManager->getPool(i)->setStopReqest(&stop_request_cond);
			poolManager->getPool(i)->startPool(poolManager->calculateNOfThreads(i,numberOfObjects[(size_t)i]));
		}
	}
	else
	{
		assert("The configurated threaded runtime should be configured before starting.");
	}
}

void ConfiguratedThreadedRT::removeObject(ES::StateMachineRef sm)
{
	int objectId = sm->getPoolId();
	numberOfObjects[objectId]--;
	poolManager->recalculateThreads(objectId, numberOfObjects[objectId]);
}

void ConfiguratedThreadedRT::stopUponCompletion()
{
	for(int i = 0; i < poolManager->getNumberOfConfigurations(); i++)
	{
		poolManager->getPool(i)->stopUponCompletion(&messages);
	}
}

void ConfiguratedThreadedRT::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{
	
	sm->setMessageCounter(&messages);
	int objectId = sm->getPoolId();
	ES::SharedPtr<StateMachineThreadPool> matchedPool = poolManager->getPool(objectId);
	sm->setPool(matchedPool);
	numberOfObjects[(size_t) objectId]++;
	poolManager->recalculateThreads(objectId,numberOfObjects[(size_t) objectId]);
}

bool ConfiguratedThreadedRT::isConfigurated()
{
    return poolManager->isConfigurated();
}

void ConfiguratedThreadedRT::setConfiguration(ESContainer::FixedArray<ES::SharedPtr<Configuration>> conf)
{
    poolManager->setConfiguration(conf);
	int numberOfConfigurations = poolManager->getNumberOfConfigurations();
	numberOfObjects = ESContainer::FixedArray<int>(numberOfConfigurations,0);
}
