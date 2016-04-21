#include "threadcontainer.hpp"
#include <algorithm>

ThreadContainer::ThreadContainer(): active_threads(0),expected_threads(0) {}

void ThreadContainer::addThread(std::thread* th)
{
	std::unique_lock<std::mutex> mlock(_mutex);

	threads.insert
		(std::pair<std::thread::id, EventProcessorThread*>
			(th->get_id(), new EventProcessorThread(th)));

	active_threads++;
}

void ThreadContainer::gettingThreadsReadyToStop()
{
    std::unique_lock<std::mutex> mlock(_mutex);

    for(std::map<std::thread::id,EventProcessorThread*>::iterator it =
        threads.begin();isTooManyWorkes(); it++)
    {
		if(it->second->_state == thread_state::working)
        {
			it->second->_state = thread_state::ready_to_stop;
           active_threads--;
        }
    }

    mlock.unlock();

}

void ThreadContainer::stopThread(std::thread::id thread_id)
{
    std::unique_lock<std::mutex> mlock(_mutex);
	threads[thread_id]->_state = thread_state::stopped;
}

void ThreadContainer::eraseInactiveThreads()
{
    std::unique_lock<std::mutex> mlock(_mutex);

    for(std::map<std::thread::id,EventProcessorThread*>::iterator it = threads.begin();
	it != threads.end(); it++)
    {
        if(it->second->_state == thread_state::stopped)
        {
		   delete (it->second);
        }
    }

    mlock.unlock();
}

bool ThreadContainer::isReadyToStop(std::thread::id thread_id)
{
    std::unique_lock<std::mutex> mlock(_mutex);
	if (threads.count(thread_id) > 0)
		return threads[thread_id]->_state == thread_state::ready_to_stop;
	else
		return false;

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


