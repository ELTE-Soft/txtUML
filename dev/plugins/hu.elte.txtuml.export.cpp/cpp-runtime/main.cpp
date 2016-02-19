#include <iostream>

#include "runtime/runtime.hpp"

#include "deployment.hpp"
int main()
{
  //RuntimeI* rt=deployment::createThrededRuntime(); // the runtime type depends on deployment header options

  //Class obj(rt); //create the object and emplace the object into the runtime
  //obj.startSM(); //starts the statemachine of the object
  //obj.send(EventPtr(new SignalName_EC(obj,Class::SignalName_EE))); //send a signal to the object

  //rt->run();

  std::cout << "The files are successfully compiled!\nYou can now modify the main.cpp" << std::endl;
  std::cin.get();
  return 0;
}
