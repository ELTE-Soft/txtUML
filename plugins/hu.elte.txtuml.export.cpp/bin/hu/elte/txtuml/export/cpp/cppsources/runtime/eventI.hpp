#ifndef EVENTI_HPP_INCLUDED
#define EVENTI_HPP_INCLUDED

class StateMachineI;

struct EventI
{
  EventI(StateMachineI& dest_):dest(dest_){}
  virtual ~EventI(){}

  StateMachineI& dest;
};

#endif // EVENTI_HPP_INCLUDED
