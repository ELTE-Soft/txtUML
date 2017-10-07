#include "istatemachine.hpp"
#include "ievent.hpp"
#include "ESRoot/Types.hpp"
#include <iostream>
#ifndef PORT_HPP
#define PORT_HPP

namespace Model
{
template <typename ProvidedInf, typename RequiredInf>
class IPort;

template <typename ProvidedInf, typename RequiredInf>
class Port;

template <typename ProvidedInf, typename RequiredInf>
class PortImpl;

template <typename ProvidedInf, typename RequiredInf>
struct DelegationConnection;

template <typename ProvidedInf, typename RequiredInf>
struct AssemblyConnection;


struct IConnection;



template <typename ProvidedInf, typename RequiredInf>
class IPort : public RequiredInf::RequiredInfType , public ProvidedInf::ProvidedInfType
{
public:
	template <typename RequiredInf1, typename ProvidedInf1>
	friend class PortImpl;

	template <typename RequiredInf1, typename ProvidedInf1>
	friend struct AssemblyConnection;

	void setAssemblyConnectedPort (IPort<RequiredInf,ProvidedInf> * connectedPort_);
	void setDelgationConnectedPort (Port<ProvidedInf,RequiredInf> * connectedPort_);

protected:
	IConnection * connectedPort;
};

template <typename ProvidedInf, typename RequiredInf>
class Port : public IPort<ProvidedInf, RequiredInf>
{
public:
	Port () : connectionToInnerPort(nullptr) {}
	virtual ~Port() {}
	void setInnerConnection(IPort<RequiredInf, ProvidedInf> * innerPort) { connectionToInnerPort = innerPort; }
protected:
	
	IPort<RequiredInf, ProvidedInf> * connectionToInnerPort;


};

template <typename ProvidedInf, typename RequiredInf>
class BehaviorPort : public IPort<ProvidedInf, RequiredInf>
{
public:
	BehaviorPort(int type_, ES::StateMachineRef owner_) : type(type_), owner(owner_) {}
	virtual ~BehaviorPort() {}
	int getType() const { return type; }
protected:

	int type;
	ES::StateMachineRef owner;
};

struct IConnection
{
	virtual void fowardSendedMessageToConnectedPort(ES::EventRef signal) = 0;

};

template <typename ProvidedInf, typename RequiredInf>
struct AssemblyConnection : public IConnection
{
	AssemblyConnection (IPort<ProvidedInf, RequiredInf> * port_) : port(port_) {}
	virtual void fowardSendedMessageToConnectedPort (ES::EventRef signal)
	{
		port->reciveAny(signal);
	}
	
private:
	IPort<ProvidedInf,RequiredInf> * port;
};

template <typename ProvidedInf, typename RequiredInf>
struct DelegationConnection : public IConnection
{

	DelegationConnection (Port<ProvidedInf, RequiredInf> * port_) : port(port_) {}

	virtual void fowardSendedMessageToConnectedPort (ES::EventRef signal)
	{
		port->sendAny(signal);
	}
	
private:
	Port<ProvidedInf , RequiredInf> * port;
};

template<typename Inf1, typename Inf2>
void connect(IPort<Inf1, Inf2> * p1, IPort <Inf2, Inf1> * p2)
{
	p1->setAssemblyConnectedPort(p2);
	p2->setAssemblyConnectedPort(p1);
}

template<typename Inf1, typename Inf2>
void connect(IPort<Inf1, Inf2> * p1, Port <Inf1, Inf2> * p2)
{
	p1->setDelgationConnectedPort(p2);
	p2->setInnerConnection(p1);
}


template <typename ProvidedInf, typename RequiredInf>
class BehaviorPortImpl : public BehaviorPort <ProvidedInf, RequiredInf>
{
    public:
		BehaviorPortImpl (int type_, ES::StateMachineRef parent_) : BehaviorPort <RequiredInf, ProvidedInf> (type_,parent_) {}
		virtual ~BehaviorPortImpl() {}
    protected:
        virtual void sendAny (ES::EventRef signal)
        {
			BehaviorPort <RequiredInf, ProvidedInf>::connectedPort->fowardSendedMessageToConnectedPort(signal);
        }

        virtual void reciveAny (ES::EventRef signal)
        {
			Model::EventBase* realEvent = static_cast<Model::EventBase*>(signal.get());
			realEvent->p = BehaviorPort <RequiredInf, ProvidedInf>::type;
			BehaviorPort <RequiredInf, ProvidedInf>::owner->send(signal);
        }


};

template <typename ProvidedInf, typename RequiredInf>
class PortImpl : public Port <ProvidedInf, RequiredInf>
{
public:
	PortImpl() : Port <RequiredInf, ProvidedInf>() {}
	virtual ~PortImpl() {}


protected:
	virtual void sendAny(ES::EventRef signal)
	{
		Port <RequiredInf, ProvidedInf>::connectedPort->fowardSendedMessageToConnectedPort(signal);
	}

	virtual void reciveAny(ES::EventRef signal)
	{
		if (Port <RequiredInf, ProvidedInf>::connectionToInnerPort != nullptr)
		{
			Port <RequiredInf, ProvidedInf>::connectionToInnerPort->reciveAny(signal);
		}
	}

};

template <typename ProvidedInf, typename RequiredInf>
void IPort<ProvidedInf,RequiredInf>::setAssemblyConnectedPort (IPort<RequiredInf,ProvidedInf> * connectedPort_) {
	connectedPort = new AssemblyConnection<RequiredInf,ProvidedInf>(connectedPort_);
}

template <typename ProvidedInf, typename RequiredInf>
void IPort<ProvidedInf,RequiredInf>::setDelgationConnectedPort (Port<ProvidedInf,RequiredInf> * connectedPort_) {
		connectedPort = new DelegationConnection<ProvidedInf,RequiredInf>(connectedPort_);
}
}

#endif // PORT_HPP
