#include <atomic>
#include <functional>
#include <complex> 

#include "threadpool.hpp"



StateMachineThreadPool::StateMachineThreadPool(int threads_)
    : threads(threads_),stop(true),worker_threads(0), reduce_number(0) {}
	
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

void StateMachineThreadPool::stopUponCompletion()
{
	
	std::unique_lock<std::mutex> mlock(complite_mu);
	
	if(!(stateMachines.empty() && worker_threads == 0))
	{
		complite_cond.wait(mlock,[this]{return this->stateMachines.empty() && this->worker_threads == 0;});
	}
		
	stopPool();
	


}

void StateMachineThreadPool::task()
{	
	while(!this->stop)
    {
		
		std::unique_lock<std::mutex> mlock(mu);
		
                if(workers.getState(std::this_thread::get_id()) == thread_state::ready_to_stop)
                {
                    workers.stopThread(std::this_thread::get_id());
                    return;
                }
		
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
		
		
		
		if(!sm->isStarted())
		{
			stateMachines.push_back(sm);
			sm = nullptr;
		}
		else if(sm->isStarted() && !sm->isInitialized())
		{
			sm->init();
		}
		
		if(sm)
		{
			for(int i = 0; i < 5 && !sm->emptyMessageQueue(); ++i)
			{
				sm->processEventVirtual();
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
			complite_cond.notify_one();
		}
					
    }
	
}

void StateMachineThreadPool::modifiedThreads(int n)
{
	workers.setExpectedThreads(n);

        if (workers.isTooManyWorkes())
        {
            workers.gettingThreadsReadyToStop();
            reduce_number++;
        }
        while (workers.isTooFewWorkes())
	{
            workers.addThread(new std::thread(&StateMachineThreadPool::task,this));
	}

        if (reduce_number == 5)
        {
            workers.eraseInactiveThreads();
            reduce_number = 0;
        }


	
}


void StateMachineThreadPool::incrementWorkers()
{
	std::unique_lock<std::mutex> mlock(worker_mu);
	worker_threads++;
	mlock.unlock();
}

void StateMachineThreadPool::reduceWorkers()
{
	std::unique_lock<std::mutex> mlock(worker_mu);
	worker_threads--;
	mlock.unlock();
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
