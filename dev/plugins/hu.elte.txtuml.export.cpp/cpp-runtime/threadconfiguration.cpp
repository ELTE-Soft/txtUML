#include "threadconfiguration.hpp"

ThreadConfiguration::ThreadConfiguration(int size)
{
	configurations.resize((size_t)size);
}


void ThreadConfiguration::insertConfiguration(int id,ES::SharedPtr<Configuration> conf)
{
        configurations[(size_t)id] = conf;
}

ES::SharedPtr<StateMachineThreadPool> ThreadConfiguration::getThreadPool(int id)
{
    return configurations[(size_t)id]->threadPool;
}

ES::SharedPtr<LinearFunction> ThreadConfiguration::getFunction(int id)
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
}
