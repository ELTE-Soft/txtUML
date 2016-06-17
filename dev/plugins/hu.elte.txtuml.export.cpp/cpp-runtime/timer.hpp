#ifndef TIMER_H
#define TIMER_H

#include <condition_variable>
#include <functional>
#include <future>
#include <chrono>
#include <ostream>
#include <thread>

#include "itimer.hpp"

class Timer : public ITimer
{           typedef std::chrono::milliseconds milliseconds;
       public:

           Timer(StateMachineI*,EventPtr,int);
           ~Timer();

           //TODO implement
           virtual void reset(int) {}
           virtual int quary() {return 0;}
           virtual void add(int) {}
           virtual bool canel() {return false;}

       private:

           void schedule(int millisecs);
           void f(int millisecs);

           std::condition_variable _cond;
           std::function<void()> command;
           std::future<void> fut;

};

#endif // TIMER_H
