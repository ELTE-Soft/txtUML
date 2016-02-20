#include "runtime.hpp"

//********************************SingleThreadRT**********************************

SingleThreadRT* SingleThreadRT::instance = nullptr;

SingleThreadRT::SingleThreadRT():_messageQueue(new MessageQueueType), waiting(false){}

void SingleThreadRT::setupObjectSpecificRuntime(StateMachineI *sm)
{
    sm->setMessageQueue(_messageQueue);
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

    while(!_stop)
    {

        if(!_messageQueue->empty())
        {

            if (_messageQueue->front()->dest.isStarted() && _messageQueue->front()->dest.isInitialized())
		 {
			_messageQueue->front()->dest.processEventVirtual();			
		 }
		 else if (_messageQueue->front()->dest.isStarted() && !_messageQueue->front()->dest.isInitialized()) 
		 {
			 _messageQueue->front()->dest.init();
		 }
      
    }
    else if(_messageQueue->empty() && waiting)
    {
    	waiting_empty_cond.notify_one();
    }

  }

}

void SingleThreadRT::setConfiguration(ThreadConfiguration *conf){}

void SingleThreadRT::stopUponCompletion()
{
	std::unique_lock<std::mutex> mlock(_mutex);
	waiting = true;
	waiting_empty_cond.wait(mlock);
	mlock.unlock();
	stop();
}



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
		numberOfObjects.resize(numberOfConfigurations);
		
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
	numberOfObjects[objectID]++;
	poolManager->recalculateThreads(objectID,numberOfObjects[objectID]);

}

bool ConfiguratedThreadedRT::isConfigurated()
{
    return poolManager->isConfigurated();
}

void ConfiguratedThreadedRT::removeObject(StateMachineI* sm)
{
	int objectID = sm->getPoolId();
	numberOfObjects[objectID]--;
	poolManager->recalculateThreads(objectID,numberOfObjects[objectID]);
	
}

void ConfiguratedThreadedRT::setConfiguration(ThreadConfiguration* conf)
{
	poolManager->setConfiguration(conf);
}

ConfiguratedThreadedRT::~ConfiguratedThreadedRT()
{
	delete poolManager;
}
