#include <atomic>
#include <functional>
#include <complex> 

#include "threadpool.hpp"

namespace Execution
{
StateMachineThreadPool::StateMachineThreadPool() :
	_sharedConditionVar(new std::condition_variable()),
	_stateMachines(this),
	_stop(true) {}

void StateMachineThreadPool::stopPool()
{
	_stop = true;
	_stateMachines.stopQueue();

}

void StateMachineThreadPool::startPool(unsigned n)
{
	_stateMachines.startQueue();
	_stop = false;
	for (unsigned i = 0; i < n; ++i)
	{
		workers.push_back(std::thread(&StateMachineThreadPool::task, this));
	}

}

void StateMachineThreadPool::stopUponCompletion()
{
	std::unique_lock<std::mutex> lock(stop_request_mu);

	if (!(nOfAllMessages->isZeroCounter() && nOfWorkerThreads->isZeroCounter()))
	{
		stop_request_cond->wait(lock, [this] {return nOfAllMessages->isZeroCounter() && nOfWorkerThreads->isZeroCounter(); });
	}

	stopPool();

}

void StateMachineThreadPool::task()
{
	while (!this->_stop)
	{

		ES::StateMachineRef sm = nullptr;
		while (sm == nullptr && !this->_stop)
		{

			_stateMachines.dequeue(sm);
		}


		if (sm != nullptr)
		{
			incrementWorkers();
			bool validSM = true;
			for (int i = 0; i < 5 && !sm->emptyMessageQueue(); ++i)
			{
				validSM = sm->processNextEvent();
				if (!validSM) {
					break;
				}
			}

			if (validSM) {
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
	for (std::thread& worker : workers) {
		worker.join();
	}
	workers.clear();
}

PoolQueueType::PoolQueueType(StateMachineThreadPool * ownerPool_) : 
	ES::ThreadSafeQueue<ES::Queue<ES::StateMachineRef>>(ownerPool_->_sharedConditionVar),
	ownerPool (ownerPool_)
{

}


bool PoolQueueType::exitFromWaitingCondition()
{
	return true; //TODO porpuse?
}

}


