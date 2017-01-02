#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <condition_variable>
#include <mutex>
#include "fmi2Functions.h"

std::condition_variable cv;
std::mutex m;

const int BUFFER_SIZE = 30;

bool ready = false;

void stepFinishNotify(fmi2ComponentEnvironment, fmi2Status) {
  ready = true;
  cv.notify_one();
}

fmi2CallbackFunctions *callbacks = new fmi2CallbackFunctions{ NULL, NULL, NULL, stepFinishNotify, NULL };

int main(int, char *argv[]) {
  std::cout << "Starting simulation" << std::endl;

  std::ifstream infile(argv[1]);

  fmi2Component comp = fmi2Instantiate("tested", fmi2CoSimulation, "GUID-HERE", "RESOURCE-LOC", callbacks, true, false);

  std::string line;
  while (std::getline(infile, line))
  {
    // set input variables
    std::istringstream iss(line);
    fmi2ValueReference i = 0;
    fmi2Real temp[1];
    std::cout << std::flush << "inputs: ";
    while(iss >> temp[0]) {
      fmi2ValueReference vars[] = { i++ };
      fmi2SetReal(comp, vars, 1, temp);
      std::cout << temp[0] << " ";
    }
    std::cout << std::endl;
    
    // perform stepping
    fmi2DoStep(comp, 0.0, 0.0, true);
    std::unique_lock<std::mutex> lk(m);
    cv.wait(lk, []{ return ready; });
    ready = false;

    // write output variables
    fmi2Status res;
    std::cout << std::flush << "outputs: ";
    do {
      fmi2ValueReference vars[] = { i++ };
      res = fmi2GetReal(comp, vars, 1, temp);
      if (res != fmi2Error) {
        std::cout << temp[0] << " ";
      }
    } while (res != fmi2Error);
    std::cout << std::endl;
  }

  return 0;
}
