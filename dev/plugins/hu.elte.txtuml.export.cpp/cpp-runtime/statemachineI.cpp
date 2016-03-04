#include "statemachineI.hpp"
#include "threadpool.hpp"
#include "runtime.hpp"

#include <iostream>


 StateMachineI::StateMachineI(std::shared_ptr<MessageQueueType> messageQueue_)
         :_messageQueue(messageQueue_), _pool(nullptr), _inPool(false), _started(false), _initialized(false){}

 
void StateMachineI::runSM()
{
    for(;;)
    {
      processEventVirtual();
    }
}

void StateMachineI::init()
{
	_initialized = true;
	processInitTranstion();
}

void StateMachineI::send(EventPtr e_)
{
  _messageQueue->push_back(e_);
  if(_pool != nullptr)
  {
    handlePool();
  }
  

}

void StateMachineI::handlePool()
{
  std::unique_lock<std::mutex> mlock(_mutex);
  if(!_inPool)
  {
    _inPool=true;
    _pool->enqueObject(this);
  }
}

void StateMachineI::setPooled(bool value_=true)
{
	  _inPool=value_;
	  _cond.notify_one();
}

StateMachineI::~StateMachineI()
{
	std::unique_lock<std::mutex> mlock(_mutex);
	while(_inPool)
	{
		_cond.wait(mlock);
	}
	
        /*if(_runtime != nullptr)
	{
		_runtime->removeObject(this);
        }*/
		
}
