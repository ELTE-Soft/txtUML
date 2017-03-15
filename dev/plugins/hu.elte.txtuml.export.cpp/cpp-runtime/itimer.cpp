#include "itimer.hpp"
#include "timer.hpp"

ES::TimerPtr ITimer::start(ES::StateMachineRef sm, ES::EventRef event, int millisecs) {
    return ES::TimerPtr(new Timer(sm,event,millisecs));
}
