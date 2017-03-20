#include "itimer.hpp"
#include "timer.hpp"

Timer *ITimer::start(IStateMachine *sm, EventPtr event, int millisecs) {
    return new Timer(sm,event,millisecs);
}
