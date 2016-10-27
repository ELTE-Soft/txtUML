#include "statemachineI.hpp"
#include "threadpool.hpp"
#include "runtime.hpp"

#include <iostream>


 StateMachineI::StateMachineI(std::shared_ptr<MessageQueueType> messageQueue_)
         :_messageQueue(messageQueue_), _pool(nullptr), _inPool(false), _started(false), _initialized(false){}


void StateMachineI::init()
{
	_initialized = true;
	processInitTranstion();
}

void StateMachineI::send(EventPtr e_)
{
  (*message_counter)++;
  _messageQueue->push_back(e_);
  if (_started)
  {
	if(_pool != nullptr)
  	{
    		handlePool();
  	}
	else
	{
	    	RuntimeI<SingleThreadRT>::createRuntime()->enqueObject(this);
	}
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
		
}
