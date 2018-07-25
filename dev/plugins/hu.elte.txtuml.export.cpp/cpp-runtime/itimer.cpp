#include "itimer.hpp"
#include "timer.hpp"
namespace ES
{

ES::TimerPtr ITimer::schedule(ES::StateMachineRef sm, ES::EventRef event, int millisecs) {
	return ES::TimerPtr(new Timer(sm, event, millisecs));
}

}

