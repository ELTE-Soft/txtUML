#include "runtime.hpp"

//********************************RuntimeI********************************

RuntimeI::RuntimeI():_stop(false){}

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
  sm_->setRuntime(this);
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
  for(;;)
  {
    if(_stop)break;
    if(!_messageQueue->empty())
    {
      _messageQueue->front()->dest.processEventVirtual();
    }
  }
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

