/** @file threadconfiguration.hpp
*/

#ifndef THREAD_CONFIGURATION_HPP
#define THREAD_CONFIGURATION_HPP

#include "threadpool.hpp"
#include "math.h"
namespace Execution
{

/*! Represents a linear function. */
class LinearFunction
{
public:
	LinearFunction(unsigned constant, double gradient) :
		_constant(constant),
		_gradient(gradient) {}

	unsigned operator()(unsigned n)
	{
		return (unsigned)round(_gradient * n) + _constant;
	}
private:
	unsigned _constant;
	double _gradient;
};


/*! Configuration parameters datastore. */
class Configuration
{
public:

	Configuration(ES::SharedPtr<StateMachineThreadPool> threadPool, ES::SharedPtr<LinearFunction> function, unsigned max) :
		_threadPool(threadPool), _function(function), _max(max) {}
	virtual ~Configuration() {}

	ES::SharedPtr<StateMachineThreadPool> getThreadPool() const { return _threadPool; }
	ES::SharedPtr<LinearFunction> getFunction() const { return _function; }
	unsigned getMax() { return _max; }

private:

	ES::SharedPtr<StateMachineThreadPool> _threadPool;
	ES::SharedPtr<LinearFunction> _function;
	unsigned _max;
};

}


#endif
