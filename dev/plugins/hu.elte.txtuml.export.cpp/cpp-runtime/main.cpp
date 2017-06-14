#include <iostream>

#include "Env.hpp"
#include "deployment.hpp"

int main()
{
  Env::initEnvironment();
  UsedRuntimePtr rt = UsedRuntimeType::getRuntimeInstance();
  //rt->startRT(); // the runtime can be started later

  std::cout << "The files are successfully compiled!\nYou can now modify the main.cpp" << std::endl;
  std::cin.get();
  return 0;
}
