/** @file threadpoolmaneger.hpp
*/

#ifndef THEAD_POOL_MANAGER_H
#define THEAD_POOL_MANAGER_H

#include <map>
#include <list>
#include <math.h>

#include "threadpool.hpp"
#include "threadconfiguration.hpp"
#include "ESRoot/Types.hpp"
#include "ESRoot/Containers/FixedArray.hpp"

namespace Execution 
{

/*! Manages the state of thread pools. */
class ThreadPoolManager
{
public:
	ThreadPoolManager();
	~ThreadPoolManager();
	ES::SharedPtr<StateMachineThreadPool> 	getPool(int);
	void 									recalculateThreads(int, int);
	int 									calculateNOfThreads(int, int);
	void 									enqueueObject(ES::StateMachineRef);
	int 									getNumberOfConfigurations();
	void 									setConfiguration(ESContainer::FixedArray<ES::SharedPtr<Configuration>>);
	bool 									isConfigurated();

private:
	bool configured;
	ESContainer::FixedArray<ES::SharedPtr<Configuration>> configurations;

};

}


#endif // THEAD_POOL_MANAGER_H
