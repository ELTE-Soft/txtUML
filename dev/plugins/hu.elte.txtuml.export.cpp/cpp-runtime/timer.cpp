#include "timer.hpp"

#include "ESRoot\Types.hpp"

Timer::Timer(ES::StateMachineRef sm, EventPtr event, int millisecs): command( [sm,event]() {sm->send(event);})
{
    schedule(millisecs);
}

Timer::~Timer()
{
    fut.wait();
    fut.get();
}

void Timer::schedule(int millisecs)
{
    fut = std::async (std::launch::async,&Timer::f,this,millisecs);
}

void Timer::f(int millisecs)
{
    std::mutex mu;
    std::unique_lock<std::mutex> lock(mu);
    _cond.wait_for(lock,milliseconds(millisecs));
    command();
}
