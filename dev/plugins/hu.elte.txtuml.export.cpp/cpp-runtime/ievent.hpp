#ifndef EVENTI_HPP_INCLUDED
#define EVENTI_HPP_INCLUDED

#include "ESRoot/Types.hpp"

namespace Model
{


enum class SpecialSignalType {
	NoSpecial,
	InitSignal,
	DestorySignal
};


template<typename DerivedBase>
class IEvent
{
public:
	IEvent() {}
	virtual ~IEvent() {}
	void setTargetSM(const ES::StateMachineRef sm)
	{
		static_cast<DerivedBase*>(this)->targetSM = sm;
	}
	ES::StateMachineRef getTargetSM() const
	{
		return static_cast<const DerivedBase*>(this)->targetSM;
	}

	int getType() const
	{
		return static_cast<const DerivedBase*>(this)->t;
	}

	int getPortType() const
	{
		return static_cast<const DerivedBase*>(this)->p;
	}

	SpecialSignalType getSpecialType() const
	{
		return static_cast<const DerivedBase*>(this)->specialType;
	}

public:
	static void invalidatesEvent(ES::EventRef& event) {
		event->setTargetSM(nullptr);
	}

	static bool eventIsValid(const ES::EventRef& event) {
		return event->getTargetSM() != nullptr;
	}

};


template<typename Derived>
class SpecialEventChecker {
public:
	bool operator() (const ES::EventRef& e) {
		return e->getSpecialType() != SpecialSignalType::NoSpecial;
	}
};

class EventBase : public IEvent<EventBase>
{
public:
	EventBase(int t_, SpecialSignalType extermalType_ = SpecialSignalType::NoSpecial) :
		t(t_),
		specialType(extermalType_),
		p(1) {}

	ES::StateMachineRef targetSM;
	int t;
	SpecialSignalType specialType;
	int p;

};

class InitSpecialSignal : public EventBase
{
public:
	InitSpecialSignal() : EventBase(0, SpecialSignalType::InitSignal) {}
};

class DestorySpecialSignal : public EventBase
{
public:
	DestorySpecialSignal() : EventBase(0, SpecialSignalType::DestorySignal) {}
};

}


#endif // EVENTI_HPP_INCLUDED
