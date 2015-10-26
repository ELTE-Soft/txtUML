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


//********************************SeparateThreadRT********************************

void SeparateThreadRT::startObject(StateMachineI* sm_)
{
  sm_->startSM();
  std::thread th(&StateMachineI::runSM,sm_);
  th.detach();
}

void SeparateThreadRT::run()
{
   std::unique_lock<std::mutex> mlock(_mutex);
    while (!_stop)
    {
      _cond.wait(mlock);
    }
}

//********************************SingleThreadRT**********************************

SingleThreadRT::SingleThreadRT():_messageQueue(new MessageQueueType){}

void SingleThreadRT::run()
{
  waiting = false;
  while(!_stop)
  {
  
    if(!_messageQueue->empty())
    {
      _messageQueue->front()->dest.processEventVirtual();
    }
	else if(waiting && _messageQueue->empty())
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

//********************************ThreadPoolRT************************************

ThreadPoolRT::ThreadPoolRT(size_t threads_,int workTime_):_pool(threads_,workTime_){}

void ThreadPoolRT::run()
{
    std::unique_lock<std::mutex> mlock(_mutex);
    while (!_stop)
    {
      _cond.wait(mlock);
    }
}

//********************************ConfiguredThreadPoolsRT************************************

 
ConfiguredThreadPoolsRT::ConfiguredThreadPoolsRT(): pool_manager(new ThreadPoolManager())
{
	pool_ides = pool_manager->get_idies();
	for(std::list<id_type>::iterator it = pool_ides.begin(); it != pool_ides.end(); it++)
	{
		number_of_objects[*it] = 0;
	}
		
}

void ConfiguredThreadPoolsRT::run()
{
    
	for(std::list<id_type>::iterator it = pool_ides.begin(); it != pool_ides.end(); it++)
	{
		pool_manager->get_pool(*it)->startPool();
	}
}

void ConfiguredThreadPoolsRT::stopUponCompletion()
{
	for(std::list<id_type>::iterator it = pool_ides.begin(); it != pool_ides.end(); it++)
	{
		pool_manager->get_pool(*it)->stopUponCompletion();
	}
}

void ConfiguredThreadPoolsRT::setupObjectVirtual(StateMachineI* sm_)
{
	
	id_type object_id = sm_->getPoolId();
	StateMachineThreadPool* matched_pool = pool_manager->get_pool(object_id);
	
	sm_->setPool(matched_pool);
	
	number_of_objects[object_id] = number_of_objects[object_id] + 1;
	pool_manager->recalculateThreads(object_id,number_of_objects[object_id]);
	
}

void ConfiguredThreadPoolsRT::removeObject(StateMachineI* sm_)
{
	id_type object_id = sm_->getPoolId();
	number_of_objects[object_id] = number_of_objects[object_id] - 1;
	pool_manager->recalculateThreads(object_id,number_of_objects[object_id]);
	
}