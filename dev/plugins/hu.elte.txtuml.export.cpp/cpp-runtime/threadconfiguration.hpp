/** @file threadconfiguration.hpp
*/

#ifndef THREAD_CONFIGURATION_HPP
#define THREAD_CONFIGURATION_HPP

#include "threadpool.hpp"
#include "math.h"

namespace Execution
{

/*! Configuration parameters datastore. */
class Configuration
{
public:

	Configuration(ThreadPoolPtr threadPool, unsigned numberOfExecutors) :
		_threadPool(threadPool), _numberOfExecutors(numberOfExecutors) {}
	virtual ~Configuration() {}

	ThreadPoolPtr getThreadPool() const { return _threadPool; }
	unsigned getNumberOfExecutors() const { return _numberOfExecutors; }

private:

	ThreadPoolPtr _threadPool;
	unsigned _numberOfExecutors;
};

}


#endif
