#include "threadcontainer.hpp"
#include <algorithm>


namespace Execution
{

ThreadContainer::ThreadContainer() : active_threads(0), expected_threads(0) {}

void ThreadContainer::addThread(std::thread* th)
{
	std::unique_lock<std::mutex> mlock(_mutex);

	threads.insert(std::pair<std::thread::id, EventProcessorThread>(th->get_id(), EventProcessorThread(th)));
	active_threads++;
}



void ThreadContainer::gettingThreadsReadyToStop(ES::SharedPtr<std::condition_variable> cond)
{

	cont_it it = threads.begin();
	cont_it it2;
	while (isTooManyWorkers() && it != threads.end())
	{
		if (it->second.isWorking() && !it->second.threadsAreIdentical())
		{
			active_threads--;
			modifyThreadState(it->first, EventProcessorThread::ThreadState::ReadyToStop);
			cond->notify_all();
			it2 = it;
			it++;
			it2->second.waitFinishing();
			threads.erase(it2);

		}
		else
		{
			it++;
		}
	}

}

void ThreadContainer::modifyThreadState(EventProcessorThread::ThreadId id, EventProcessorThread::ThreadState state)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	threads[id].modifyState(state);
}

bool ThreadContainer::isReadyToStop(EventProcessorThread::ThreadId thread_id)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	return threads.count(thread_id) > 0 && !threads[thread_id].isWorking();
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

}



