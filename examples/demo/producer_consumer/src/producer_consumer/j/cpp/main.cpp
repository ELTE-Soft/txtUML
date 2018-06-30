#include <iostream>

#include "Env.hpp"
#include "deployment.hpp"
#include "AssociationInstances.hpp"
#include "Action.hpp"
#include "EventStructures.hpp"
#include "Storage.hpp"
#include "Producer.hpp"
#include "Consumer.hpp"

int main()
{
	Env::initEnvironment();
	UsedRuntimePtr rt = UsedRuntimeType::getRuntimeInstance();

	Model::Storage* storage = new Model::Storage(2);
	Model::Producer* p1 = new Model::Producer(3);
	Model::Producer* p2 = new Model::Producer(3);
	Model::Consumer* c1 = new Model::Consumer(2);
	Model::Consumer* c2 = new Model::Consumer(2);
	Model::Consumer* c3 = new Model::Consumer(2);

	Action::link(Model::Production.producer, p1, Model::Production.storage, storage);
	Action::link(Model::Production.producer, p2, Model::Production.storage, storage);
	Action::link(Model::Consumption.consumer, c1, Model::Consumption.storage, storage);
	Action::link(Model::Consumption.consumer, c2, Model::Consumption.storage, storage);
	Action::link(Model::Consumption.consumer, c3, Model::Consumption.storage, storage);

	Action::start(storage);
	Action::start(p1);
	Action::start(p2);
	Action::start(c1);
	Action::start(c2);
	Action::start(c3);

	rt->startRT();
	rt->stopUponCompletion();

	return 0;
}
