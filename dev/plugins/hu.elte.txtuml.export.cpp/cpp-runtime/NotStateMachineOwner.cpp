#include "NotStateMachineOwner.hpp"

namespace Model
{
// NotStateMachineOwner
NotStateMachineOwner::~NotStateMachineOwner()
{
}

void NotStateMachineOwner::start()
{
	//empty statement
}

void NotStateMachineOwner::deleteObject()
{
	delete this;
}

void NotStateMachineOwner::send(const ES::EventRef /*e*/)
{
	//empty statement
}
}

