#ifndef OBJECT_HPP
#define OBJECT_HPP

#include "ESRoot/Types.hpp"

namespace ES
{
class ModelObject 
{
public:		
	virtual void send(const EventRef) = 0;
	virtual void start() = 0;
	virtual void deleteObject() = 0;

        template<typename Role>
        const auto& assoc(Role* role) {
		return role->association->get(static_cast<typename Role::RoleType*>(this), role);
	}
};
}

#endif

