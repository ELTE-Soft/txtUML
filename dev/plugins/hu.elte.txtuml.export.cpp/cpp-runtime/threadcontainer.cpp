#include "threadcontainer.hpp"
#include <algorithm>

ThreadContainer::ThreadContainer(): active_threads(0),expected_threads(0) {}

void ThreadContainer::addThread(std::thread* th)
{
	std::unique_lock<std::mutex> mlock(_mutex);

	threads.insert
		(std::pair<std::thread::id, EventProcessorThread>
			(th->get_id(), std::move(EventProcessorThread(th))));

	active_threads++;
}



void ThreadContainer::gettingThreadsReadyToStop()
{
    std::unique_lock<std::mutex> mlock(_mutex);

	std::map<std::thread::id,EventProcessorThread>::iterator it = threads.begin();
	std::map<std::thread::id,EventProcessorThread>::iterator it2;
    while(isTooManyWorkes())
    {
		if(it->second._state == thread_state::working)
        {
		   it->second._state = thread_state::ready_to_stop;
		   it++;

        }
		else if (it->second._state == thread_state::stopped)
		{
			active_threads--;
			it2 = it;
			it++;
			threads.erase(it2);
		}
		else
		{
			it++;
		}
    }

    mlock.unlock();

}

void ThreadContainer::signStop(std::thread::id id)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	threads[id]._state = stopped;
}

bool ThreadContainer::isReadyToStop(std::thread::id thread_id)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	
	if(threads.count(thread_id) > 0)
	{
		return threads[thread_id]._state == thread_state::ready_to_stop;
	}
	else
	{
		return false;
	}
}

void ThreadContainer::removeAll()
{
	std::unique_lock<std::mutex> mlock(_mutex);
	threads.clear();	
}

ThreadContainer::~ThreadContainer()
{
	removeAll();
}


