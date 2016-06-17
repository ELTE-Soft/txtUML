#include "itimer.hpp"
#include "timer.hpp"

Timer *ITimer::start(StateMachineI *sm, EventPtr event, long millisecs) {
    return new Timer(sm,event,millisecs);
}
