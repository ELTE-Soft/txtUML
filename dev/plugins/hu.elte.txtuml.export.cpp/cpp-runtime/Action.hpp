/** @file Action.hpp
*/
#include "istatemachine.hpp"
#include "ESRoot/Types.hpp"

namespace Action 
{
	
void send(ES::ModelObject* target, ES::EventRef signal);
/**<
Sends a message to a model object.
@param target The target object where the signal has to be sent. 
@param signal The signal to be sent.
*/

void start(ES::ModelObject* sm);
/**<
Starts the state machine of a model object.
@param sm The state machine to be started. 
*/
	
void log(ES::String message);
/**<
Logs a message.
@param message The message to be logged. 
*/
	
template<typename LeftEnd, typename RightEnd>
void link(typename LeftEnd::EdgeType* e1, typename RightEnd::EdgeType* e2);
/**<
Links two model objects through the specified association.
It has no effect if the objects are already linked through the specified association.
@param LeftEnd The type representing the left end of the association.
@param RightEnd The type representing the rigth end of the association.
@param e1 The object at the left end of the association.
@param e2 The object at the right end of the association.
*/

	
template<typename LeftEnd, typename RightEnd>
void unlink(typename LeftEnd::EdgeType* e1, typename RightEnd::EdgeType* e2);
/**<
Unlinks two model objects through the specified association.
It has no effect if the objects are not linked through the specified association.
@param LeftEnd The type representing the left end of the association.
@param RightEnd The type representing the rigth end of the association.
@param e1 The object at the left end of the association.
@param e2 The object at the right end of the association.
*/



template<typename LeftEnd, typename RightEnd>
void link(typename LeftEnd::EdgeType* e1, typename RightEnd::EdgeType* e2)
{
	e1->template link<RightEnd>(e2);
	e2->template link<LeftEnd>(e1);
}

template<typename LeftEnd, typename RightEnd>
void unlink(typename LeftEnd::EdgeType* e1, typename RightEnd::EdgeType* e2)
{
	e1->template unlink<RightEnd>(e2);
	e2->template unlink<LeftEnd>(e1);
}

}
