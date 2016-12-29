#ifndef fmuenvironment_hpp
#define fmuenvironment_hpp

#include "fmi2Functions.hpp"

typedef struct fmu_environment;

class FMUEnvironment : public StateMachineBase {

   bool process_event(EventBaseCRef);
   void setInitialState();

};

#endif /* fmuenvironment_hpp */