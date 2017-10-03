#ifndef STATEMACHINEBASE_H_INCLUDED
#define STATEMACHINEBASE_H_INCLUDED


#include <functional>
#include <utility>
#include <list>
#include <vector>
#include <unordered_map>
#include <memory>

#include "ievent.hpp"
#include "ESRoot/Types.hpp"
namespace Model
{
	
struct EventState {
EventState(int e_, int s_, int p_) : event (e_), state (s_), port (p_) {}
  
int event;
int state;
int port;
};

inline bool operator == (const EventState& a, const EventState& b) {
	return b.event == a.event && b.state == a.state && b.port == a.port;

}

}


namespace std
{
	
	template<>
	struct hash<Model::EventState> : public unary_function<Model::EventState, size_t>
	{
	  std::size_t operator ()(const Model::EventState& es_) const
	  {
	    hash<int> intHash;
	    size_t hashValue = (intHash(es_.event) ^ intHash(es_.state) ^ intHash(es_.port) );
	    return hashValue;
	  }
	};
}

namespace Model
{
	
class StateMachineBase
{
public:
 virtual bool process_event(ES::EventRef)=0;
 virtual void setInitialState()=0;
 virtual void initialize(ES::EventRef) = 0;
 virtual void finalize(ES::EventRef) = 0;
 virtual ~StateMachineBase(){}
protected:
  bool defaultGuard(ES::EventRef){return true;}
};

}

#endif // STATEMACHINEBASE_H_INCLUDED
