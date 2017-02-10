#ifndef RUNTIMETYPES_HPP_INCLUDED
#define RUNTIMETYPES_HPP_INCLUDED

#include <list>
#include <memory>

#include "threadsafequeue.hpp"

class IEvent;
class IStateMachine;


typedef std::shared_ptr<IEvent> EventPtr;
typedef ThreadSafeQueue<EventPtr> MessageQueueType;
typedef ThreadSafeQueue<IStateMachine*> PoolQueueType;

const int NoPort_PE = 1;



#endif // RUNTIMETYPES_HPP_INCLUDED
