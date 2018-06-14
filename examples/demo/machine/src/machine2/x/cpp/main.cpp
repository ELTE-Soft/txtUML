#include <iostream>

#include "Env.hpp"
#include "deployment.hpp"
#include "Machine.hpp"
#include "User.hpp"
#include "runtime/Action.hpp"
#include "EventStructures.hpp"
#include "AssociationInstances.hpp"


int main()
{
	Env::initEnvironment();
	UsedRuntimePtr rt = UsedRuntimeType::getRuntimeInstance();
	rt->startRT();

	Model::Machine m(3);
	Model::User u1;
	Model::User u2;

	Action::link(Model::Usage.usedMachine, &m, Model::Usage.userOfMachine, &u1);
	Action::link(Model::Usage.usedMachine, &m, Model::Usage.userOfMachine, &u2);

	Action::log("Machine and users are starting.");
	Action::start(&m);
	Action::start(&u1);
	Action::start(&u2);

	Action::send(&u1, ES::SharedPtr<Model::DoYourWork_EC>(new Model::DoYourWork_EC()));

	rt->stopUponCompletion(); // wait for processing all messages
	return 0;
}
