#include "runtime.hpp"


// Constants

int Model::PortType::portIdCounter = 1;
Model::PortType Model::PortType::AnyPort;

namespace Execution {
// SingleThreadRT
void SingleThreadRT::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{
	sm->setMessageQueue(_messageQueue);
	sm->setMessageCounter(ES::SharedPtr<ES::AtomicCounter>(new ES::AtomicCounter()));
}

SingleThreadRT::~SingleThreadRT()
{
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

void SingleThreadRT::setConfiguration(const ThreadPoolConfigurationStore& /*threadPools*/) {}

void SingleThreadRT::stopUponCompletion() {}

void SingleThreadRT::removeObject(ES::StateMachineRef) {}

// ConfiguredThreadedRT

ConfiguredThreadedRT::ConfiguredThreadedRT() :
	worker(new ES::AtomicCounter()),
	messages(new ES::AtomicCounter()) {}

ConfiguredThreadedRT::~ConfiguredThreadedRT() {}

void ConfiguredThreadedRT::start()
{
	for (unsigned i = 0; i < threadPools.getConfigurations().size(); i++)
	{
		Configuration config = threadPools.getConfigurations()[i];
		ES::SharedPtr<StateMachineThreadPool> pool = config.getThreadPool();
		pool->setWorkersCounter(worker);
		pool->setMessageCounter(messages);
		pool->setStopReqest(&stop_request_cond);
		pool->startPool(unsigned((double)threadPools.getNOfThreads() / config.getRate()));
	}
}

void ConfiguredThreadedRT::removeObject(ES::StateMachineRef sm)
{

}

void ConfiguredThreadedRT::stopUponCompletion()
{
	for (unsigned i = 0; i < threadPools.getConfigurations().size(); i++)
	{
		Configuration config = threadPools.getConfigurations()[i];
		ThreadPoolPtr pool = config.getThreadPool();
		pool->stopUponCompletion();

	}
}

void ConfiguredThreadedRT::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{

	sm->setMessageCounter(messages);
}


void ConfiguredThreadedRT::setConfiguration(const ThreadPoolConfigurationStore& conf)
{
	threadPools = conf;
}

}