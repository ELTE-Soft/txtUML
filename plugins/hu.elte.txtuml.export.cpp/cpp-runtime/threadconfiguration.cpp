#include "threadconfiguration.hpp"

ThreadConfiguration::ThreadConfiguration(int size)
{
	configurations.resize(size);
}

void ThreadConfiguration::insertConfiguration(int id,Configuration conf)
{
	configurations[id] = conf;
}

StateMachineThreadPool* ThreadConfiguration::getThreadPool(int id)
{
	return configurations[id].threadPool;
}

LinearFunction* ThreadConfiguration::getFunction(int id)
{
	return configurations[id].function;
}

int ThreadConfiguration::getMax(int id)
{
	return configurations[id].max;
}
