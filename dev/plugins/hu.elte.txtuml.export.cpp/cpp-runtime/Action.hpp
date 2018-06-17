/** @file Action.hpp
*/

#ifndef __ACTION_HPP__
#define __ACTION_HPP__

#include "ESRoot/Types.hpp"
#include "PortUtils.hpp"
#include "ESRoot/Elements.hpp"

namespace ES
{
class ModelObject;
}


namespace Action 
{

	
template <typename T, typename S>
void send(T target, S signal);
/**<
Sends a message to a port or model object.
@param port The target (port or statemachine) object where the signal has to be sent. 
@param signal The signal to be sent.
*/

template<typename T>
void start(T sm);
/**<
Starts the state machine of a model object.
@param sm The state machine to be started. 
*/

template<typename T>
void deleteObject(T modelObject);
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
void link (LE* leftEnd, L leftObject, RE* rightEnd, R rightObject);
/**<
Links two model objects through the specified association.
It has no effect if the objects are already linked through the specified association.
@param leftEnd The type representing the left end of the association.
@param rightEnd The type representing the right end of the association.
@param leftObject The object at the left end of the association.
@param rightObject The object at the right end of the association.
*/

	
template<typename L, typename LE, typename R, typename RE>
void unlink (LE* leftEnd, L leftObject, RE* rightEnd, R rightObject);
/**<
Unlinks two model objects through the specified association.
It has no effect if the objects are not linked through the specified association.
@param leftEnd The type representing the left end of the association.
@param rightEnd The type representing the right end of the association.
@param leftObject The object at the left end of the association.
@param rightObject The object at the right end of the association.
*/


template<typename T>
T* getPointer(T* p) {
	return p;
}

template<typename T>
T* getPointer(Model::MultipliedElement<T,1,1> multiPtr) {
	return multiPtr.one();
}

template<typename L, typename LE, typename R, typename RE>
void link (LE* leftEnd, L leftObject, RE* rightEnd, R rightObject)
{
	leftEnd->association->link (getPointer(leftObject), leftEnd, getPointer(rightObject), rightEnd);
}

template<typename L, typename LE, typename R, typename RE>
void unlink (LE* leftEnd, L leftObject, RE* rightEnd, R rightObject)
{
	leftEnd->association->unlink (getPointer(leftObject), leftEnd, getPointer(rightObject), rightEnd);

}
	
template <typename T, typename S>
void send(T target, S signal)
{
	target->send(signal);
}

template<typename T>
void start(T sm) 
{
	sm->start();
}

template<typename T>
void deleteObject(T modelObject)
{
	modelObject->deleteObject();
}

}

#endif
