#include "runtime.hpp"
#include <stdlib.h>

//********************************SingleThreadRT**********************************

SingleThreadRT* SingleThreadRT::instance = nullptr;

SingleThreadRT::SingleThreadRT():_messageQueue(new PoolQueueType){}

void SingleThreadRT::setupObjectSpecificRuntime(StateMachineI* sm){
	sm->setMessageCounter(new std::atomic_int(0));
}

bool SingleThreadRT::isConfigurated()
{
    return true;
}

SingleThreadRT* SingleThreadRT::createRuntime()
{
    if (instance == nullptr)
    {
        instance = new SingleThreadRT();
    }
    return instance;
}

void SingleThreadRT::start()
{

    while(!_messageQueue->empty())
    {
            StateMachineI* sm;
            _messageQueue->pop_front(sm);
            if (sm->isStarted())
            {
                if(sm->isInitialized())
                {
                    sm->processEventVirtual();
                }
                else
                {
                    sm->init();
					_messageQueue->push_back(sm);
                }

            }
    }

}

void SingleThreadRT::setConfiguration(ThreadConfiguration*){}

void SingleThreadRT::enqueObject(StateMachineI *sm)
{
    _messageQueue->push_back(sm);
}

void SingleThreadRT::stopUponCompletion() {}

void SingleThreadRT::removeObject(StateMachineI*) {}



//********************************ConfiguratedThreadedRT************************************

ConfiguratedThreadedRT* ConfiguratedThreadedRT::instance = nullptr;

ConfiguratedThreadedRT::ConfiguratedThreadedRT(): poolManager(new ThreadPoolManager()), worker(0), messages(0){}

ConfiguratedThreadedRT* ConfiguratedThreadedRT::createRuntime()
{
    if (instance == nullptr)
    {
        instance = new ConfiguratedThreadedRT();
    }
    return instance;
}

void ConfiguratedThreadedRT::start()
{
    if (poolManager->isConfigurated())
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
		abort();
	}
	

}

void ConfiguratedThreadedRT::stopUponCompletion()
{
	for(int i = 0; i < poolManager->getNumberOfConfigurations(); i++)
	{
		poolManager->getPool(i)->stopUponCompletion(&messages);
	}
}

void ConfiguratedThreadedRT::setupObjectSpecificRuntime(StateMachineI* sm)
{
	
	sm->setMessageCounter(&messages);
	int objectId = sm->getPoolId();
	StateMachineThreadPool* matchedPool = poolManager->getPool(objectId);
	sm->setPool(matchedPool);
	numberOfObjects[(size_t)objectId]++;
	poolManager->recalculateThreads(objectId,numberOfObjects[(size_t)objectId]);
}

bool ConfiguratedThreadedRT::isConfigurated()
{

    return poolManager->isConfigurated();
}

void ConfiguratedThreadedRT::removeObject(StateMachineI* sm)
{
	int objectId = sm->getPoolId();
	numberOfObjects[(size_t)objectId]--;
	poolManager->recalculateThreads(objectId,numberOfObjects[(size_t)objectId]);
	
}

void ConfiguratedThreadedRT::setConfiguration(ThreadConfiguration* conf)
{
    poolManager->setConfiguration(conf);
	int numberOfConfigurations = poolManager->getNumberOfConfigurations();
	numberOfObjects.clear();
	numberOfObjects.resize((unsigned int)numberOfConfigurations);
}

void ConfiguratedThreadedRT::enqueObject(StateMachineI*) {}

ConfiguratedThreadedRT::~ConfiguratedThreadedRT()
{
	delete poolManager;
}
