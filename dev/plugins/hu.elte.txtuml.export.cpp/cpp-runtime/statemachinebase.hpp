#ifndef STATEMACHINEBASE_H_INCLUDED
#define STATEMACHINEBASE_H_INCLUDED


#include <functional>
#include <utility>
#include <list>
#include <vector>
#include <unordered_map>
#include <memory>

#include "runtime/ievent.hpp"

struct EventState : public std::pair<int, int> {
  EventState(int e_, int s_, int p_) : event (e_), state (s_), port (p_) {}

  bool operator == (const EventState& a){
    return event == a.event && this->state == a.state && this->port == a.port;
  }
  
	int event;
	int state;
	int port;
};

namespace std
{
  template<>
	struct hash<EventState> : public unary_function<EventState, size_t>
	{
	  std::size_t operator ()(const EventState& es_) const
	  {
	    hash<int> intHash;
	    size_t hashValue = (intHash(es_.event) ^ intHash(es_.state) ^ intHash(es_.port) );
	    return hashValue;
	  }
	};
}


class StateMachineBase
{
public:
 virtual bool process_event(EventBaseCRef)=0;
 virtual void setInitialState()=0;
 virtual ~StateMachineBase(){}
protected:
  bool defaultGuard(EventBaseCRef){return true;}
};


#endif // STATEMACHINEBASE_H_INCLUDED
