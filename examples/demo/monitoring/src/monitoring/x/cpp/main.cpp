#include <iostream>

#include "Env.hpp"
#include "deployment.hpp"
#include "ResourceMonitor.hpp"
#include "Aggregator.hpp"
#include "Alert.hpp"
#include "associations.hpp"
#include "Action.hpp"
#include "event.hpp"

int main()
{
  Env::initEnvironment();
  UsedRuntimePtr rt = UsedRuntimeType::getRuntimeInstance();
  rt->startRT(); // the runtime can be started later

  //init part
  Model::ResourceMonitor monitor;
  Model::Aggregator aggregator;
  Model::Alert alert(3);
  Action::link<typename Model::ToAggregator::rmonitor, typename Model::ToAggregator::aggregator>(&monitor,&aggregator);
  Action::link<typename Model::ToAlert::rmonitor, typename Model::ToAlert::alert>(&monitor, &alert);
  Action::start(&monitor);
  Action::start(&aggregator);
  Action::start(&alert);

  Action::log("Testing monitoring example.");
  Action::log("\tq - quit");
  Action::log("\tr - read");
  Action::log("\tw - write");
  Action::log("\tc - close");
  Action::log("\tx - print report");

  char c;
  std::cin >> c;
  while (c != 'q') {
	  if (c == 'r') {
		  Action::send(&monitor, ES::SharedPtr<Model::Read_EC>(new Model::Read_EC()));
	  }
	  else if (c == 'w') {
		  Action::send(&monitor, ES::SharedPtr<Model::Write_EC>(new Model::Write_EC()));
	  }
	  else if (c == 'c') {
		  Action::send(&monitor, ES::SharedPtr<Model::Close_EC>(new Model::Close_EC()));

	  }
	  else if (c == 'x') {
		  Action::send(&aggregator, ES::SharedPtr<Model::PrintReport_EC>(new Model::PrintReport_EC()));

	  }
	  std::cin >> c;
  }
  rt->stopUponCompletion();
  return 0;
}
