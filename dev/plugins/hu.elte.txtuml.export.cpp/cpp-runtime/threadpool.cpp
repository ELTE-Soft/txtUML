#include <atomic>
#include <functional>
#include <complex> 

#include "threadpool.hpp"

StateMachineThreadPool::StateMachineThreadPool():stop (true) {}
	
void StateMachineThreadPool::stopPool()
{
	stop = true;
	stateMachines.stopQueue ();
	
}

void StateMachineThreadPool::startPool(int n)
{
	stateMachines.startQueue ();
	stop = false;
	modifiedThreads(n);
        
}

void StateMachineThreadPool::stopUponCompletion(std::atomic_int* messages)
{
	std::unique_lock<std::mutex> lock(stop_request_mu);
	
	if (!( (*messages) == 0 && (*worker_threads) == 0))
	{
		stop_request_cond->wait(lock, [this,messages] {return (*messages) == 0 && *(this->worker_threads) == 0; });
	}

	stopPool();

}

void StateMachineThreadPool::task()
{	
	while (!this->stop && !workers.isReadyToStop(std::this_thread::get_id()))
    	{
		

		
		ES::StateMachineRef sm = nullptr;
		while(sm == nullptr && !this->stop)
		{			
							
			if (workers.isReadyToStop(std::this_thread::get_id()))
			{
				return;
			}

			stateMachines.dequeue(sm);
			incrementWorkers();
		}
		
	
		if (sm != nullptr)
		{
			
			if (!sm->isInitialized()) sm->init();
			for (int i = 0; i < 5 && !sm->emptyMessageQueue(); ++i)
			{
				sm->processEventVirtual();
			}
					
			
			if (!sm->emptyMessageQueue())
			{
				stateMachines.enqueue(sm);
			}
			else
			{
				sm->setPooled (false);
			}
			
			reduceWorkers ();
			stop_request_cond->notify_one ();			
		}
					
    }
	
}

void StateMachineThreadPool::modifiedThreads (int n)
{
	if (!stop) 
	{
		std::unique_lock<std::mutex> mlock(modifie_mutex);
		
		workers.setExpectedThreads (n);
		if (workers.isTooManyWorkers())
		{
			workers.gettingThreadsReadyToStop(cond);
		}
		while (workers.isTooFewWorkers ())
		{
			workers.addThread (new std::thread(&StateMachineThreadPool::task,this));
		}
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
        stateMachines.enqueue(sm);
}

StateMachineThreadPool::~StateMachineThreadPool()
{
    	stopPool();
	workers.removeAll();
}
