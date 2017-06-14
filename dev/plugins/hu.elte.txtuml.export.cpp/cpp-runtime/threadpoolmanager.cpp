#include "threadpoolmanager.hpp"
#include "threadconfiguration.hpp"

#include <stdlib.h>
#include <algorithm> 
namespace Execution 
{

ThreadPoolManager::ThreadPoolManager() : configured(false) {}

void ThreadPoolManager::recalculateThreads(int id, int n)
{
	if (!isConfigurated()) {
		abort();
	}

	configurations[id]->getThreadPool()->modifyThreads(calculateNOfThreads(id, n));
}

int ThreadPoolManager::calculateNOfThreads(int id, int n)
{
	if (!isConfigurated()) {
		abort();
	}

	LinearFunction function = *(configurations[id]->getFunction());
	int max = configurations[id]->getMax();
	return std::min(function(n), max);

}

void ThreadPoolManager::enqueueObject(ES::StateMachineRef sm)
{
	if (!isConfigurated()) {
		abort();
	}

	int objectId = sm->getPoolId();
	configurations[objectId]->getThreadPool()->enqueueObject(sm);
}

int ThreadPoolManager::getNumberOfConfigurations()
{
	if (!isConfigurated()) {
		abort();
	}

	return (configurations.getSize());
}

ES::SharedPtr<StateMachineThreadPool> ThreadPoolManager::getPool(int id)
{
	if (!isConfigurated()) {
		abort();
	}

	return configurations[id]->getThreadPool();
}

void ThreadPoolManager::setConfiguration(ESContainer::FixedArray<ES::SharedPtr<Configuration>> configurations)
{
	this->configurations = configurations;
	configured = true;
}

bool ThreadPoolManager::isConfigurated()
{
	return configured;
}

ThreadPoolManager::~ThreadPoolManager()
{
}

}


