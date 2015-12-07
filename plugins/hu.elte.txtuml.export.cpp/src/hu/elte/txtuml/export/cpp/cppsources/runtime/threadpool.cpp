#include <atomic>
#include <functional>
#include <complex> 

#include "threadpool.hpp"



StateMachineThreadPool::StateMachineThreadPool(size_t threads_,int messages_to_procces_)
    :   stop(true),worker_threads(0),threads(threads_),messages_to_procces(messages_to_procces_), delta(10), 
		future_getter_thread(new std::thread(&StateMachineThreadPool::futureGetter,this) ) {}
	
void StateMachineThreadPool::stopPool()
{
	stop = true;
	cond.notify_all();
	future_cond.notify_all();
	future_cond_alt.notify_all();
	
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
		
		//check if there are too many threads..
		std::unique_lock<std::mutex> fmlock(future_mu);
		if(workers.isTooManyWorkes())
		{
			workers.reduceActiveThreads();
			while(!getter_ready)
			{
				future_cond.notify_one();
				future_cond_alt.wait(fmlock);
			}
			getter_ready = false;
			f = std::async(&ThreadContainer::removeThread, &workers, std::this_thread::get_id());

			future_cond.notify_one();
			fmlock.unlock();
			return;
		}
		else
		{
			fmlock.unlock();
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
		
		if(sm != nullptr)
		{
			for(int i = 0; i < messages_to_procces && !sm->emptyMessageQueue(); ++i)
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

void StateMachineThreadPool::futureGetter()
{
	std::unique_lock<std::mutex> fmlock(future_mu);
	getter_ready = true;
	while(!this->stop)
	{
		future_cond.wait(fmlock);
		if(!this->stop)
		{
			f.get();
			getter_ready = true;
			future_cond_alt.notofy_one();
		}
		
	}
}

void StateMachineThreadPool::startPool()
{
	std::unique_lock<std::mutex> mlock(start_mu);
	
	stop = false;
	workers.setExpectedThreads(threads);
	for(size_t i = 0;i<threads;++i)
	{
		workers.addThread(new std::thread(&StateMachineThreadPool::task,this));
	}
	
	mlock.unlock();
        
}

void StateMachineThreadPool::modifiedThreads(int n)
{
	std::unique_lock<std::mutex> mlock(start_mu);
	workers.setExpectedThreads(n);

	while(workers.isTooFewWorkes())
	{
		workers.addThread(new std::thread(&StateMachineThreadPool::task,this));	
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
	
    future_getter_thread->join();
	delete future_getter_thread;
}
