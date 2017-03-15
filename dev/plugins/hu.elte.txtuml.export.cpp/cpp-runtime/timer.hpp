#ifndef TIMER_H
#define TIMER_H

#include <condition_variable>
#include <functional>
#include <chrono>
#include <ostream>
#include <thread>

#include "itimer.hpp"
#include "ESRoot/Types.hpp"

class Timer : public ITimer
{          
typedef std::chrono::milliseconds milliseconds;
public:

    Timer(ES::StateMachineRef,ES::EventRef,int);
    ~Timer();

private:

    void schedule(int millisecs);
    void scheduledTask();
	
    std::condition_variable _cond;
	
	int _millisecs;
    std::function<void()> _command;
    std::thread _scheduler;

};

#endif // TIMER_H
