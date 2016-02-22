#include "threadconfiguration.hpp"

ThreadConfiguration::ThreadConfiguration(int size)
{
	configurations.resize((size_t)size);
}


void ThreadConfiguration::insertConfiguration(int id,Configuration* conf)
{
	configurations[(size_t)id] = conf;
}

StateMachineThreadPool* ThreadConfiguration::getThreadPool(int id)
{
    return configurations[(size_t)id]->threadPool;
}

LinearFunction* ThreadConfiguration::getFunction(int id)
{

	return configurations[(size_t)id]->function;
}

int ThreadConfiguration::getMax(int id)
{

	return configurations[(size_t)id]->max;

}

int ThreadConfiguration::getNumberOfConfigurations()
{
	return (int)configurations.size();
}

ThreadConfiguration::~ThreadConfiguration()
{
	for (std::vector<Configuration*>::iterator it = configurations.begin(); it != configurations.end(); it++)
	{
		delete (*it);
	}
}
