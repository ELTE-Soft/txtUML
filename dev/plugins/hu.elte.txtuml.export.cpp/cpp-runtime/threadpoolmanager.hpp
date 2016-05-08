#ifndef THEAD_POOL_MANAGER_H
#define THEAD_POOL_MANAGER_H

#include <map>
#include <list>
#include <math.h>

#include "threadpool.hpp"
#include "runtimetypes.hpp"
#include "threadconfiguration.hpp"

class ThreadPoolManager
{

	public:
		ThreadPoolManager();
		~ThreadPoolManager();
		StateMachineThreadPool* getPool(int);
		void recalculateThreads(int,int);
		void enqueObject(StateMachineI*);
		int getNumberOfConfigurations();
		void setConfiguration(ThreadConfiguration*);
		bool isConfigurated();
	
	private:
		ThreadConfiguration* configuration;
	
};

#endif // THEAD_POOL_MANAGER_H
