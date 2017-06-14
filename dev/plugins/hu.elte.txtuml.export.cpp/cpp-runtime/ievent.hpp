#ifndef EVENTI_HPP_INCLUDED
#define EVENTI_HPP_INCLUDED

#include "ESRoot/Types.hpp"

namespace Model
{

template<typename DerivedBase>
class IEvent
{
public:
	IEvent() {}
	virtual ~IEvent() {}
	void setTargetSM(const ES::StateMachineRef sm)
	{
		static_cast<ES::Ptr<DerivedBase>>(this)->targetSM = sm;
	}
	ES::StateMachineRef getTargetSM() const
	{
		return static_cast<ES::Ptr<const DerivedBase>>(this)->targetSM;
	}

	int getType() const
	{
		return static_cast<ES::Ptr<const DerivedBase>>(this)->t;
	}

	int getPortType() const
	{
		return static_cast<ES::Ptr<const DerivedBase>>(this)->p;
	}

};

class EventBase : public IEvent<EventBase>
{
public:
	EventBase(int t_) : t(t_), p(1) {}

	ES::StateMachineRef targetSM;
	int t;
	int p;
};

}


#endif // EVENTI_HPP_INCLUDED
