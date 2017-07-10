#include "istatemachine.hpp"
#include "threadpool.hpp"
#include "runtime.hpp"

#include <assert.h>

namespace Model
{

IStateMachine::IStateMachine()
	:_messageQueue(new ES::MessageQueueType()),
	_pool(nullptr), 
	_inPool(false), 
	_started(false), 
	_initialized(false), 
	_deleted (false) {}

void IStateMachine::setPoolId(int id) 
{ 
	poolId = id; 
}

void IStateMachine::destroy()
{
	setPooled(false);
	delete this;
}

void IStateMachine::init()
{
	_initialized = true;
	processInitTransition();
}

ES::EventRef IStateMachine::getNextMessage()
{
	ES::EventRef event;
	_messageQueue->dequeue(event);
	messageCounter->decrementCounter();
	return event;
}

bool IStateMachine::emptyMessageQueue() 
{ 
	return _messageQueue->isEmpty();
}

void IStateMachine::setPool(ES::SharedPtr<Execution::StateMachineThreadPool> pool) 
{ 
	_pool = pool; 
}

void IStateMachine::setMessageQueue(ES::SharedPtr<ES::MessageQueueType> messageQueue) 
{ 
	_messageQueue = messageQueue; 
}

bool IStateMachine::processNextEvent()
{
	ES::EventRef nextEvent = _messageQueue->next();
	switch (nextEvent->getSpecialType()) {
	case SpecialSignalType::NoSpecial:
		processEventVirtual();
		break;
	case SpecialSignalType::InitSignal:
		init();
		break;
	case SpecialSignalType::DestorySignal:
		destroy();
		return false;
		break;
	default:
		assert(false);
		return false;
	}

	return false;
}

void IStateMachine::startSM()
{ 
	_started = true; 
	handlePool();
}

void IStateMachine::send(const ES::EventRef e)
{
	messageCounter->incrementCounter();
	e->setTargetSM(this);
	_messageQueue->enqueue(e);
	if (_started)
	{
		handlePool();
	}


}

void IStateMachine::handlePool()
{
	if (!_inPool && _pool != nullptr)
	{
		_inPool = true;
		_pool->enqueueObject(this);
	}
}

void IStateMachine::setPooled(bool value = true)
{
	_inPool = value;
	_cond.notify_one();
}

bool IStateMachine::isInPool() const 
{ 
	return _inPool; 
}

bool IStateMachine::isStarted() const 
{ 
	return _started; 
}

bool IStateMachine::isInitialized() const 
{ 
	return _initialized; 
}

bool IStateMachine::isDestroyed() const
{ 
	return _deleted; 
}

int IStateMachine::getPoolId() const 
{ 
	return poolId; 
}

void IStateMachine::setMessageCounter(ES::SharedPtr<ES::AtomicCounter> counter)
{ 
	messageCounter = counter; 
}

std::string IStateMachine::toString() const
{ 
	return ""; 
}

IStateMachine::~IStateMachine()
{
	messageCounter->decrementCounter((unsigned) _messageQueue->size());
	std::unique_lock<std::mutex> mlock(_mutex);
	while (_inPool)
	{
		_cond.wait(mlock);
	}

}

}

