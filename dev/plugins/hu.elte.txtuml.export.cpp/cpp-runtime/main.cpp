#include <iostream>
#include "deployment.hpp"

int main()
{
  //Runtime* rt=deployment::createThreadedRuntime(); // the runtime type depends on deployment header options
  //rt->startRT();
  
  //Class1 obj1; //create an object
  //Class2 obj2; // create an other object
  //obj1.startSM(); //starts the statemachine of the object
  //obj1.send(EventPtr(new SignalName_EC()); //send a signal to the object
  //action::link<Class1,Class2>(&obj,&obj2); // link objects
  //action::unlink<Class1,Class2>(&obj,&obj2); // unlink objects

  std::cout << "The files are successfully compiled!\nYou can now modify the main.cpp" << std::endl;
  std::cin.get();
  return 0;
}
