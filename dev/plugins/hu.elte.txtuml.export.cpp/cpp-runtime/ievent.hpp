#ifndef EVENTI_HPP_INCLUDED
#define EVENTI_HPP_INCLUDED

#include "runtimetypes.hpp"
#include "ESRoot\Types.hpp"

class IEvent
{
public:
  IEvent () {}
  virtual ~IEvent () {}
  virtual void setTargetSM (const ES::StateMachineRef sm) = 0;
  virtual const ES::StateMachineRef getTargetSM () const = 0;
  
};

class EventBase : public IEvent
{
public:
  EventBase (int t_) : t (t_), p (NoPort_PE) {}
  virtual void setTargetSM (const ES::StateMachineRef sm) override {targetSM = sm;}
  virtual const ES::StateMachineRef getTargetSM () const override {return targetSM;}
  
  
  ES::StateMachineRef targetSM;
  int t;
  int p;
};

typedef const EventBase& EventBaseCRef;

#endif // EVENTI_HPP_INCLUDED
