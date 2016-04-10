#include "runtime.hpp"

//********************************SingleThreadRT**********************************

SingleThreadRT* SingleThreadRT::instance = nullptr;

SingleThreadRT::SingleThreadRT():_messageQueue(new PoolQueueType){}

void SingleThreadRT::setupObjectSpecificRuntime(StateMachineI*){}

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

ConfiguratedThreadedRT::ConfiguratedThreadedRT(): poolManager(new ThreadPoolManager()){}

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

        int numberOfConfigurations = poolManager->getNumberOfConfigurations();
		
		numberOfObjects.clear();
                numberOfObjects.resize((unsigned int)numberOfConfigurations);

		for(int i = 0; i < numberOfConfigurations; i++)
		{
			poolManager->getPool(i)->startPool();
		}
	}
	else
	{
		//TODO sign error
	}
	

}

void ConfiguratedThreadedRT::stopUponCompletion()
{
	for(int i = 0; i < poolManager->getNumberOfConfigurations(); i++)
	{
		poolManager->getPool(i)->stopUponCompletion();
	}
}

void ConfiguratedThreadedRT::setupObjectSpecificRuntime(StateMachineI* sm)
{
	

	int objectID = sm->getPoolId();
	StateMachineThreadPool* matchedPool = poolManager->getPool(objectID);
	sm->setPool(matchedPool);
	numberOfObjects[(size_t)objectID]++;
	poolManager->recalculateThreads(objectID,numberOfObjects[(size_t)objectID]);
}

bool ConfiguratedThreadedRT::isConfigurated()
{

    return poolManager->isConfigurated();
}

void ConfiguratedThreadedRT::removeObject(StateMachineI* sm)
{
	int objectID = sm->getPoolId();
	numberOfObjects[(size_t)objectID]--;
	poolManager->recalculateThreads(objectID,numberOfObjects[(size_t)objectID]);
	
}

void ConfiguratedThreadedRT::setConfiguration(ThreadConfiguration* conf)
{
    poolManager->setConfiguration(conf);
}

void ConfiguratedThreadedRT::enqueObject(StateMachineI *sm) {}

ConfiguratedThreadedRT::~ConfiguratedThreadedRT()
{
	delete poolManager;
}
