#include <iostream>
#include "deployment.hpp"
#include "init_maps.hpp"

int main()
{
  deployment::initRuntime(); // the runtime type depends on deployment header options(default - threaded)
  UsedRuntimePtr rt = UsedRuntimeType::getRuntimeInstance();
  //rt->startRT(); // the runtime could be started later too..
  StateMachine::initTransitionTables();
 
  
  //obj1.send(EventPtr(new SignalName_EC()); //send a signal called SignalName to obj1
  /* there is an association called Class1ToClass2 between Calass1(let role name c1) and Class2(let rola name c2) you are able to link this objects like this:	
  //action::link<Class1ToClass2,typename Class1ToClass2::c1,typename Class1ToClass2::c2>(&obj,&obj2); // link objects
  // unlink is analoge
  */


  std::cout << "The files are successfully compiled!\nYou can now modify the main.cpp" << std::endl;
  std::cin.get();
  return 0;
}
