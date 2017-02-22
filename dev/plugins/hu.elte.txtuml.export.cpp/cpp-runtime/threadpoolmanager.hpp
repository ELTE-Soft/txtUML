#ifndef THEAD_POOL_MANAGER_H
#define THEAD_POOL_MANAGER_H

#include <map>
#include <list>
#include <math.h>

#include "threadpool.hpp"
#include "runtimetypes.hpp"
#include "threadconfiguration.hpp"
#include "ESRoot\Types.hpp"

class ThreadPoolManager
{

	public:
		ThreadPoolManager();
		~ThreadPoolManager();
		ES::SharedPtr<StateMachineThreadPool> getPool(int);
		void recalculateThreads(int,int);
		int calculateNOfThreads(int,int);
		void enqueueObject(ES::StateMachineRef);
		int getNumberOfConfigurations();
		void setConfiguration(ES::SharedPtr<ThreadConfiguration>);
		bool isConfigurated();
	
	private:
		ES::SharedPtr<ThreadConfiguration> configuration;
	
};

#endif // THEAD_POOL_MANAGER_H
