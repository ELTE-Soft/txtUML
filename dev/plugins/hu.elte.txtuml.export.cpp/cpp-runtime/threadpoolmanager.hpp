/** @file threadpoolmaneger.hpp
*/

#ifndef THEAD_POOL_MANAGER_H
#define THEAD_POOL_MANAGER_H

#include <map>
#include <list>
#include <math.h>
#include <array>

#include "threadpool.hpp"
#include "threadconfiguration.hpp"
#include "ESRoot/Types.hpp"

namespace Execution 
{

/*! Manages the state of thread pools. */
template<int NC>
class ThreadPoolManager
{
public:
	ThreadPoolManager();
	~ThreadPoolManager();
	ES::SharedPtr<StateMachineThreadPool> 	getPool(int);
	void 									recalculateThreads(int, int);
	int 									calculateNOfThreads(int, int);
	void 									enqueueObject(ES::StateMachineRef);
	void 									setConfiguration(std::array<ES::SharedPtr<Configuration>, NC>);
	bool 									isConfigurated();

private:
	bool configured;
	std::array<ES::SharedPtr<Configuration>, NC> configurations;

};

template<int NC>
ThreadPoolManager<NC>::ThreadPoolManager () : configured (false) {}

template<int NC>
void ThreadPoolManager<NC>::recalculateThreads (int id, int n)
{
	if (!isConfigurated ()) {
		abort ();
	}

	configurations[id]->getThreadPool ()->modifyThreads (calculateNOfThreads (id, n));
}

template<int NC>
int ThreadPoolManager<NC>::calculateNOfThreads (int id, int n)
{
	if (!isConfigurated ()) {
		abort ();
	}

	LinearFunction function = *(configurations[id]->getFunction ());
	int max = configurations[id]->getMax ();
	return std::min (function (n), max);

}

template<int NC>
void ThreadPoolManager<NC>::enqueueObject (ES::StateMachineRef sm)
{
	if (!isConfigurated ()) {
		abort ();
	}

	int objectId = sm->getPoolId ();
	configurations[objectId]->getThreadPool ()->enqueueObject (sm);
}

template<int NC>
ES::SharedPtr<StateMachineThreadPool> ThreadPoolManager<NC>::getPool (int id)
{
	if (!isConfigurated ()) {
		abort ();
	}

	return configurations[id]->getThreadPool ();
}

template<int NC>
void ThreadPoolManager<NC>::setConfiguration (std::array<ES::SharedPtr<Configuration>, NC> configurations)
{
	this->configurations = configurations;
	configured = true;
}

template<int NC>
bool ThreadPoolManager<NC>::isConfigurated ()
{
	return configured;
}

template<int NC>
ThreadPoolManager<NC>::~ThreadPoolManager ()
{
}


}


#endif // THEAD_POOL_MANAGER_H
