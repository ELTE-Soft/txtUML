#ifndef TIMER_H
#define TIMER_H

#include <condition_variable>
#include <functional>
#include <future>
#include <chrono>
#include <ostream>
#include <thread>

#include "istatemachine.hpp"
#include "runtimetypes.hpp"

#include "itimer.hpp"
#include "ESRoot/Types.hpp"

class Timer : public ITimer
{           typedef std::chrono::milliseconds milliseconds;
       public:

           Timer(ES::StateMachineRef,ES::EventRef,int);
           ~Timer();

           //TODO implement
           virtual void reset(int) {}
           virtual int query() {return 0;}
           virtual void add(int) {}
           virtual bool cancel() {return false;}

       private:

           void schedule(int millisecs);
           void f(int millisecs);

           std::condition_variable _cond;
           std::function<void()> command;
           std::future<void> fut;

};

#endif // TIMER_H
