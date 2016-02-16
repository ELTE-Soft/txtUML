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

void RuntimeI::startObject(ObjectList& ol_)
{
  for(auto it=ol_.begin();it!=ol_.end();++it)
  {
      startObject(*it);
  }
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

//********************************ConfiguredThreadPoolsRT************************************

 
ConfiguredThreadPoolsRT::ConfiguredThreadPoolsRT(): pool_manager(new ThreadPoolManager())
{
		
}

void ConfiguredThreadPoolsRT::run()
{
	if (pool_manager->isConfigurated())
	{
		
		int numberOfConfigurations = pool_manager->getNumberOfConfigurations();
		
		number_of_objects.clear();
		number_of_objects.resize(numberOfConfigurations);
		
		for(int i = 0; i < numberOfConfigurations; i++)
		{
			pool_manager->getPool(i)->startPool();
		}
	}
	else
	{
		//TODO sign error
	}
	

}

void ConfiguredThreadPoolsRT::stopUponCompletion()
{
	for(int i = 0; i < pool_manager->getNumberOfConfigurations(); i++)
	{
		pool_manager->getPool(i)->stopUponCompletion();
	}
}

void ConfiguredThreadPoolsRT::setupObjectVirtual(StateMachineI* sm_)
{
	
	int objectID = sm_->getPoolId();
	
	StateMachineThreadPool* matched_pool = pool_manager->getPool(objectID);
	sm_->setPool(matched_pool);
	
	number_of_objects[objectID]++;
	pool_manager->recalculateThreads(objectID,number_of_objects[objectID]);
	
}

void ConfiguredThreadPoolsRT::removeObject(StateMachineI* sm_)
{
	int objectID = sm_->getPoolId();
	number_of_objects[objectID]--;
	pool_manager->recalculateThreads(objectID,number_of_objects[objectID]);
	
}
