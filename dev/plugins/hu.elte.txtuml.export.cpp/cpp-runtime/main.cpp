#include <iostream>
#include "deployment.hpp"
//#include "Class1.hpp"
//#include "Class2.hpp"

int main()
{
  //Runtime* rt=deployment::createThreadedRuntime(); // the runtime type depends on deployment header options(default - threaded)
  //rt->startRT(); // the runtime could be started later too..
  
  //Class1 obj1; //create an object
  //Class2 obj2; // create an other object
  
  //obj1.startSM(); //starts the statemachine of obj1
  //obj1.send(EventPtr(new SignalName_EC()); //send a signal called SignalName to obj1
  /* there is an association called Class1ToClass2 between Calass1(let role name c1) and Class2(let rola name c2) you are able to link this objects like this:	
  //action::link<Class1ToClass2,typename Class1ToClass2::c1,typename Class1ToClass2::c2>(&obj,&obj2); // link objects
  // unlink is analoge
  */


  std::cout << "The files are successfully compiled!\nYou can now modify the main.cpp" << std::endl;
  std::cin.get();
  return 0;
}
