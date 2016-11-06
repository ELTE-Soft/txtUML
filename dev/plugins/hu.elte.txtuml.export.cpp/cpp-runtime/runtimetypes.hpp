#ifndef RUNTIMETYPES_HPP_INCLUDED
#define RUNTIMETYPES_HPP_INCLUDED

#include <list>
#include <memory>

#include "threadsafequeue.hpp"

struct IEvent;
class StateMachineI;


typedef std::shared_ptr<IEvent> EventPtr;
typedef ThreadSafeQueue<EventPtr > MessageQueueType;
typedef ThreadSafeQueue<StateMachineI*> PoolQueueType;

const int NoPort_PE = 1;



#endif // RUNTIMETYPES_HPP_INCLUDED
