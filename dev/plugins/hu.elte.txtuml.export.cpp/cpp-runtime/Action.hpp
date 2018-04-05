/** @file Action.hpp
*/

#ifndef __ACTION_HPP__
#define __ACTION_HPP__

#include "ESRoot/Types.hpp"
#include "PortUtils.hpp"

namespace ES
{
class ModelObject;
}
namespace Action 
{
	

void send(ES::ModelObject* target, ES::EventRef signal);
/**<
Sends a message to a model object.
@param target The target object where the signal has to be sent. 
@param signal The signal to be sent.
*/
	
template <typename T, typename S>
void send(T target, S signal);
/**<
Sends a message to a port.
@param port The port object where the signal has to be sent. 
@param signal The signal to be sent.
*/

void start(ES::ModelObject* sm);
/**<
Starts the state machine of a model object.
@param sm The state machine to be started. 
*/

void deleteObject(ES::ModelObject* modelObject);
/**<
Dispose a model object reference.
@param modelObject The object to be deleted.
*/
	
void log(ES::String message);
/**<
Logs a message.
@param message The message to be logged. 
*/
	
template<typename L, typename LE, typename R, typename RE>
void link (L* leftObject, LE* leftEnd, R* rightObject, RE* rightEnd);
/**<
Links two model objects through the specified association.
It has no effect if the objects are already linked through the specified association.
@param leftEnd The type representing the left end of the association.
@param rightEnd The type representing the right end of the association.
@param leftObject The object at the left end of the association.
@param rightObject The object at the right end of the association.
*/

	
template<typename LeftEnd, typename RightEnd>
void unlink (L* leftObject, LE* leftEnd, R* rightObject, RE* rightEnd);
/**<
Unlinks two model objects through the specified association.
It has no effect if the objects are not linked through the specified association.
@param leftEnd The type representing the left end of the association.
@param rightEnd The type representing the right end of the association.
@param leftObject The object at the left end of the association.
@param rightObject The object at the right end of the association.
*/



template<typename L, typename LE, typename R, typename RE>
void link (L* leftObject, LE* leftEnd, R* rightObject, RE* rightEnd)
{
	leftEnd->association->link (leftObject, leftEnd, rightObject, rightEnd);
}

template<typename L, typename LE, typename R, typename RE>
void unlink (L* leftObject, LE* leftEnd, R* rightObject, RE* rightEnd);
{
	leftEnd->association->unlink (leftObject, leftEnd, rightObject, rightEnd);

}
	
template <typename T, typename S>
void send(T target, S signal)
{
	target->send(signal);
}

}

#endif
