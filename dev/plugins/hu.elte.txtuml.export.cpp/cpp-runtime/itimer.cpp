#include "itimer.hpp"
#include "timer.hpp"

Timer *ITimer::start(IStateMachine *sm, ES::EventRef event, int millisecs) {
    return new Timer(sm,event,millisecs);
}
