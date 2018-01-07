#include "istatemachine.hpp"
#include "ievent.hpp"
#include "ESRoot/Types.hpp"
#include "InterfaceUtils.hpp"
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
class DelegationConnection;

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

	void setAssemblyConnectedPort (ES::SharedPtr<IPort<RequiredInf,ProvidedInf>> connectedPort_);
	void setDelgationConnectedPort (ES::SharedPtr<Port<ProvidedInf,RequiredInf>> connectedPort_);

protected:
	IConnection * connectedPort;
};

template <typename ProvidedInf, typename RequiredInf>
class Port : public IPort<ProvidedInf, RequiredInf>
{
public:
	Port () {}
	virtual ~Port() {}
	void setInnerConnection(ES::SharedPtr<IPort<ProvidedInf, RequiredInf>> innerPort) { connectionToInnerPort = innerPort; }

	template <typename, typename>
	friend class DelegationConnection;
protected:
	
	ES::SharedPtr<IPort<ProvidedInf, RequiredInf>> connectionToInnerPort;


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
	AssemblyConnection (ES::SharedPtr<IPort<ProvidedInf, RequiredInf>> port_) : port(port_) {}
	virtual void fowardSendedMessageToConnectedPort (ES::EventRef signal)
	{
		port->reciveAny(signal);
	}
	
private:
	ES::SharedPtr<IPort<ProvidedInf,RequiredInf>> port;
};

template <typename ProvidedInf, typename RequiredInf>
class DelegationConnection : public IConnection
{
public:

	DelegationConnection (ES::SharedPtr<Port<ProvidedInf, RequiredInf>> port_) : port(port_) {}

	virtual void fowardSendedMessageToConnectedPort (ES::EventRef signal)
	{
		port->sendAny(signal);
	}
	
private:
	ES::SharedPtr<Port<ProvidedInf , RequiredInf>> port;
};

// TODO Handle create link actions uniformly
template<typename LeftEnd, typename RightEnd>
void assemblyConnect(
IPort<typename LeftEnd::EdgeType, typename RightEnd::EdgeType> * p1, 
IPort <typename RightEnd::EdgeType, typename LeftEnd::EdgeType> * p2)
{
	p1->setAssemblyConnectedPort(p2);
	p2->setAssemblyConnectedPort(p1);
}

template<typename LeftEnd, typename RightEnd>
void delegateConnect(
IPort<typename LeftEnd::EdgeType, typename RightEnd::EdgeType> * childPort, 
Port <typename LeftEnd::EdgeType, typename RightEnd::EdgeType> * parentPort)
{
	childPort->setDelgationConnectedPort(p2);
	parentPort->setInnerConnection(p1);
}


template <typename ProvidedInf, typename RequiredInf>
class BehaviorPortImpl : public BehaviorPort <ProvidedInf, RequiredInf>
{
    public:
		BehaviorPortImpl (int type_, ES::StateMachineRef parent_) : BehaviorPort <ProvidedInf, RequiredInf> (type_,parent_) {}
		virtual ~BehaviorPortImpl() {}
    protected:
        virtual void sendAny (ES::EventRef signal)
        {
			BehaviorPort <ProvidedInf, RequiredInf>::connectedPort->fowardSendedMessageToConnectedPort(signal);
        }

        virtual void reciveAny (ES::EventRef signal)
        {
			Model::EventBase* realEvent = static_cast<Model::EventBase*>(signal.get());
			realEvent->p = BehaviorPort <ProvidedInf, RequiredInf>::type;
			BehaviorPort <ProvidedInf, RequiredInf>::owner->send(signal);
        }


};

template <typename ProvidedInf, typename RequiredInf>
class PortImpl : public Port <ProvidedInf, RequiredInf>
{
public:
	PortImpl() : Port <ProvidedInf, RequiredInf>() {}
	virtual ~PortImpl() {}


protected:
	virtual void sendAny(ES::EventRef signal)
	{
		Port <ProvidedInf, RequiredInf>::connectedPort->fowardSendedMessageToConnectedPort(signal);
	}

	virtual void reciveAny(ES::EventRef signal)
	{
		if (Port <ProvidedInf, RequiredInf>::connectionToInnerPort != nullptr)
		{
			Port <ProvidedInf, RequiredInf>::connectionToInnerPort->reciveAny(signal);
		}
	}

};

template <typename ProvidedInf, typename RequiredInf>
void IPort<ProvidedInf,RequiredInf>::setAssemblyConnectedPort (ES::SharedPtr<IPort<RequiredInf,ProvidedInf> > connectedPort_) {
	connectedPort = new AssemblyConnection<RequiredInf,ProvidedInf>(connectedPort_);
}

template <typename ProvidedInf, typename RequiredInf>
void IPort<ProvidedInf,RequiredInf>::setDelgationConnectedPort (ES::SharedPtr<Port<ProvidedInf,RequiredInf> > connectedPort_) {
		connectedPort = new DelegationConnection<ProvidedInf,RequiredInf>(connectedPort_);
}
}

#endif // PORT_HPP
