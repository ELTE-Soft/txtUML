#ifndef THREAD_CONFIGURATION_HPP
#define THREAD_CONFIGURATION_HPP

#include <vector>
#include "threadpool.hpp"
#include "math.h"

struct LinearFunction
{
	public:
		LinearFunction(int constant,double gradient) : 
			_constant(constant),
			_gradient(gradient) 
		{
		}

		int operator()(int n)
		{
			return (int)round(_gradient * n) + _constant;
		}
	private:
		int _constant;
		double _gradient;
};

struct Configuration
{

	ES::SharedPtr<StateMachineThreadPool> threadPool;
	ES::SharedPtr<LinearFunction> function;
	int max;

	Configuration(ES::SharedPtr<StateMachineThreadPool> threadPool,ES::SharedPtr<LinearFunction> function,int max)
	{
		this->threadPool = threadPool;
		this->function = function;
		this->max = max;
	}
	virtual ~Configuration()
	{
	}

};



class ThreadConfiguration
{
	public:

		ThreadConfiguration(int);
		~ThreadConfiguration();

		void insertConfiguration(int,ES::SharedPtr<Configuration>);
		ES::SharedPtr<StateMachineThreadPool> getThreadPool(int);
		ES::SharedPtr<LinearFunction> getFunction(int);
		int getMax(int);
		int getNumberOfConfigurations();

	private:

		std::vector<ES::SharedPtr<Configuration>> configurations;

};

#endif
