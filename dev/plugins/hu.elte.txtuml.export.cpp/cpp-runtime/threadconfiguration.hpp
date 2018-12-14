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

	Configuration(ThreadPoolPtr threadPool, double rate) :
		_threadPool(threadPool), _rate(rate) {}
	virtual ~Configuration() {}

	ThreadPoolPtr getThreadPool() const { return _threadPool; }
	double getRate() const { return _rate; }

private:

	ThreadPoolPtr _threadPool;
	double _rate;
	unsigned _max;
};

class ThreadPoolConfigurationStore {
public:
	ThreadPoolConfigurationStore() {}
	ThreadPoolConfigurationStore(unsigned nOfAllThread_, std::vector<Configuration> configurations_) :
		nOfAllThreads(nOfAllThread_),
		configurations(configurations_) {}

	const std::vector<Configuration>& getConfigurations() const { return configurations; };
	unsigned getNOfThreads() const { return nOfAllThreads; }

private:
	unsigned nOfAllThreads;
	std::vector<Configuration> configurations;

};

}


#endif
