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
	typedef typename std::array<ES::SharedPtr<Configuration>, NC>::size_type id_type;
public:
	ThreadPoolManager();
	~ThreadPoolManager();
	ES::SharedPtr<StateMachineThreadPool> 	getPool(id_type);
	void 									recalculateThreads(id_type, unsigned);
	unsigned 								calculateNOfThreads(id_type, unsigned);
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
void ThreadPoolManager<NC>::recalculateThreads (id_type id, unsigned n)
{
	if (!isConfigurated ()) {
		abort ();
	}

	configurations[id]->getThreadPool ()->modifyThreads (calculateNOfThreads (id, n));
}

template<int NC>
unsigned ThreadPoolManager<NC>::calculateNOfThreads (id_type id, unsigned n)
{
	if (!isConfigurated ()) {
		abort ();
	}

	LinearFunction function = *(configurations[id]->getFunction ());
	unsigned max = configurations[id]->getMax ();
	return std::min<unsigned> (function (n), max);

}

template<int NC>
void ThreadPoolManager<NC>::enqueueObject (ES::StateMachineRef sm)
{
	if (!isConfigurated ()) {
		abort ();
	}

	int objectId = sm->getPoolId ();
	configurations[(id_type)objectId]->getThreadPool ()->enqueueObject (sm);
}

template<int NC>
ES::SharedPtr<StateMachineThreadPool> ThreadPoolManager<NC>::getPool (id_type id)
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
