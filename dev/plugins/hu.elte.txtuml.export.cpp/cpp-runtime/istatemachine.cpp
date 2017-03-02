#include "istatemachine.hpp"
#include "threadpool.hpp"
#include "runtime.hpp"

#include <assert.h>

 IStateMachine::IStateMachine(ES::SharedPtr<ES::MessageQueueType> messageQueue)
         :_messageQueue(messageQueue), _pool(nullptr), _inPool(false), _started(false), _initialized(false){}


void IStateMachine::init()
{
	_initialized = true;
	processInitTransition();
}

ES::EventRef IStateMachine::getNextMessage() 
{ 
	ES::EventRef event;  
	_messageQueue->dequeue(event); 
	(*message_counter)--; 
	return event; 
}

void IStateMachine::send(const ES::EventRef e)
{
  (*message_counter)++;
  e->setTargetSM(this);
  _messageQueue->enqueue(e);
  if (_started && _pool != nullptr)
  {
	 handlePool();
  }
  

}

void IStateMachine::handlePool()
{
	assert(_pool != nullptr && "Handle pool should not be called when there is no associated thread pool");
  std::unique_lock<std::mutex> mlock(_mutex);
  if(!_inPool)
  {
    _inPool=true;
    _pool->enqueueObject(this);
  }
}

void IStateMachine::setPooled(bool value_=true)
{
	  _inPool=value_;
	  _cond.notify_one();
}

IStateMachine::~IStateMachine()
{
	std::unique_lock<std::mutex> mlock(_mutex);
	while(_inPool)
	{
		_cond.wait(mlock);
	}
		
}
