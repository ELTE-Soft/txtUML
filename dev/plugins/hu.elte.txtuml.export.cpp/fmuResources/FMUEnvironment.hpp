#ifndef fmuenvironment_hpp
#define fmuenvironment_hpp

#include "fmi2Functions.h"
#include "statemachinebase.hpp"
#include "istatemachine.hpp"
#include "associations.hpp"

struct fmu_environment;

class FMUEnvironment : public StateMachineBase, public IStateMachine {
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
        IEvent* base = getNextMessage().get();
        EventBase* realEvent = static_cast<EventBase*>(base);
        process_event(*realEvent);
        deleteNextMessage();
    }

    void processInitTransition() {}

    ~FMUEnvironment() {}
};

#endif /* fmuenvironment_hpp */