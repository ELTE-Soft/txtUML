/** @file Action.hpp
*/
#include "istatemachine.hpp"
#include "ESRoot/Types.hpp"

namespace Action 
{
	
void send(ES::StateMachineRef target, ES::EventRef signal);
/**<
Sends a message to a model object.
@param target The target object where the signal have to be arrived. 
@param signal The signal to be send.
*/

void start(ES::StateMachineRef sm);
/**<
Start the statemachine of a model object.
@param sm The state machine to be started. 
*/
	
void log(ES::String message);
/**<
Logs a message.
@param message The message to be loged. 
*/
	
template<typename Association, typename LeftEnd, typename RightEnd>
void link(typename LeftEnd::EdgeType* e1, typename RightEnd::EdgeType* e2);
/**<
Links two model object with each other via the specified association.
It has no effect if the object are already linked via the specifed association.
@param Association The association descriptior structure.
@param LeftEnd The roule type of left end inside the Association strcutre.
@param RightEnd The roule type of rigth end inside the Association strcutre.
@param e1 The object at the left end of the association.
@param e2 The object at the right end of the association.
*/

	
template<typename Association, typename LeftEnd, typename RightEnd>
void unlink(typename LeftEnd::EdgeType* e1, typename RightEnd::EdgeType* e2);
/**<
Unlinks two model object with each other via the specified association.
It has no effect if the object are already linked via the specifed association.
@param Association The association descriptior structure.
@param LeftEnd The roule type of left end inside the Association strcutre.
@param RightEnd The roule type of rigth end inside the Association strcutre.
@param e1 The object at the left end of the association.
@param e2 The object at the right end of the association.
*/



template<typename Association, typename LeftEnd, typename RightEnd>
void link(typename LeftEnd::EdgeType* e1, typename RightEnd::EdgeType* e2)
{
	e1->template link<Association,RightEnd>(e2);
	e2->template link<Association,LeftEnd>(e1);
}

template<typename Association, typename LeftEnd, typename RightEnd>
void unlink(typename LeftEnd::EdgeType* e1, typename RightEnd::EdgeType* e2)
{
	e1->template link<Association,RightEnd>(e2);
	e2->template link<Association,LeftEnd>(e1);
}

}
