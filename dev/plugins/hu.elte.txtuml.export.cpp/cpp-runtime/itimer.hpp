#ifndef ITIMER_H
#define ITIMER_H

struct Timer;

class ITimer
{
public:
    static Timer* start(StateMachineI*,EventPtr,long);

    virtual int quary() = 0;
    virtual void reset(int) = 0;
    virtual void add(int) = 0;
    virtual bool canel() = 0;


};

#endif // ITIMER_H
