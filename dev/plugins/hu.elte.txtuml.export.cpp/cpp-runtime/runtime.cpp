#include "runtime.hpp"
#include "runtimetypes.hpp"

#include <assert.h>
#include <stdlib.h>


template<typename RuntimeType>
ES::Ref<IRuntime<RuntimeType>> IRuntime<RuntimeType>::instance = nullptr;

//********************************SingleThreadRT**********************************

SingleThreadRT::SingleThreadRT():_messageQueue(new MessageQueueType()) {}

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

    while(!_messageQueue->empty())
    {
		EventPtr e;
		_messageQueue->pop_front(e);
		ES::StateMachineRef sm = e->getTargetSM();
		if(sm->isStarted())
		{
			sm->processEventVirtual();
		}
    }

}

void SingleThreadRT::setConfiguration(ES::Ref<ThreadConfiguration>){}

void SingleThreadRT::stopUponCompletion() {}

void SingleThreadRT::removeObject(ES::StateMachineRef) {}



//********************************ConfiguratedThreadedRT************************************

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
	numberOfObjects[(size_t) objectId]--;
	poolManager->recalculateThreads(objectId, numberOfObjects[(size_t) objectId]);
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
	StateMachineThreadPool* matchedPool = poolManager->getPool(objectId);
	sm->setPool(matchedPool);
	numberOfObjects[(size_t) objectId]++;
	poolManager->recalculateThreads(objectId,numberOfObjects[(size_t) objectId]);
}

bool ConfiguratedThreadedRT::isConfigurated()
{
    return poolManager->isConfigurated();
}

void ConfiguratedThreadedRT::setConfiguration(ES::Ref<ThreadConfiguration> conf)
{
    poolManager->setConfiguration(conf);
	int numberOfConfigurations = poolManager->getNumberOfConfigurations();
	numberOfObjects.clear();
	numberOfObjects.resize((unsigned int)numberOfConfigurations);
}
