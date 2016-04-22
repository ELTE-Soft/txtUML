#include <atomic>
#include <functional>
#include <complex> 

#include "threadpool.hpp"



StateMachineThreadPool::StateMachineThreadPool(int threads_)
    : threads(threads_),stop(true),worker_threads(0) {}
	
void StateMachineThreadPool::stopPool()
{
	stop = true;
	cond.notify_all();
	
}

void StateMachineThreadPool::startPool()
{
	
	stop = false;
	stop_request = false;
	workers.setExpectedThreads(threads);
	for(int i = 0; i < threads; ++i)
	{
		workers.addThread(new std::thread(&StateMachineThreadPool::task,this));
	}
        
}

void StateMachineThreadPool::stopUponCompletion()
{
	std::unique_lock<std::mutex> lock(stop_request_mu);
	if(stateMachines.empty() && worker_threads == 0)
	{
		stopPool();
	}
	else
	{
		stop_request = true;
		stop_request_cond.wait(lock, [this]{return this->stateMachines.empty() && this->worker_threads == 0;});
	}

}

void StateMachineThreadPool::task()
{	
	while(!this->stop))
    {
		
		if (workers.isReadyToStop(std::this_thread::get_id()))
		{
			workers.signStop((std::this_thread::get_id()));
			return;
		}
		
		std::unique_lock<std::mutex> mlock(mu);
		
		StateMachineI* sm = nullptr;
		while(!sm && !this->stop)
		{
			
			if(!stateMachines.empty())
			{
				incrementWorkers();
				stateMachines.pop_front(sm);			
							
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
			
		}
		
		reduceWorkers();
		
		if(stop_request)
		{
			stop_request_cond.notify_one();
		}
					
    }
	
}

void StateMachineThreadPool::modifiedThreads(int n)
{
	workers.setExpectedThreads(n);

        if (workers.isTooManyWorkes())
        {
            workers.gettingThreadsReadyToStop();
        }
        while (workers.isTooFewWorkes())
		{
            workers.addThread(new std::thread(&StateMachineThreadPool::task,this));
		}


	
}


void StateMachineThreadPool::incrementWorkers()
{	
	worker_threads++;

}

void StateMachineThreadPool::reduceWorkers()
{
	worker_threads--;
}

void StateMachineThreadPool::enqueObject(StateMachineI* sm)
{
        stateMachines.push_back(sm);
        cond.notify_one();
}

// the destructor joins all threads
StateMachineThreadPool::~StateMachineThreadPool()
{
    stopPool();
	workers.removeAll();
}
