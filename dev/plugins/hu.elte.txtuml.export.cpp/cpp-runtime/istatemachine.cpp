#include "istatemachine.hpp"
#include "threadpool.hpp"
#include "runtime.hpp"
#include "ievent.hpp"

#include <assert.h>

namespace Model
{

StateMachineOwner::StateMachineOwner()
	:_messageQueue(new ES::MessageQueueType()),
	_pool(nullptr), 
	_inPool(false), 
	_started(false) {}

StateMachineOwner::~StateMachineOwner()
{
	std::unique_lock<std::mutex> mlock(_mutex);
	while (_inPool)
	{
		_cond.wait(mlock);
	}

}

void StateMachineOwner::setPoolId(int id) 
{ 
	poolId = id; 
}

void StateMachineOwner::destroy()
{
	setPooled(false);
	messageCounter->decrementCounter((unsigned)_messageQueue->size());
	std::function<bool(const ES::EventRef&)> p = [this](const ES::EventRef& e) {return e->getTargetSM() == this; };
	_messageQueue->modifyElements([this](const ES::EventRef& e) {return e->getTargetSM() == this; },
		[](ES::EventRef& e) {IEvent<EventBase>::invalidatesEvent(e); });
	delete this;
}

bool StateMachineOwner::emptyMessageQueue() const
{
	return _messageQueue->isEmpty();
}

ES::EventRef StateMachineOwner::getNextMessage()
{
	ES::EventRef event;
	_messageQueue->dequeue(event);
	messageCounter->decrementCounter();
	return event;
}

void StateMachineOwner::setPool(ES::SharedPtr<Execution::StateMachineThreadPool> pool) 
{ 
	_pool = pool; 
}

void StateMachineOwner::setMessageQueue(ES::SharedPtr<ES::MessageQueueType> messageQueue) 
{ 
	_messageQueue = messageQueue; 
}

bool StateMachineOwner::processNextEvent()
{
	ES::EventRef nextEvent = getNextMessage();
	switch (nextEvent->getSpecialType()) {
	case SpecialSignalType::NoSpecial:
		processEventVirtual(nextEvent);
		return true;
	case SpecialSignalType::InitSignal:
		processInitTransition(nextEvent);
		return true;
	case SpecialSignalType::DestorySignal:
		destroy();
		return false;
	default:
		assert(false);
		return false;
	}

}

void StateMachineOwner::start()
{ 
	_started = true;
	send(ES::EventRef(new InitSpecialSignal()));
}

void StateMachineOwner::deleteObject()
{
	send(ES::EventRef(new DestorySpecialSignal()));
	
}

void StateMachineOwner::send(const ES::EventRef e)
{
	messageCounter->incrementCounter();
	e->setTargetSM(this);
	_messageQueue->enqueue(e);
	if (_started || 
		e->getSpecialType() == SpecialSignalType::DestorySignal)
	{
		handlePool();
	}

}

void StateMachineOwner::handlePool()
{
	if (!_inPool && _pool != nullptr)
	{
		setPooled();
		_pool->enqueueObject(this);
	}
}

void StateMachineOwner::setPooled(bool value)
{
	_inPool = value;
	_cond.notify_one();
}

int StateMachineOwner::getPoolId() const 
{ 
	return poolId; 
}

void StateMachineOwner::setMessageCounter(ES::SharedPtr<ES::AtomicCounter> counter)
{ 
	messageCounter = counter; 
}

std::string StateMachineOwner::toString() const
{ 
	return ""; 
}


// NotStateMachineOwner
NotStateMachineOwner::~NotStateMachineOwner()
{
}

void NotStateMachineOwner::start()
{
	//empty statement
}

void NotStateMachineOwner::deleteObject()
{
	delete this;
}

void NotStateMachineOwner::send(const ES::EventRef e)
{
	//empty statement
}

}

