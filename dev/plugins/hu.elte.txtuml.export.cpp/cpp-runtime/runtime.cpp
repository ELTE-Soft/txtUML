#include "runtime.hpp"
#include "StateMachineOwner.hpp"
#include "ESRoot/Types.hpp"
#include "ievent.hpp"

#include <assert.h>
#include <stdlib.h>


//********************************SingleThreadRT**********************************

namespace Execution {

SingleThreadRT::SingleThreadRT() :_messageQueue(new ES::MessageQueueType()) {}

void SingleThreadRT::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{
	sm->setMessageQueue(_messageQueue);
	sm->setMessageCounter(ES::SharedPtr<ES::AtomicCounter>(new ES::AtomicCounter()));
}

SingleThreadRT::~SingleThreadRT()
{
}

bool SingleThreadRT::isConfigurated()
{
	return true;
}

void SingleThreadRT::start()
{

	while (!_messageQueue->isEmpty())
	{
		ES::EventRef e = _messageQueue->next();
		if (Model::IEvent<Model::EventBase>::eventIsValid(e)) {
			const ES::StateMachineRef sm = e->getTargetSM();
			sm->processNextEvent();
		}
		else {
			_messageQueue->dequeue(e); // drop event
		}


	}

}

void SingleThreadRT::setConfiguration(std::array<ES::SharedPtr<Configuration>, 0>) {}

void SingleThreadRT::stopUponCompletion() {}

void SingleThreadRT::removeObject(ES::StateMachineRef) {}



}


// Constants

int Model::PortType::portIdCounter = 1;
Model::PortType Model::PortType::AnyPort;

