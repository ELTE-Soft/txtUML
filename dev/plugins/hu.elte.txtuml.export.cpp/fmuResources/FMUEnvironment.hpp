#ifndef fmuenvironment_hpp
#define fmuenvironment_hpp

#include "fmi2Functions.h"
#include "statemachinebase.hpp"
#include "statemachineI.hpp"
#include "associations.hpp"

struct fmu_environment;

class FMUEnvironment : public StateMachineBase, public StateMachineI {
  public:
    bool process_event(EventBaseCRef) = 0;
    void setInitialState() = 0;

    AssociationEnd<$fmuclass> LanderWorld_lander = AssociationEnd< $fmuclass > (1, 1);
    template<typename T, typename EndPointName>
    void link(typename EndPointName::EdgeType*) {
    }
    template<typename T, typename EndPointName>
    void unlink(typename EndPointName::EdgeType*) {
    }

    void processEventVirtual() {
        EventI* base = getNextMessage().get();
        EventBase* realEvent = static_cast<EventBase*>(base);
        process_event(*realEvent);
        deleteNextMessage();
    }

    void processInitTranstion() {}

    ~FMUEnvironment() {}
};

#endif /* fmuenvironment_hpp */