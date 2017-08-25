#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include "fmi2Functions.h"

void stepFinishNotify(fmi2ComponentEnvironment, fmi2Status) {
}

fmi2CallbackFunctions *callbacks = new fmi2CallbackFunctions{ NULL, NULL, NULL, stepFinishNotify, NULL };

int main(int, char *argv[]) {
  std::cout << "Starting simulation" << std::endl << std::endl;

  std::ifstream infile(argv[1]);

  fmi2Component comp = fmi2Instantiate("tested", fmi2CoSimulation, "GUID-HERE", "RESOURCE-LOC", callbacks, true, false);

  std::string line;
  while (std::getline(infile, line))
  {
    $declarebuffers
    fmi2ValueReference vars[] = { 0 };

    // set input variables
    std::istringstream iss(line);
    std::cout << std::flush << "inputs: ";

    $setinputvariables
    std::cout << std::endl;
    
    // perform stepping
    fmi2DoStep(comp, 0.0, 0.0, true);

    // get output variables
    std::cout << std::flush << "outputs: ";

    $getoutputvariables
    std::cout << std::endl << std::endl;
  }

  return 0;
}
