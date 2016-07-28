#ifndef ITIMER_H
#define ITIMER_H

#include "statemachineI.hpp"
#include "runtimetypes.hpp"
class Timer;

class ITimer
{
public:
    static Timer* start(StateMachineI*,EventPtr,int);

    virtual int query() = 0;
    virtual void reset(int) = 0;
    virtual void add(int) = 0;
    virtual bool cancel() = 0;


};

#endif // ITIMER_H
