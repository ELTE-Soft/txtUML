#include <atomic>
#include <functional>
#include <complex> 

#include "threadpool.hpp"
namespace Execution 
{
StateMachineThreadPool::StateMachineThreadPool() : 
_sharedConditionVar(new std::condition_variable()), 
_stateMachines(_sharedConditionVar),
_stop(true) {}

void StateMachineThreadPool::stopPool()
{
	_stop = true;
	_stateMachines.stopQueue();

}

void StateMachineThreadPool::startPool(int n)
{
	_stateMachines.startQueue();
	_stop = false;
	modifyThreads(n);
	for(int i = 0; i < n; ++i)
	{
		workers.addThread(new std::thread(&StateMachineThreadPool::task, this));
	}

}

void StateMachineThreadPool::stopUponCompletion()
{
	std::unique_lock<std::mutex> lock(stop_request_mu);

	if (!(nOfAllMessages->isZeroCounter () && nOfWorkerThreads->isZeroCounter ()))
	{
		stop_request_cond->wait(lock, [this] {return nOfAllMessages->isZeroCounter() && nOfWorkerThreads->isZeroCounter(); });
	}

	stopPool();

}

void StateMachineThreadPool::task()
{
	while (!this->_stop && !workers.isReadyToStop(std::this_thread::get_id()))
	{
		if (modifie_mutex.try_lock()) 
		{
			if (workers.isTooManyWorkers())
			{
				workers.gettingThreadsReadyToStop(_sharedConditionVar);
			}
			while (workers.isTooFewWorkers())
			{
				workers.addThread(new std::thread(&StateMachineThreadPool::task, this));
			}
			modifie_mutex.unlock();
		}

		ES::StateMachineRef sm = nullptr;
		while (sm == nullptr && !this->_stop)
		{

			if (workers.isReadyToStop(std::this_thread::get_id()))
			{
				return;
			}

			_stateMachines.dequeue(sm);
			
		}


		if (sm != nullptr)
		{
			incrementWorkers();
			bool hasNextEvent = true;
			for (int i = 0; i < 5 && hasNextEvent; ++i)
			{
				hasNextEvent = sm->processNextEvent();
			}


			if (hasNextEvent)
			{
				_stateMachines.enqueue(sm);
			}
			else
			{
				sm->setPooled(false);
			}

			reduceWorkers();
			stop_request_cond->notify_one();
		}

	}

}

void StateMachineThreadPool::modifyThreads(int n)
{
	if (!_stop)
	{
		workers.setExpectedThreads(n);
	}
}


void StateMachineThreadPool::incrementWorkers()
{
	nOfWorkerThreads->incrementCounter();

}

void StateMachineThreadPool::reduceWorkers()
{
	nOfWorkerThreads->decrementCounter();
}

void StateMachineThreadPool::enqueueObject(ES::StateMachineRef sm)
{
	_stateMachines.enqueue(sm);
}

StateMachineThreadPool::~StateMachineThreadPool()
{
	stopPool();
	workers.removeAll(); // wait to all threads
}
}

