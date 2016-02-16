#ifndef THREAD_CONFIGURATION_HPP
#define THREAD_CONFIGURATION_HPP

#include <vector>
#include "threadpool.hpp"
#include "math.h"

struct LinearFunction
{
	public:
		LinearFunction(double gradient_,int constant_): gradient(gradient_),constant(constant_) {}
		int operator()(int n) {return  round(gradient*n + constant);}
	private:
		double gradient;
		int constant;
};

struct Configuration
{

	StateMachineThreadPool* threadPool;
	LinearFunction* function;
	int max;

	Configuration(StateMachineThreadPool* threadPool,LinearFunction* function,int max)
	{
		this->threadPool = threadPool;
		this->function = function;
		this->max = max;
	}
	~Configuration()
	{
		delete threadPool;
		delete function;
	}

};



class ThreadConfiguration
{
	public:

		ThreadConfiguration(int);
		~ThreadConfiguration();

		void insertConfiguration(int,Configuration*);
		StateMachineThreadPool* getThreadPool(int);
		LinearFunction* getFunction(int);
		int getMax(int);
		int getNumberOfConfigurations();

	private:

		std::vector<Configuration*> configurations;

};

#endif
