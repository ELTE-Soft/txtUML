#ifndef fmuenvironment_hpp
#define fmuenvironment_hpp

#include "fmi2Functions.h"
#include "StateMachineOwner.hpp"

namespace Model {

class FMUEnvironment : public StateMachineOwner {
  public:
    bool process_event(ES::EventRef) = 0;
    void setInitialState() = 0;   

	void processEventVirtual(ES::EventRef event_) {
		process_event(event_);
	}

    void processInitTransition() {}

    ~FMUEnvironment() {}
};

}

#endif /* fmuenvironment_hpp */
