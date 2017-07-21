#ifndef INTERFACE_UTILS_HPP
#define INTERFACE_UTILS_HPP

template <typename SendInf, typename ReciveInf>
class IntegratedInf
{
public:
	using RequiredInfType = SendInf;
	using ProvidedInfType = ReciveInf;
};

class EmptyReqInf {

	template <typename RequiredInf, typename ProvidedInf>
	friend struct DelegationConnection;

protected:
	virtual void sendAny(ES::EventRef s) = 0;
};

class EmptyProvInf {
protected:
	virtual void reciveAny(ES::EventRef s) = 0;
};

using EmptyInf = IntegratedInf<EmptyReqInf, EmptyProvInf>;

#endif