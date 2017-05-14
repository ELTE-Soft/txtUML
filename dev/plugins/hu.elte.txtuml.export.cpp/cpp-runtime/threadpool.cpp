#include <atomic>
#include <functional>
#include <complex> 

#include "threadpool.hpp"
namespace Execution 
{
StateMachineThreadPool::StateMachineThreadPool() :_stop(true), _sharedConditionVar(new std::condition_variable()), _stateMachines(_sharedConditionVar) {}

void StateMachineThreadPool::stopPool()
{
	_stop = true;
	_stateMachines.stopQueue();

}

void StateMachineThreadPool::startPool(int n)
{
	_stateMachines.startQueue();
	_stop = false;
	modifiedThreads(n);
	for(int i = 0; i < n; ++i)
	{
		workers.addThread(new std::thread(&StateMachineThreadPool::task, this));
	}

}

void StateMachineThreadPool::stopUponCompletion(std::atomic_int* messages)
{
	std::unique_lock<std::mutex> lock(stop_request_mu);

	if (!((*messages) == 0 && (*worker_threads) == 0))
	{
		stop_request_cond->wait(lock, [this, messages] {return (*messages) == 0 && *(this->worker_threads) == 0; });
	}

	stopPool();

}

void StateMachineThreadPool::task()
{
	while (!this->_stop && !workers.isReadyToStop(std::this_thread::get_id()))
	{
		if (modifie_mutex.try_lock()) {
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

			if (!sm->isInitialized()) sm->init();
			for (int i = 0; i < 5 && sm != nullptr && !sm->emptyMessageQueue(); ++i)
			{
				if (!sm->isDestoryed()) {
					sm->processEventVirtual();
				}
				else {
					sm->setPooled(false);
					delete sm;
					sm = nullptr;
				}
			}

			if (sm != nullptr) {

				if (!sm->emptyMessageQueue())
				{
					_stateMachines.enqueue(sm);
				}
				else
				{
					sm->setPooled(false);
				}
			}

			reduceWorkers();
			stop_request_cond->notify_one();
		}

	}

}

void StateMachineThreadPool::modifiedThreads(int n)
{
	if (!_stop)
	{
		workers.setExpectedThreads(n);
	}
}


void StateMachineThreadPool::incrementWorkers()
{
	(*worker_threads)++;

}

void StateMachineThreadPool::reduceWorkers()
{
	(*worker_threads)--;
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

