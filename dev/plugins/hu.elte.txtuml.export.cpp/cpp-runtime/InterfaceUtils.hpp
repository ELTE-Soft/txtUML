/** @file InterfaceUtils.hpp
Contains prewritten interface bases to create an integrated interface with required and provided part.
*/

#ifndef INTERFACE_UTILS_HPP
#define INTERFACE_UTILS_HPP

#include "ESRoot/Types.hpp"

template <typename SendInf, typename ReciveInf>
class IntegratedInf
{
public:
	using RequiredInfType = SendInf;
	using ProvidedInfType = ReciveInf;
};

class RequiredInterfaceBase {
protected:
	virtual void sendAny(ES::EventRef s) = 0;
};

class ProvidedInterfaceBase {
protected:
	virtual void receiveAny(ES::EventRef s) = 0;
};

class EmptyReqInf : public RequiredInterfaceBase {

};

class EmptyProvInf : public ProvidedInterfaceBase {
};

using EmptyInf = IntegratedInf<EmptyReqInf, EmptyProvInf>;

#endif
