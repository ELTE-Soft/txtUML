#include "threadcontainer.hpp"
#include <algorithm>

ThreadContainer::ThreadContainer(): active_threads(0),expected_threads(0) {}

void ThreadContainer::addThread(std::thread* th)
{
	std::unique_lock<std::mutex> mlock(_mutex);

	threads.insert
		(std::pair<std::thread::id, EventProcessorThread>
                        (th->get_id(), EventProcessorThread(th)));

	active_threads++;
}



void ThreadContainer::gettingThreadsReadyToStop(std::condition_variable& cond)
{
		
	cont_it it = threads.begin();
	cont_it it2;
	while (isTooManyWorkers() && it != threads.end())
	{
		if (it->second._state == thread_state::working)
		{
			active_threads--;
			modifyThreadState(it->first, thread_state::ready_to_stop);
			cond.notify_all();
			it2 = it;			
			it++;
			it2->second._thread->join();
			threads.erase(it2);

		}
		else
		{
			it++;
		}
	}


}

void ThreadContainer::modifyThreadState(std::thread::id id, thread_state state)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	threads[id]._state = state;
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


