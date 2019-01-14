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

void SingleThreadRT::setConfiguration(const std::vector<Configuration>& /*conf*/) {}

void SingleThreadRT::stopUponCompletion() {}

// ConfiguredThreadedRT

ConfiguredThreadedRT::ConfiguredThreadedRT() :
	worker(new ES::AtomicCounter()),
	messages(new ES::AtomicCounter()) {}

ConfiguredThreadedRT::~ConfiguredThreadedRT() {}

void ConfiguredThreadedRT::start()
{
	for (unsigned i = 0; i < threadConfig.getConfigurations().size(); i++)
	{
		Configuration config = configurations[i];
		ThreadPoolPtr pool = config.getThreadPool();
		pool->setWorkersCounter(worker);
		pool->setMessageCounter(messages);
		pool->setStopReqest(&stop_request_cond);

		pool->startPool(config.getNumberOfExecutors ());
	}
}

void ConfiguredThreadedRT::stopUponCompletion()
{
	for (unsigned i = 0; i < threadConfig.getConfigurations().size(); i++)
	{
		Configuration config = configurations[i];
		ThreadPoolPtr pool = config.getThreadPool();
		pool->stopUponCompletion();
	}
}

void ConfiguredThreadedRT::setupObjectSpecificRuntime(ES::StateMachineRef sm)
{

	sm->setMessageCounter(messages);
	unsigned objectId = sm->getPoolId ();
	Configuration config = configurations[objectId];
	ThreadPoolPtr matchedPool = config.getThreadPool ();
	sm->setPool (matchedPool);
}


void ConfiguredThreadedRT::setConfiguration(const std::vector<Configuration>& conf)
{
	configurations = conf;
}

}