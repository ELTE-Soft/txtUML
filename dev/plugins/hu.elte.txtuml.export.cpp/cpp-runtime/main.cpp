#include <iostream>
#include "deployment.hpp"

int main()
{
  //Runtime* rt=deployment::createThreadedRuntime(); // the runtime type depends on deployment header options

  //Class obj(); //create the object
  //obj.startSM(); //starts the statemachine of the object
  //obj.send(EventPtr(new SignalName_EC(obj,Class::SignalName_EE))); //send a signal to the object

  //rt->run();

  std::cout << "The files are successfully compiled!\nYou can now modify the main.cpp" << std::endl;
  std::cin.get();
  return 0;
}
