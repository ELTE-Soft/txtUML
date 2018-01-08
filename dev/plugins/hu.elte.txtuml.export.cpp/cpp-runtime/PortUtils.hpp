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
	BehaviorPort(int type_, ES::ModelObjectRef owner_) : type(type_), owner(owner_) {}
	virtual ~BehaviorPort() {}
	int getType() const { return type; }
protected:

	int type;
	ES::ModelObjectRef owner;
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
ES::IPortRef<typename LeftEnd::EdgeType, typename RightEnd::EdgeType> p1, 
ES::IPortRef <typename RightEnd::EdgeType, typename LeftEnd::EdgeType> p2)
{
	p1->setAssemblyConnectedPort(p2);
	p2->setAssemblyConnectedPort(p1);
}

template<typename ChildEnd, typename ParentRequired>
void delegateConnect(
ES::IPortRef<typename ChildEnd::EdgeType, ParentRequired> childPort,
ES::PortRef <typename ChildEnd::EdgeType, ParentRequired> parentPort)
{
	childPort->setDelgationConnectedPort(parentPort);
	parentPort->setInnerConnection(childPort);
}


template <typename ProvidedInf, typename RequiredInf>
class BehaviorPortImpl : public BehaviorPort <ProvidedInf, RequiredInf>
{
    public:
		BehaviorPortImpl (int type_, ES::ModelObjectRef parent_) : BehaviorPort <ProvidedInf, RequiredInf> (type_,parent_) {}
		virtual ~BehaviorPortImpl() {}
    protected:
        virtual void sendAny (ES::EventRef signal)
        {
			BehaviorPort <ProvidedInf, RequiredInf>::connectedPort->fowardSendedMessageToConnectedPort(signal);
        }

        virtual void reciveAny (ES::EventRef signal)
        {
			signal->setPortType(BehaviorPort <ProvidedInf, RequiredInf>::type);
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
