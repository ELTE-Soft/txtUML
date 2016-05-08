#include <atomic>
#include <functional>
#include <complex> 

#include "threadpool.hpp"



StateMachineThreadPool::StateMachineThreadPool(int threads_)
    : threads(threads_),stop(true){}
	
void StateMachineThreadPool::stopPool()
{
	stop = true;
	cond.notify_all();
	
}

void StateMachineThreadPool::startPool()
{
	
	stop = false;
	workers.setExpectedThreads(threads);
	for(int i = 0; i < threads; ++i)
	{
		workers.addThread(new std::thread(&StateMachineThreadPool::task,this));
	}
        
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
		
		std::unique_lock<std::mutex> mlock(mu);
		
		StateMachineI* sm = nullptr;
		while(!sm && !this->stop)
		{
			
			if(!stateMachines.empty())
			{
				incrementWorkers();
				stateMachines.pop_front(sm);			
							
			}
			else if (workers.isReadyToStop(std::this_thread::get_id()))
			{
				return;
			}
			else
			{
				cond.wait(mlock);
			}
		}
		mlock.unlock();
		
	
		if(sm)
		{
			
			if(sm->isStarted() && sm->isInitialized())
			{
				for(int i = 0; i < 5 && !sm->emptyMessageQueue(); ++i)
				{
					sm->processEventVirtual();
				}
					
			}
			else if(sm->isStarted() && !sm->isInitialized())
			{
				sm->init();
			}
			
			if(!sm->emptyMessageQueue())
			{
				stateMachines.push_back(sm);
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

void StateMachineThreadPool::modifiedThreads(int n)
{
		std::unique_lock<std::mutex> mlock(modifie_mutex);
		
		workers.setExpectedThreads(n);
        if (workers.isTooManyWorkes())
        {
            workers.gettingThreadsReadyToStop(cond);
        }
        while (workers.isTooFewWorkes())
		{
            workers.addThread(new std::thread(&StateMachineThreadPool::task,this));
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

void StateMachineThreadPool::enqueObject(StateMachineI* sm)
{
        stateMachines.push_back(sm);
        cond.notify_one();
}

StateMachineThreadPool::~StateMachineThreadPool()
{
    stopPool();
	workers.removeAll();
}
