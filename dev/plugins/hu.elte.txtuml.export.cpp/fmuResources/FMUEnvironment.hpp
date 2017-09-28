#ifndef fmuenvironment_hpp
#define fmuenvironment_hpp

#include "fmi2Functions.h"
#include "statemachinebase.hpp"
#include "istatemachine.hpp"
#include "associations.hpp"

namespace Model {

class FMUEnvironment : public StateMachineBase, public IStateMachine {
  public:
    bool process_event(ES::EventRef) = 0;
    void setInitialState() = 0;

    Model::AssociationEnd<$fmuclass> $fmuassociationend = AssociationEnd< $fmuclass > (1, 1);
    
    template<typename EndPointName>
    void link(typename EndPointName::EdgeType*);
    
    template<typename EndPointName>
    void unlink(typename EndPointName::EdgeType*);

	void processEventVirtual(ES::EventRef event_) {
		process_event(event_);
	}

    void processInitTransition() {}

    ~FMUEnvironment() {}
};

}

#endif /* fmuenvironment_hpp */
