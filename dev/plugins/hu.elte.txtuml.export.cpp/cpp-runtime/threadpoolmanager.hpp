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
		StateMachineThreadPool* getPool(int);
		void recalculateThreads(int,int);
		int calculateNOfThreads(int,int);
		void enqueueObject(IStateMachine*);
		int getNumberOfConfigurations();
		void setConfiguration(ES::Ref<ThreadConfiguration>);
		bool isConfigurated();
	
	private:
		ES::Ref<ThreadConfiguration> configuration;
	
};

#endif // THEAD_POOL_MANAGER_H
