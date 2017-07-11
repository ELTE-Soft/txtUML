#include "istatemachine.hpp"
#include "threadpool.hpp"
#include "runtime.hpp"
#include "ievent.hpp"

#include <assert.h>

namespace Model
{

IStateMachine::IStateMachine()
	:_messageQueue(new ES::MessageQueueType()),
	_pool(nullptr), 
	_inPool(false), 
	_started(false) {}

void IStateMachine::setPoolId(int id) 
{ 
	poolId = id; 
}

void IStateMachine::destroy()
{
	setPooled(false);
	messageCounter->decrementCounter((unsigned)_messageQueue->size());
	std::function<bool(const ES::EventRef&)> p = [this](const ES::EventRef& e) {return e->getTargetSM() == this; };
	_messageQueue->modifyElements([this](const ES::EventRef& e) {return e->getTargetSM() == this; },
		[](ES::EventRef& e) {IEvent<EventBase>::invalidatesEvent(e); });
	delete this;
}

void IStateMachine::init()
{
	processInitTransition();
}

ES::EventRef IStateMachine::getNextMessage()
{
	ES::EventRef event;
	_messageQueue->dequeue(event);
	messageCounter->decrementCounter();
	return event;
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
	ES::EventRef nextEvent = getNextMessage();
	switch (nextEvent->getSpecialType()) {
	case SpecialSignalType::NoSpecial:
		processEventVirtual();
		return !_messageQueue->isEmpty();
		break;
	case SpecialSignalType::InitSignal:
		init();
		return true;
		break;
	case SpecialSignalType::DestorySignal:
		destroy();
		return false;
		break;
	default:
		assert(false);
		return false;
	}

}

void IStateMachine::startSM()
{ 
	send(ES::EventRef(new InitSpecialSignal()));
	_started = true;
}

void IStateMachine::deleteSM()
{
	send(ES::EventRef(new DestorySpecialSignal()));
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
	std::unique_lock<std::mutex> mlock(_mutex);
	while (_inPool)
	{
		_cond.wait(mlock);
	}

}

}

