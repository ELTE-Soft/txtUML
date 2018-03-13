#include <iostream>

#include "Env.hpp"
#include "deployment.hpp"
#include "associations.hpp"
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
  Action::link<typename Model::Production::producer, typename Model::Production::storage>(p1, storage);
  Action::link<typename Model::Production::producer, typename Model::Production::storage>(p2, storage);
  Action::link<typename Model::Consumption::consumer, typename Model::Consumption::storage>(c1, storage);
  Action::link<typename Model::Consumption::consumer, typename Model::Consumption::storage>(c2, storage);
  Action::link<typename Model::Consumption::consumer, typename Model::Consumption::storage>(c3, storage);
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
