/** @file ModelObject.hpp
Contains interface for model classes with the common methods: send, start, delete, assoc
*/

#ifndef OBJECT_HPP
#define OBJECT_HPP

#include "ESRoot/Types.hpp"

namespace ES
{
class ModelObject 
{
public:
	/*!
	Sends a message to the object.
	*/	
	virtual void send(const EventRef) = 0;
	
	/*!
	Starts the object.
	*/
	virtual void start() = 0;
	
	/*!
	Deletes the object.
	*/
	virtual void deleteObject() = 0;

	/*!
	Helper method to navigate via objects by the given role.
	*/
    template<typename Role>
    const auto& assoc(Role* role) {
        return role->association->get(this, role);
	}
};
}

#endif

