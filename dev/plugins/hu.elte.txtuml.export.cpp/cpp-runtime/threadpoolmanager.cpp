#include "threadpoolmanager.hpp"
#include <stdlib.h>
#include <algorithm> 

ThreadPoolManager::ThreadPoolManager() : configuration(nullptr) {}

void ThreadPoolManager::recalculateThreads(int id,int n)
{
	if (!isConfigurated()) {
		abort();
	}
	
	configuration->getThreadPool(id)->modifiedThreads(calculateNOfThreads(id,n));
}

int ThreadPoolManager::calculateNOfThreads(int id, int n)
{
	if (!isConfigurated()) {
		abort();
	}
	
	LinearFunction function = *(configuration->getFunction(id));
	int max = configuration->getMax(id);
	return std::min(function(n),max);
	
}

void ThreadPoolManager::enqueueObject(IStateMachine* sm)
{
	if (!isConfigurated()) {
		abort();
	}
	
	int objectId = sm->getPoolId();
	configuration->getThreadPool(objectId)->enqueueObject(sm);
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

