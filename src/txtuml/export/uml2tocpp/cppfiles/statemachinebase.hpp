#ifndef STATEMACHINEBASE_H_INCLUDED
#define STATEMACHINEBASE_H_INCLUDED


#include <functional>
#include <utility>
#include <list>
#include <vector>
#include <unordered_map>
#include <memory>

#include "event.hpp"

struct EventState : public std::pair<int, int> {
  EventState(int e_, int s_) : std::pair<int, int>(e_,s_) {}

  bool operator == (const EventState& a){
    return this->first == a.first && this->second == a.second;
  }
};

namespace std
{
  template<>
	struct hash<EventState> : public unary_function<EventState, size_t>
	{
	  std::size_t operator ()(const EventState& es_) const
	  {
	    hash<int> intHash;
	    size_t hashValue = (intHash(es_.first) ^ intHash(es_.second));
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
