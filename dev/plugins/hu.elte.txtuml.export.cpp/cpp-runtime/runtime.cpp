#include "runtime.hpp"

//********************************RuntimeI********************************

RuntimeI::RuntimeI():_stop(false) {}

void RuntimeI::setupObject(ObjectList& ol_)
{
  for(auto it=ol_.begin();it!=ol_.end();++it)
  {
    (*it)->setRuntime(this);
    setupObjectVirtual(*it);
  }
}

void RuntimeI::setupObject(StateMachineI* sm_)
{
  setupObjectVirtual(sm_);
}

void RuntimeI::stop()
{
  std::unique_lock<std::mutex> mlock(_mutex);
  _stop=true;
  mlock.unlock();
  _cond.notify_one();
}

//********************************SingleThreadRT**********************************

SingleThreadRT::SingleThreadRT():_messageQueue(new MessageQueueType), waiting(false){}

void SingleThreadRT::run()
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

void SingleThreadRT::stopUponCompletion()
{
	std::unique_lock<std::mutex> mlock(_mutex);
	waiting = true;
	waiting_empty_cond.wait(mlock);
	mlock.unlock();
	stop();
}

//********************************ConfiguratedThreadedRT************************************

 
ConfiguratedThreadedRT::ConfiguratedThreadedRT(): poolManager(new ThreadPoolManager())
{
		
}

void ConfiguratedThreadedRT::run()
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

void ConfiguratedThreadedRT::setupObjectVirtual(StateMachineI* sm)
{
	
	int objectID = sm->getPoolId();
	StateMachineThreadPool* matchedPool = poolManager->getPool(objectID);
	sm->setPool(matchedPool);
	numberOfObjects[objectID]++;
	poolManager->recalculateThreads(objectID,numberOfObjects[objectID]);
	
}

void ConfiguratedThreadedRT::removeObject(StateMachineI* sm)
{
	sm->setRuntime(nullptr);
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
