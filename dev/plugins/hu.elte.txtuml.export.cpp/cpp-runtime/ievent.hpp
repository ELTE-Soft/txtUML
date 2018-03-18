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

        PortType getPortType() const
	{
                return static_cast<const DerivedBase*>(this)->pType;
	}
	
	void setPortType(PortType portType)
	{
		static_cast<DerivedBase*>(this)->pType = portType;
	}

	SpecialSignalType getSpecialType() const
	{
		return static_cast<const DerivedBase*>(this)->specialType;
	}

public:
	static void invalidatesEvent(ES::SharedPtr<IEvent<DerivedBase>>& event) {
		event->setTargetSM(nullptr);
	}

	static bool eventIsValid(const ES::SharedPtr<IEvent<DerivedBase>>& event) {
		return event->getTargetSM() != nullptr;
	}

};


template<typename DerivedBase>
class SpecialEventChecker {
public:
	bool operator() (const ES::SharedPtr<const IEvent<DerivedBase>>& e) {
		return e->getSpecialType() != SpecialSignalType::NoSpecial;
	}
};

class EventBase : public IEvent<EventBase>
{
public:
	EventBase(int t_, SpecialSignalType extermalType_ = SpecialSignalType::NoSpecial, PortType pType_ = PortType::AnyPort) :
		t(t_),
		specialType(extermalType_),
                pType(pType_) {}

	ES::StateMachineRef targetSM;
	int t;
	SpecialSignalType specialType;
	PortType pType;

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
