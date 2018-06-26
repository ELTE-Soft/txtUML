/** @file itimer.hpp
*/

#ifndef ITIMER_H
#define ITIMER_H

#include "ESRoot/Types.hpp"

namespace ES
{

/*!
* External class which is able to send timed events to the cpp model.
*/
class ITimer
{
public:
	/*!
	* Schedules a new delayed send operation. Sends asynchronously a signal to the
	* target model object after a specified timeout.
	*
	* @param sm
	*            the target state machine owner object of the delayed send operation
	* @param event
	*            the signal which is to be sent after the delay
	* @param millisecs
	*            the time in millisecs to wait before sending the signal
	* @return a timer instance to manage this delayed send operation before it
	*         happens
	*/
	static ES::TimerPtr schedule(ES::StateMachineRef sm, ES::EventRef event, int millisecs);

};

}



#endif // ITIMER_H
