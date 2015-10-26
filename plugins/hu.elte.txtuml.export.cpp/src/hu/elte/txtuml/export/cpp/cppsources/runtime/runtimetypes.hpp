#ifndef RUNTIMETYPES_HPP_INCLUDED
#define RUNTIMETYPES_HPP_INCLUDED

#include <list>
#include <memory>

#include "threadsafequeue.hpp"

class EventI;
class StateMachineI;


typedef std::shared_ptr<EventI> EventPtr;
typedef ThreadSafeQueue<EventPtr > MessageQueueType;
typedef ThreadSafeQueue<StateMachineI*> PoolQueueType;
typedef std::list<StateMachineI*> ObjectList;
typedef unsigned int id_type;

const int defaultThreadCount = 4;
const int defaultWorkTime = 2000;

#endif // RUNTIMETYPES_HPP_INCLUDED
