#include <iostream>

#include "runtime/runtime.hpp"

int main()
{
  //RuntimeI* rt=new SingleThreadRT();
  //RuntimeI* rt=new SeparateThreadRT();
  //RuntimeI* rt=new ThreadPoolRT();
  //RuntimeI* rt=new ConfiguredThreadPoolsRT();

  //Class obj;
  //rt->setupObject(&obj);//emplace the object into the runtime
  //rt->startObject(&obj);//starts the statemachine of the object

  //rt->run();

  std::cout << "The files are successfully compiled!\nYou can now modify the main.cpp" << std::endl;
	std::cin.get();
  return 0;
}
