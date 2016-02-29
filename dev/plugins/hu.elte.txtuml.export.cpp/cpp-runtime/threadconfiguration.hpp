#ifndef THREAD_CONFIGURATION_HPP
#define THREAD_CONFIGURATION_HPP

#include <vector>
#include "threadpool.hpp"
#include "math.h"

struct LinearFunction
{
	public:
		LinearFunction(int constant_,double gradient_): constant(constant_),gradient(gradient_) {}
		int operator()(int n) {return (int)round(gradient*n) + constant;}
	private:
		int constant;
		double gradient;
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
