#include "threadpoolmanager.hpp"

ThreadPoolManager::ThreadPoolManager() : configuration(nullptr) {}

void ThreadPoolManager::recalculateThreads(int id,int n)
{
	if (!isConfigurated()) {
		abort();
	}
	
	LinearFunction function = *(configuration->getFunction(id));
	int max = configuration->getMax(id);
	if (function(n) < max) {
		configuration->getThreadPool(id)->modifiedThreads(function(n));
	}
}

void ThreadPoolManager::enqueObject(StateMachineI* sm)
{
	if (!isConfigurated()) {
		abort();
	}
	
	int objectId = sm->getPoolId();
	configuration->getThreadPool(objectId)->enqueObject(sm);
}

int ThreadPoolManager::getNumberOfConfigurations()
{
	if (!isConfigurated()) {
		abort();
	}
	
	return ((int)configuration->getNumberOfConfigurations());
}

StateMachineThreadPool* ThreadPoolManager::getPool(int id)
{
	if (!isConfigurated()) {
		abort();
	}
	
	return configuration->getThreadPool(id);
}

ThreadPoolManager::~ThreadPoolManager()
{
	if(isConfigurated())
		delete configuration;
}

void ThreadPoolManager::setConfiguration(ThreadConfiguration* configuration)
{
	this->configuration = configuration;
}

bool ThreadPoolManager::isConfigurated()
{
	return configuration != nullptr;
}

