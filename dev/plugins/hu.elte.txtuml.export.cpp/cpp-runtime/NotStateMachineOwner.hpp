#ifndef NOT_STATE_MACHINE_OWNER_HPP
#define NOT_STATE_MACHINE_OWNER_HPP


#include "ModelObject.hpp"

namespace Model
{
class NotStateMachineOwner : public ES::ModelObject
{
public:
	virtual ~NotStateMachineOwner();
	virtual void start() override;
	virtual void deleteObject() override;
	virtual void send(const ES::EventRef e) override;

};
}

#endif
