#ifndef EVENTI_HPP_INCLUDED
#define EVENTI_HPP_INCLUDED

#include "runtimetypes.hpp"

class StateMachineI;

struct IEvent
{
  IEvent () {}
  virtual ~IEvent () {}
  
  int t;
  int p; 
  
};

struct EventBase : public IEvent
{
  EventBase (int t_) : t (t_), p (NoPort_PE) {}
	
  int t;
  int p;
};

typedef const EventBase& EventBaseCRef;

#endif // EVENTI_HPP_INCLUDED
