#include "timer.hpp"

#include "istatemachine.hpp"

namespace ES
{

Timer::Timer(ES::StateMachineRef sm, ES::EventRef event, int millisecs) : _command([sm, event]() {sm->send(event); })
{
	schedule(millisecs);
}

Timer::~Timer()
{
	_scheduler.join();
}

void Timer::schedule(int millisecs)
{
	_millisecs = millisecs;
	_scheduler = std::thread(&Timer::scheduledTask, this);
}

void Timer::scheduledTask()
{
	std::mutex mu;
	std::unique_lock<std::mutex> lock(mu);
	_cond.wait_for(lock, milliseconds(_millisecs));
	_command();
}

}

