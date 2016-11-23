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
		

		
		IStateMachine* sm = nullptr;
		while(!sm && !this->stop)
		{
			
			
							
			if (workers.isReadyToStop(std::this_thread::get_id()))
			{
				return;
			}

			stateMachines.pop_front(sm);
			incrementWorkers();
		}
		
	
		if (sm)
		{
			
			if (!sm->isInitialized()) sm->init();
			for (int i = 0; i < 5 && !sm->emptyMessageQueue(); ++i)
			{
				sm->processEventVirtual();
			}
					
			
			if (!sm->emptyMessageQueue())
			{
				stateMachines.push_back(sm);
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
		if (workers.isTooManyWorkes())
		{
			workers.gettingThreadsReadyToStop(cond);
		}
		while (workers.isTooFewWorkes ())
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

void StateMachineThreadPool::enqueObject(IStateMachine* sm)
{
        stateMachines.push_back(sm);
}

StateMachineThreadPool::~StateMachineThreadPool()
{
    	stopPool();
	workers.removeAll();
}
