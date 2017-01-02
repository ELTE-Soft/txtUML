#ifndef fmuenvironment_hpp
#define fmuenvironment_hpp

#include "fmi2Functions.h"
#include "statemachinebase.hpp"
#include "associations.hpp"

struct fmu_environment;

class FMUEnvironment : public StateMachineBase {
  public:
    bool process_event(EventBaseCRef);
    void setInitialState();

    AssociationEnd<$fmuclass> LanderWorld_lander = AssociationEnd< $fmuclass > (1, 1);
    template<typename T, typename EndPointName>
    void link(typename EndPointName::EdgeType*) {
    }
    template<typename T, typename EndPointName>
    void unlink(typename EndPointName::EdgeType*) {
    }
};

#endif /* fmuenvironment_hpp */